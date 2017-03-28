package semantic.expression;

/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public class RelationExpressionFragment extends AbstractRelationAndMathExpressionFragment {

    public RelationExpressionFragment() {
        super();
    }

    @Override
    public void pushRelationOp(String op) {
        super.pushOp(op);
    }

    @Override
    public void accept(ExpressionElement expr) {

        if (expr instanceof RelationExpressionFragment
                || expr instanceof AdditionExpressionFragment) {

           subAccept(expr);
        } else {
            super.accept(expr);
        }
    }

}
