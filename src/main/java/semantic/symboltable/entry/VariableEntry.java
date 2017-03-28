package semantic.symboltable.entry;

import semantic.symboltable.SymbolTable;
import semantic.symboltable.SymbolTableEntry;
import semantic.symboltable.type.SymbolTableEntryType;

/**
 * Created by ERIC_LAI on 2017-03-25.
 */
public class VariableEntry extends SymbolTableEntry {
    public VariableEntry(String name, SymbolTable scope) {
        super(name, Kind.Variable, null, scope);
    }

    public VariableEntry(String name, SymbolTableEntryType type, SymbolTable scope) {
        super(name, Kind.Variable, type, scope);
    }

    @Override
    protected int calculateSize() {
        // variable can be int | float | class
        // but each class variable should have a copy the class member variable
        return getType().getSize();
    }
}
