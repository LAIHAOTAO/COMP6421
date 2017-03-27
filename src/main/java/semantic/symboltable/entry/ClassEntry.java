package semantic.symboltable.entry;

import semantic.symboltable.SymbolTable;
import semantic.symboltable.SymbolTableEntry;
import semantic.symboltable.type.NoneType;

/**
 * Created by ERIC_LAI on 2017-03-25.
 */
public class ClassEntry extends SymbolTableEntry{

    public ClassEntry(String name, SymbolTable scope) {
        super(name, Kind.Class, new NoneType(), scope);
    }
}
