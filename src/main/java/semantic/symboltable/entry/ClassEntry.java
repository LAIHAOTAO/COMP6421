package semantic.symboltable.entry;

import semantic.symboltable.SymbolTable;
import semantic.symboltable.type.ClassType;
import semantic.symboltable.type.NoneType;
import semantic.symboltable.type.SymbolTableEntryType;

/**
 * Created by ERIC_LAI on 2017-03-25.
 */
public class ClassEntry extends SymbolTableEntry{

    public ClassEntry(String name, SymbolTable scope) {
        super(name, Kind.Class, null, scope);
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

    @Override
    public SymbolTableEntryType getType() {
        return new ClassType(this);
    }
}
