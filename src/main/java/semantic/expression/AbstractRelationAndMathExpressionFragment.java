package semantic.expression;

import codegenerate.MathOpt;
import semantic.symboltable.type.SymbolTableEntryType;
import semantic.value.MathValue;
import semantic.value.Value;

import java.util.Objects;


/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public abstract class AbstractRelationAndMathExpressionFragment extends TypedExpressionElement {

    protected enum State {
        WAIT_FIRST,
        WAIT_OP,
        WAIT_SECOND,
        DONE,
    }

    protected State state;
    protected TypedExpressionElement first;
    protected TypedExpressionElement second;
    protected MathOpt operator;

    public AbstractRelationAndMathExpressionFragment() {
        this.state = State.WAIT_FIRST;
    }

    protected void pushOp(String op) {
        if (state == State.WAIT_OP) {
            this.operator = MathOpt.operators.get(op);
            this.state = State.WAIT_SECOND;
        }
    }

    public abstract SymbolTableEntryType getType();

    @Override
    public Value getValue() {
        if (this.state == State.WAIT_OP) {
            return first.getValue();
        } else {
            return new MathValue(this.operator, first.getValue(), second.getValue());
        }
    }

    protected void subAccept(ExpressionElement expr) {
        switch (state) {
            case WAIT_FIRST:
                this.first = (TypedExpressionElement) expr;
                this.state = State.WAIT_OP;
                break;

            case WAIT_SECOND:
                this.second = (TypedExpressionElement) expr;

                // type checking
                if (!Objects.equals(first.getType(), second.getType())) {
                    throw new RuntimeException("Type error: " + first.getType() + " is not compatible with " +
                            second.getType() + " for operator: " + operator.symbol);
                }

                this.state = State.DONE;
                context.finish();
                break;

            default:
                super.accept(expr);
                break;
        }
    }
}
