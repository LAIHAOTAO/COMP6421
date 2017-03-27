package semantic.expression;

import semantic.value.Value;

/**
 * Created by ERIC_LAI on 2017-03-24.
 */
public abstract class ExpressionElement {

    protected final ExpressionContext context = ExpressionContext.instance;

    public abstract Value getValue();

    public void pushID(String varName) {

    }

    public void accept(ExpressionElement expr) {

    }

    public void pushIntNum(int i) {

    }

    public void pushFloat(float f) {

    }

    public void pushAddtionOp(String op) {

    }

    public void pushMultiplicationOp(String op) {

    }

    public void pushRelationOp(String op) {

    }

}
