package semantic.symboltable.type;

import semantic.symboltable.SymbolTable;

/**
 * Created by ERIC_LAI on 2017-03-26.
 */
public interface SymbolTableEntryType {

    /**
     * Get the amount of memory required to store a value of this type.
     *
     * @return the size as described above in bytes
     * @throws
     */
    int getSize();

    /**
     * for a class type return that class type's scope, for all other types,
     * returns `null`.
     * @return The scope for this type
     */
    SymbolTable getScope();

    String toString();
}
