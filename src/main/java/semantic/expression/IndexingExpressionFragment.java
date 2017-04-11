package semantic.expression;

import codegenerate.MathOpt;
import exception.CompilerException;
import semantic.symboltable.type.ArrayType;
import semantic.value.MathValue;
import semantic.value.StaticNumValue;
import semantic.value.StaticValue;
import semantic.value.Value;
import sun.tools.java.CompilerError;

import java.util.Collections;
import java.util.List;

/**
 * Created by ERIC_LAI on 2017-03-31.
 */
public class IndexingExpressionFragment extends ExpressionElement {

    private Value offset;
    private int currentDimension;
    private int typeSize;
    private List<Integer> dimensions;

    public IndexingExpressionFragment() {
        this.offset = new StaticNumValue(0);
        this.currentDimension = 1;
        this.typeSize = 0;
        this.dimensions = Collections.emptyList();
    }

    public IndexingExpressionFragment(ArrayType type) {
        this.offset = new StaticNumValue(0);
        this.currentDimension = 1;
        this.typeSize = type.getArrayTypeType().getSize();
        this.dimensions = type.getDimension();
    }

    @Override
    public void accept(ExpressionElement expr) {
        if (expr instanceof AdditionExpressionFragment) {
            int index = ((StaticValue) expr.getValue()).intValue();
            if (index < dimensions.get(currentDimension - 1)) {
                if (currentDimension == dimensions.size()) {
                    Value i = new MathValue(
                            MathOpt.MULTIPLY, expr.getValue(),
                            new StaticNumValue(4));
                    offset = new MathValue(MathOpt.ADD, offset, i);
                    context.finish();
                } else {
                    Value i = new MathValue(
                            MathOpt.MULTIPLY, expr.getValue(),
                            new StaticNumValue(4 * dimensions.get(currentDimension - 1)));
                    offset = new MathValue(MathOpt.ADD, offset, i);
                    currentDimension++;
                }
            } else {
                throw new CompilerException("Array out of bound, please check carefully");
            }
        } else {
            super.accept(expr);
        }
    }

    @Override
    public Value getValue() {
        if (currentDimension != dimensions.size()) {
            throw new CompilerError("Not enough indices");
        } else {
            return offset;
        }
    }
}
