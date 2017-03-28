package semantic.expression;

import codegenerate.MathOpt;
import semantic.symboltable.type.SymbolTableEntryType;
import semantic.value.Value;

import java.util.Objects;


/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public class AbstractRelationAndMathExpressionFragment extends TypedExpressionElement {

    protected enum State {
        WAITING_FOR_FIRST,
        WAITING_FOR_OPTOR,
        WAITING_FOR_SECOND,
        DONE,
    }

    protected State state;
    protected TypedExpressionElement first;
    protected TypedExpressionElement second;
    protected MathOpt operator;

    public AbstractRelationAndMathExpressionFragment() {
        this.state = State.WAITING_FOR_FIRST;
    }

    protected void pushOp(String op) {
        if (state == State.WAITING_FOR_OPTOR) {
            this.operator = MathOpt.operators.get(op);
            this.state = State.WAITING_FOR_SECOND;
        }
    }

    @Override
    public SymbolTableEntryType getType() {
        return first.getType();
    }

    @Override
    public Value getValue() {
        // todo
        return null;
    }

    protected void subAccept(ExpressionElement expr) {
        switch (state) {
            case WAITING_FOR_FIRST:
                this.first = (TypedExpressionElement) expr;
                this.state = State.WAITING_FOR_OPTOR;
                break;

            case WAITING_FOR_SECOND:
                this.second = (TypedExpressionElement) expr;

                // type checking
                if (Objects.equals(first.getType(), second.getType())) {
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
