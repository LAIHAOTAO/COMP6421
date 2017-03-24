package semantic.statement;

/**
 * Created by ERIC_LAI on 2017-03-23.
 */
public abstract class ExpressionElement {

    protected final ExpressionContext context = ExpressionContext.instance;

    public void acceptSubElement(ExpressionElement e) {}

    public void pushIdentifier(String id) {

    }

    public abstract String getValue();

    public void pushIntLiteral(int i) {}

    public void pushFloatLiteral(float f) {
        pushIntLiteral((int)f); // for now we just cast them
    }

//    public void pushAdditionOperator(MathOperation operator) {
//    }
//
//    public void pushMultiplicationOperator(MathOperation operator) {
//    }
//
//    public void pushRelationOperator(MathOperation operator) {
//    }
}
