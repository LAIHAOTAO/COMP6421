package semantic;

import java.util.LinkedList;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class SymbolTableEntry {

    public enum Kind {
        Class,
        Function,
        Parameter,
        Variable
    }

    public enum Type {
        Null,
        Int,
        Float,
        Class
    }

    private String name;
    private Kind kind;
    private Type type;
    private LinkedList<Type> paramTypeList;
    private SymbolTable scope;

    public SymbolTableEntry(String name, Kind kind, Type type, SymbolTable scope) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.scope = scope;
        this.paramTypeList = new LinkedList<>();
    }

    @Override
    public String toString() {
        return "SymbolTableEntry{" +
                "name='" + name + '\'' +
                ", kind=" + kind +
                ", type=" + type +
                ", paramTypeList=" + paramTypeList +
                ", scope=" + scope +
                '}';
    }

    public String getName() {
        return name;
    }

    public Kind getKind() {
        return kind;
    }

    public Type getType() {
        return type;
    }

    public LinkedList<Type> getParamTypeList() {
        return paramTypeList;
    }

    public void addParameterType(Type paramType) {
        this.paramTypeList.add(paramType);
    }

    public SymbolTable getScope() {
        return scope;
    }

    public void setScope(SymbolTable scope) {
        this.scope = scope;
    }
}
