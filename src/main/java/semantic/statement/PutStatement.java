package semantic.statement;

import codegenerate.CodeGenerateContext;
import codegenerate.Register;
import codegenerate.instruction.PutInstruction;
import semantic.expression.ExpressionElement;
import semantic.expression.RelationExpressionFragment;
import semantic.value.Value;
import semantic.value.VoidValue;

/**
 * Created by ERIC_LAI on 2017-04-07.
 */
public class PutStatement extends ExpressionElement implements Statement {

    private RelationExpressionFragment expr;

    @Override
    public void accept(ExpressionElement expr) {
        if (expr instanceof RelationExpressionFragment) {
            this.expr = (RelationExpressionFragment) expr;
            context.finish();
        } else {
            super.accept(expr);
        }
    }

    @Override
    public void generateCode(CodeGenerateContext c) {
        Register register = expr.getValue().getRegisterValue(c).getRegister();
        c.appendInstruction(new PutInstruction(register));
        c.registerManager.freeRegister(register);
    }

    @Override
    public Value getValue() {
        return VoidValue.get();
    }
}
