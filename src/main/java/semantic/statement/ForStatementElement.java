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
import semantic.value.Value;
import semantic.value.VoidValue;

/**
 * Created by ERIC_LAI on 2017-04-06.
 */
public class ForStatementElement extends ExpressionElement implements Statement {

    enum State {
        INIT,
        CONDITION,
        OPERATION,
        STATEMENT,
        DONE,
    }

    private State state;
    private AssignmentStatement init;
    private ExpressionElement condition;
    private AssignmentStatement operation;
    private StatementBlockElement statements;

    public ForStatementElement() {
        this.state = State.INIT;
    }

    @Override
    public void accept(ExpressionElement expr) {
        switch (state) {

            case INIT:
                state = State.CONDITION;
                if (expr instanceof AssignmentStatement)
                    init = (AssignmentStatement) expr;
                else
                    super.accept(expr);
                break;

            case CONDITION:
                state = State.OPERATION;
                if (expr instanceof RelationExpressionFragment)
                    condition = expr;
                else
                    super.accept(expr);
                break;

            case OPERATION:
                state = State.STATEMENT;
                if (expr instanceof AssignmentStatement)
                    operation = (AssignmentStatement) expr;
                else
                    super.accept(expr);
                break;

            case STATEMENT:
                if (expr instanceof StatementBlockElement) {
                    state = State.DONE;
                    statements = (StatementBlockElement) expr;
                    context.finish();
                } else
                    super.accept(expr);
                break;

            default:
                super.accept(expr);
                break;
        }
    }

    @Override
    public void generateCode(CodeGenerateContext c) {
        int labelId = LabelGenerator.instance.getUniqueLabel();
        String loopTopLabel = "loop_top_" + labelId;
        String loopEndLabel = "loop_end_" + labelId;

        // generate initialization code for forloop statement
        c.setNextComment("for loop initialization");
        init.generateCode(c);

        // generate the condition statement code, every time loop begin from here
        c.setNextLabel(loopTopLabel);
        Register tmp = condition.getValue().getRegisterValue(c).getRegister();
        c.appendInstruction(new BranchInstruction(BranchInstruction.Kind.IfZero, tmp, loopEndLabel)
                .setComment("break out of loop"));
        if (!tmp.reserved) c.registerManager.freeRegister(tmp);

        // generate the code of the statements inside the loop
        statements.generateCode(c);

        // generate the operation code (usually increment or decrement operation)
        operation.generateCode(c);

        // jump back to the condition position
        c.appendInstruction(new JumpAndLinkInstruction(loopTopLabel));
        // jump out of the loop code
        c.appendInstruction(new NoopInstruction().setLabel(loopEndLabel));
    }

    @Override
    public Value getValue() {
        return VoidValue.get();
    }
}
