package semantic.symboltable.type;

import semantic.symboltable.SymbolTable;
import semantic.symboltable.entry.ClassEntry;

import java.util.Objects;

/**
 * Created by ERIC_LAI on 2017-03-26.
 */
public class ClassType implements SymbolTableEntryType {

    private final String name;
    private ClassEntry entry;

    public ClassType(String name) {
        this.name = name;
    }

    @Override
    public int getSize() {
        return entry.getSize();
    }

    @Override
    public SymbolTable getScope() {
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClassType) {
            ClassType other = (ClassType) obj;
            return Objects.equals(this.name, other.getName());
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setEntry(ClassEntry entry) {
        this.entry = entry;
    }
}
