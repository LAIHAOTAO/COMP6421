package semantic.symboltable.type;

import semantic.symboltable.SymbolTable;
import semantic.symboltable.entry.ClassEntry;

import java.util.Objects;

/**
 * Created by ERIC_LAI on 2017-03-26.
 */
public class ClassType implements SymbolTableEntryType {

    private ClassEntry entry;

    public ClassType(ClassEntry entry) {
        this.entry = entry;
    }

    @Override
    public int getSize() {
        return entry.getSize();
    }

    @Override
    public SymbolTable getScope() {
        return this.entry.getScope();
    }

    @Override
    public String toString() {
        return entry.getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClassType) {
            ClassType other = (ClassType) obj;
            return Objects.equals(entry.getName(), other.getName());
        }
        return false;
    }

    public String getName() {
        return entry.getName();
    }

    public void setEntry(ClassEntry entry) {
        this.entry = entry;
    }

    public ClassEntry getEntry() {
        return entry;
    }
}
