package semantic.symboltable.entry;

import common.Const;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.type.SymbolTableEntryType;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public abstract class SymbolTableEntry {

    public enum Kind {
        Class,
        Function,
        Parameter,
        Variable
    }

    private String name;
    private Kind kind;
    private SymbolTableEntryType type;
    private SymbolTable scope;
    private int offset;


    public SymbolTableEntry(String name, Kind kind, SymbolTableEntryType type, SymbolTable scope) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.scope = scope;
        this.offset = Const.OFFSET_INIT;
    }

    /**
     * Force all subclass to provide the size interface
     * @return
     */
    protected abstract int calculateSize();

    public String getName() {
        return name;
    }

    public Kind getKind() {
        return kind;
    }

    public SymbolTableEntryType getType() {
        return type;
    }

    public SymbolTable getScope() {
        return scope;
    }

    public void setScope(SymbolTable scope) {
        this.scope = scope;
    }

    public void setType(SymbolTableEntryType type) {
        this.type = type;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getSize() {
        return calculateSize();
    }


}
