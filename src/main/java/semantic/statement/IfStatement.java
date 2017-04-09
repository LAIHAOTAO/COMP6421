package semantic.statement;

import codegenerate.CodeGenerateContext;
import codegenerate.LabelGenerator;
import codegenerate.Register;
import codegenerate.instruction.BranchInstruction;
import codegenerate.instruction.JumpAndLinkInstruction;
import codegenerate.instruction.NoopInstruction;
import semantic.expression.ExpressionElement;
import semantic.expression.RelationExpressionFragment;
import semantic.expression.StatementBlockElement;
import semantic.value.RegisterValue;
import semantic.value.Value;
import semantic.value.VoidValue;

/**
 * Created by ERIC_LAI on 2017-04-05.
 */
public class IfStatement extends ExpressionElement implements Statement{

    private enum State {
        CONDITION,
        THEN_BLOCK,
        ELSE_BLOCK,
        DONE,
    }

    private State state;
    private ExpressionElement condition;
    private StatementBlockElement thenBlock;
    private StatementBlockElement elseBlock;

    public IfStatement() {
        this.state = State.CONDITION;
    }

    @Override
    public void accept(ExpressionElement expr) {
        switch (state) {
            case CONDITION:
                if (expr instanceof RelationExpressionFragment) {
                    condition = expr;
                    state = State.THEN_BLOCK;
                } else super.accept(expr);
                break;
            case THEN_BLOCK:
                if (expr instanceof Statement) {
                    thenBlock = (StatementBlockElement) expr;
                    state = State.ELSE_BLOCK;
                } else super.accept(expr);
                break;
            case ELSE_BLOCK:
                if (expr instanceof Statement) {
                    elseBlock = (StatementBlockElement) expr;
                    state = State.DONE;
                    context.finish();
                } else super.accept(expr);
                break;
            default:
                super.accept(expr);
                break;
        }
    }

    @Override
    public void generateCode(CodeGenerateContext c) {
        RegisterValue conditionValue = condition.getValue().getRegisterValue(c);
        Register reg = conditionValue.getRegister();

        int labelId = LabelGenerator.instance.getUniqueLabel();
        String elseLabel  = "else_"  + labelId;
        String endifLabel = "endif_" + labelId;

        // zero means false, so if the condition is false, jump to the else block
        c.appendInstruction(new BranchInstruction(BranchInstruction.Kind.IfZero, reg, elseLabel)
                .setComment("if statement"));

        // if the condition being satisfied, program will execute the then block
        // instead of jumping to the else block
        thenBlock.generateCode(c);

        // if the program execute here, need to jump out of the if statement, since the following
        // code is the else block which do not need to be executed
        c.appendInstruction(new JumpAndLinkInstruction(endifLabel)
                .setComment("jump out of the else block"));

        // else block code is always following the then block code
        c.setNextLabel(elseLabel);
        elseBlock.generateCode(c);

        // end of the if statement
        c.appendInstruction(new NoopInstruction().setLabel(endifLabel)
            .setComment("end of the if statement"));
    }

    @Override
    public Value getValue() {
        return VoidValue.get();
    }

}
