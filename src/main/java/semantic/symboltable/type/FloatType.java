package semantic.symboltable.type;

import semantic.symboltable.SymbolTable;

import java.util.Objects;

/**
 * Created by ERIC_LAI on 2017-03-26.
 */
public class FloatType implements SymbolTableEntryType {

    @Override
    public int getSize() {
        return 4;
    }

    @Override
    public SymbolTable getScope() {
        return null;
    }

    @Override
    public String toString() {
        return "Float";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FloatType;
    }
}
