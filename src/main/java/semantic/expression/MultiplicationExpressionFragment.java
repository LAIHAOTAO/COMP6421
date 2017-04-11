package semantic.expression;

import codegenerate.MathOpt;
import exception.CompilerException;
import semantic.symboltable.type.IntType;
import semantic.symboltable.type.SymbolTableEntryType;
import semantic.value.MathValue;
import semantic.value.StaticNumValue;
import semantic.value.Value;

import java.util.Objects;

/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public class MultiplicationExpressionFragment extends AbstractRelationAndMathExpressionFragment {

    private enum State {
        WAIT_FIRST,
        FIRST,
        WAIT_OP,
        WAIT_SECOND,
        SECOND,
        DONE,
    }

    private State state;

    public MultiplicationExpressionFragment() {
        this.state = State.WAIT_FIRST;
    }

    @Override
    public SymbolTableEntryType getType() {
        try {
            return first.getType();
        } catch (Throwable e) {
            return new IntType();
        }
    }

    @Override
    public void pushID(String varName) {
        switch (this.state) {
            case WAIT_FIRST:
                context.push(new VariableElementFragment(varName));
                this.state = State.FIRST;
                break;
            case WAIT_SECOND:
                context.push(new VariableElementFragment(varName));
                this.state = State.SECOND;
                break;
            default:
                super.pushID(varName);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void accept(ExpressionElement expr) {

        if (expr instanceof MultiplicationExpressionFragment ||
                expr instanceof VariableElementFragment ||
                expr instanceof AdditionExpressionFragment ||
                expr instanceof FunctionCallExpressionFragment) {

            switch (this.state) {
                case WAIT_FIRST:
                case FIRST:
                    this.first = (TypedExpressionElement) expr;
                    this.state = State.WAIT_OP;
                    break;

                case WAIT_SECOND:
                case SECOND:
                    this.second = (TypedExpressionElement) expr;

                    // type checking
                    if (!Objects.equals(first.getType(), second.getType())) {
                        throw new CompilerException("Type error: " + first.getType() + " is not compatible with " +
                                second.getType() + " for operator: " + operator.symbol);
                    }

                    this.state = State.DONE;
                    context.finish();
                    break;
                default:
                    super.accept(expr);
                    break;
            }
        } else {
            super.accept(expr);
        }
    }

    @Override
    public void pushIntNum(int i) {
        switch (this.state) {
            case WAIT_FIRST:
                this.state = State.WAIT_OP;
                this.first = new IntNumExpressionElement(i);
                break;
            case WAIT_SECOND:
                this.state = State.DONE;
                this.second = new IntNumExpressionElement(i);
                context.finish();
                break;
            default:
                super.pushIntNum(i);
                break;
        }
    }

    @Override
    public void pushMultiplicationOp(String op) {
        if (this.state == State.WAIT_OP) {
            this.operator = MathOpt.operators.get(op);
            this.state = State.WAIT_SECOND;
        } else {
            super.pushMultiplicationOp(op);
        }
    }

    @Override
    public Value getValue() {
        try {
            if (this.state == State.WAIT_OP) {
                return this.first.getValue();
            } else {
                return new MathValue(this.operator, this.first.getValue(), this.second.getValue());
            }
        } catch (Throwable e) {
            return new StaticNumValue(0);
        }
    }


}
