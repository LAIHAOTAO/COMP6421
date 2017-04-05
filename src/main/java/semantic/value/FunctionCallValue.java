package semantic.value;

import codegenerate.CodeGenerateContext;
import codegenerate.MathOpt;
import codegenerate.Register;
import codegenerate.instruction.JumpAndLinkInstruction;
import codegenerate.instruction.MathOptImmInstruction;
import codegenerate.instruction.SWInstruction;
import exception.CompilerException;
import semantic.expression.TypedExpressionElement;
import semantic.symboltable.entry.FunctionAbstractEntry;
import semantic.symboltable.type.SymbolTableEntryType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ERIC_LAI on 2017-04-03.
 */
public class FunctionCallValue extends DynamicValue implements Value {

    private List<Value> arguments;
    private List<SymbolTableEntryType> paramList;
    private int scopeSize;
    private String callingLabel;

    public FunctionCallValue(FunctionAbstractEntry entry, List<TypedExpressionElement> expressions) {
        paramList = entry.getParamTypeList();

        // arguments checking
        if (paramList.size() != expressions.size()) {
            throw new CompilerException(entry.getName() + " requires " + paramList.size() +
                    " arguments, but only found " + expressions.size() + " while calling it");
        }
        arguments = new ArrayList<>(expressions.size());
        for (int i = 0; i < paramList.size(); i++) {
            TypedExpressionElement expr = expressions.get(i);
            SymbolTableEntryType type = paramList.get(i);
            if (!expr.getType().equals(type)) {
                throw new CompilerException("the " + (i + 1) + " parameter type expected " + type
                        + ", but found " + expr.getType());
            }
            // pass the parameter to become argument, this "expr" should implement its "getValue()"
            // method to determine return value or reference !!!
            arguments.add(expr.getValue());
        }

        callingLabel = entry.getLabel();
        scopeSize = entry.getScope().getSize();
    }

    @Override
    public Value getUsedValue(CodeGenerateContext context) {

        // the first argument should be one word below stack pointer so
        // initialize it with -4
        int argumentOffset = -4;
        // add arguments in reversed order
        for (int i = arguments.size() - 1; i >= 0; i--) {
            Register reg = arguments.get(i).getRegisterValue(context).getRegister();
            context.appendInstruction(new SWInstruction(argumentOffset, Register.STACK_POINTER, reg)
                    .setComment("add parameter"));
            // pass value or pass reference, the offset should always 4
            argumentOffset -= 4;
        }

        // store and update the frame pointer
        context.appendInstruction(new SWInstruction(
                argumentOffset - 4, Register.STACK_POINTER, Register.FRAME_POINTER
        ).setComment("store the previous frame pointer"));
        context.appendInstruction(new MathOptImmInstruction(
                MathOpt.ADD.immediateOpcode, Register.FRAME_POINTER, Register.STACK_POINTER, argumentOffset - 4
        ).setComment("update the frame pointer"));

        // update the stack pointer to the new function
        int varOffset = scopeSize - (Math.abs(argumentOffset - 4) + 8);
        context.appendInstruction(new MathOptImmInstruction(
                MathOpt.ADD.immediateOpcode, Register.STACK_POINTER, Register.FRAME_POINTER, varOffset
        ).setComment("update the stack pointer"));

        // call jump and link instruction
        context.appendInstruction(new JumpAndLinkInstruction(Register.RETURN, callingLabel)
                .setComment("store return address and jump to " + callingLabel));

        return new RegisterValue(Register.RETURN);
    }

    @Override
    public RegisterValue getRegisterValue(CodeGenerateContext context) {
        return getUsedValue(context).getRegisterValue(context);
    }
}
