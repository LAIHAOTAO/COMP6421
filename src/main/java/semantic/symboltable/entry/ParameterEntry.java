package semantic.symboltable.entry;

import semantic.symboltable.SymbolTableEntry;
import semantic.symboltable.type.ClassType;
import semantic.symboltable.type.FloatType;
import semantic.symboltable.type.IntType;
import semantic.symboltable.type.SymbolTableEntryType;

/**
 * Created by ERIC_LAI on 2017-03-26.
 */
public class ParameterEntry extends SymbolTableEntry {

    public ParameterEntry(String name, SymbolTableEntryType type) {
        super(name, Kind.Parameter, type, null);
    }

    @Override
    protected int calculateSize() {
        // parameter only can be int | float | class (reference), so the size
        // should always 4
        return 4;
    }
}
