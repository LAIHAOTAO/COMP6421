package semantic.symboltable.entry;

import semantic.symboltable.SymbolTableEntry;
import semantic.symboltable.type.SymbolTableEntryType;

/**
 * Created by ERIC_LAI on 2017-03-26.
 */
public class ParameterEntry extends SymbolTableEntry {

    public ParameterEntry(String name, SymbolTableEntryType type) {
        super(name, Kind.Parameter, type, null);
    }
}
