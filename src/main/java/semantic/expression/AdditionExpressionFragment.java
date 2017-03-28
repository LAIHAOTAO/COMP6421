package semantic.expression;

/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public class AdditionExpressionFragment extends AbstractRelationAndMathExpressionFragment {

    public AdditionExpressionFragment() {
        super();
    }

    @Override
    public void pushAdditionOp(String op) {
        super.pushOp(op);
    }

    @Override
    public void accept(ExpressionElement expr) {
        if (expr instanceof AdditionExpressionFragment
                || expr instanceof MultiplicationExpressionFragment) {

            super.subAccept(expr);
        } else {
            super.accept(expr);
        }
    }
}
