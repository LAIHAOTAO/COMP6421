package semantic.Statement;

import semantic.expression.ExpressionElement;
import semantic.expression.VariableElementFragment;

/**
 * Created by ERIC_LAI on 2017-03-24.
 */
public class AssignmentStatement extends ExpressionElement implements Statement{

    public enum State {
        WAIT_LHS,
        LHS,
        WAIT_RHS,
        RHS,
        DONE,
    }

    private State currentState;
    private String lhs;
    private String rhs;

    public AssignmentStatement() {
        this.currentState = State.WAIT_LHS;
    }

    @Override
    public void pushID(String varName) {
        if (currentState == State.WAIT_LHS) {
            currentState = State.LHS;
            // put a new expression in the top of the symContext
            context.push(new VariableElementFragment(varName));
        } else if (currentState == State.WAIT_RHS) {
            currentState = State.RHS;
            // put a new expression in the top of the symContext
            context.push(new VariableElementFragment(varName));
        }
    }

    @Override
    public void accept(ExpressionElement expr) {
        if (currentState == State.LHS) {
            lhs = expr.toString();
            currentState = State.WAIT_RHS;
        } else if (currentState == State.RHS) {
            rhs = expr.toString();
            currentState = State.DONE;
            context.finish();
            System.out.println("finish!!!!" + lhs + " = " + rhs);
        } else {
            throw new RuntimeException("Unexpected " + expr + " while in state " + currentState);
        }
    }
}
