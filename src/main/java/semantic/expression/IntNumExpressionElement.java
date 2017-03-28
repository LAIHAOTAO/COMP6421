package semantic.expression;

import semantic.symboltable.type.IntType;
import semantic.symboltable.type.SymbolTableEntryType;
import semantic.value.StaticIntValue;
import semantic.value.Value;

/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public class IntNumExpressionElement extends TypedExpressionElement{

    private final int i;

    public IntNumExpressionElement(int i) {
        this.i = i;
    }

    @Override
    public Value getValue() {
        return new StaticIntValue(i);
    }

    @Override
    public SymbolTableEntryType getType() {
        return new IntType();
    }
}
