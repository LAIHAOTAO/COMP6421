package semantic.statement;

/**
 * Created by ERIC_LAI on 2017-03-23.
 */
public class AssignStatement extends ExpressionElement {

    private enum State {
        LHS_INIT,
        LHS,
        RHS_INIT,
        RHS,
        DONE,
    }

    private State currentState;
    private String leftValue;
    private String rightValue;

    public AssignStatement() {
        this.currentState = State.LHS_INIT;
    }

    @Override
    public void pushIdentifier(String id) {
        if (currentState == State.LHS_INIT) {
            currentState = State.LHS;
            context.push(new VariableExpressionFragment(id));
        } else if (currentState == State.RHS_INIT) {
            currentState = State.RHS;
            context.push(new VariableExpressionFragment(id));
        } else {
            throw new RuntimeException("Something wrong while push an identifier in AssignStatement");
        }
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public void acceptSubElement(ExpressionElement e) {
        if (currentState == State.LHS) {
            leftValue = e.getValue();
            currentState = State.RHS_INIT;
        } else if (currentState == State.RHS) {
            rightValue = e.getValue();
            currentState = State.DONE;
            context.finishTopElement();
        } else {
            throw new RuntimeException("Unexpected " + e + " while in state " + currentState);
        }
    }

}
