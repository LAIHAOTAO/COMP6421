package semantic.Statement;

import codegenerate.CodeGenerateContext;
import codegenerate.MathOpt;
import codegenerate.Register;
import codegenerate.instruction.Instruction;
import codegenerate.instruction.MathOptImmInstruction;
import codegenerate.instruction.MathOptInstruction;
import exception.CompilerException;
import semantic.expression.ExpressionElement;
import semantic.expression.RelationExpressionFragment;
import semantic.value.*;

/**
 * Created by ERIC_LAI on 2017-04-04.
 */
public class ReturnStatement extends ExpressionElement implements Statement {

    private Value reValue;

    @Override
    public void accept(ExpressionElement expr) {
        if (expr instanceof RelationExpressionFragment) {
            reValue = expr.getValue();
            context.finish();
        } else super.accept(expr);
    }

    @Override
    public String generateCode(CodeGenerateContext c) {
        if (reValue == null) throw new CompilerException("return value is null");
        Value val = reValue.getUsedValue(c);
        Instruction i;
        if (val instanceof StaticNumValue) {
            i = new MathOptImmInstruction(
                    MathOpt.ADD.immediateOpcode, Register.RETURN, Register.ZERO, ((StaticValue) val).intValue()
            ).setComment("return value is a static value, get its value");
            c.appendInstruction(i);
        } else {
            if (val instanceof RegisterValue) {
                i = new MathOptInstruction(
                        MathOpt.ADD.opcode, Register.RETURN, Register.ZERO, ((RegisterValue) val).getRegister()
                ).setComment("return value is a register value, get its value");
                c.appendInstruction(i);
            } else {
                throw new CompilerException("Unexpected return type " + val.getClass());
            }
        }
        return i.toString();
    }

    @Override
    public Value getValue() {
        return VoidValue.get();
    }
}
