package semantic.expression;

import semantic.symboltable.type.SymbolTableEntryType;

/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public abstract class TypedExpressionElement extends ExpressionElement{

    public abstract SymbolTableEntryType getType();
}
