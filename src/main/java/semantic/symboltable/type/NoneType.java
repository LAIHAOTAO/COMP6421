package semantic.symboltable.type;

import semantic.symboltable.SymbolTable;

/**
 * Created by ERIC_LAI on 2017-03-26.
 */
public class NoneType implements SymbolTableEntryType {
    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public SymbolTable getScope() {
        return null;
    }

    @Override
    public String toString() {
        return "Null";
    }
}
