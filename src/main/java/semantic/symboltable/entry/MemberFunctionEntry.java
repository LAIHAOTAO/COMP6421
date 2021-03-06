package semantic.symboltable.entry;

import semantic.symboltable.SymbolTable;
import semantic.symboltable.type.SymbolTableEntryType;

/**
 * Created by ERIC_LAI on 2017-03-25.
 */
public class MemberFunctionEntry extends FunctionAbstractEntry {

    public MemberFunctionEntry(String name, SymbolTableEntryType type, SymbolTable scope) {
        super(name, Kind.Function, type, scope);
    }

}
