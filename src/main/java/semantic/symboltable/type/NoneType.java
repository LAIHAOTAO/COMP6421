package semantic.symboltable.type;

import semantic.symboltable.SymbolTable;

/**
 * Created by ERIC_LAI on 2017-03-26.
 */
public class NoneType implements SymbolTableEntryType {
    @Override
    public int getSize() {
        throw new RuntimeException("It is no possible to call NoneType getSize() method ...");
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
