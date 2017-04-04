package semantic.value;

import codegenerate.CodeGenerateContext;
import codegenerate.MathOpt;
import codegenerate.Register;
import codegenerate.instruction.JumpAndLinkInstruction;
import codegenerate.instruction.MathOptImmInstruction;
import codegenerate.instruction.MathOptInstruction;
import codegenerate.instruction.SWInstruction;
import semantic.expression.TypedExpressionElement;
import semantic.symboltable.entry.FunctionAbstractEntry;
import semantic.symboltable.type.SymbolTableEntryType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ERIC_LAI on 2017-04-03.
 */
public class FunctionCallValue extends DynamicValue implements Value {

    private List<Value> parameters;
    private List<SymbolTableEntryType> paramList;
    private int scopeSize;
    private String callingLabel;

    public FunctionCallValue(FunctionAbstractEntry entry, List<TypedExpressionElement> expressions) {
        paramList = entry.getParamTypeList();

        // parameters checking
        if (paramList.size() != expressions.size()) {
            throw new RuntimeException(entry.getName() + " requires " + paramList.size() +
                    " parameters, but only found " + expressions.size() + " while calling it");
        }
        parameters = new ArrayList<>(expressions.size());
        for (int i = 0; i < paramList.size(); i++) {
            TypedExpressionElement expr = expressions.get(i);
            SymbolTableEntryType type = paramList.get(i);
            if (!expr.getType().equals(type)) {
                throw new RuntimeException("the " + (i + 1) + " parameter type expected " + type
                        + ", but found " + expr.getType());
            }
            parameters.add(expr.getValue());
        }

        callingLabel = entry.getLabel();
        scopeSize = entry.getScope().getSize();
    }

    @Override
    public Value getUsedValue(CodeGenerateContext context) {

        // the first parameter should be one word below stack pointer so
        // initialize it with -4
        int paramsOffset = -4;
        // add parameters in reversed order
        for (int i = parameters.size() - 1; i >= 0; i--) {
            Register reg = parameters.get(i).getRegisterValue(context).getRegister();
            context.appendInstruction(new SWInstruction(paramsOffset, Register.STACK_POINTER, reg)
                    .setComment("add parameter"));
            // pass value or pass reference, the offset should always 4
            paramsOffset -= 4;
        }

        // store and update the frame pointer
        context.appendInstruction(new SWInstruction(
                paramsOffset - 4, Register.STACK_POINTER, Register.FRAME_POINTER
        ).setComment("store the previous frame pointer"));
        context.appendInstruction(new MathOptImmInstruction(
                MathOpt.ADD.immediateOpcode, Register.FRAME_POINTER, Register.STACK_POINTER, paramsOffset - 4
        ).setComment("update the frame pointer"));

        // update the stack pointer to the new function
        int varOffset = scopeSize - (Math.abs(paramsOffset - 4) + 8);
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
