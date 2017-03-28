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

    @Override
    protected int calculateSize() {
        int size = 0;
        SymbolTable scope = getScope();

        // the size of the class is the total size of all its
        // member variable, just forget about the member function
        for (SymbolTableEntry e : scope.valueSet()) {
            if (e instanceof VariableEntry) {
                size += e.getSize();
            }
        }
        return size;
    }

}
