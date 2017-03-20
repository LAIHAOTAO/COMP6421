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
        Class,
        IntArray,
        FloatArray,
        ClassArray
    }

    private String name;
    private Kind kind;
    private Type type;
    private SymbolTable scope;
    private int dimension;
    private LinkedList<Type> paramTypeList;
    private LinkedList<Integer> paramDimensionList;

    public SymbolTableEntry(String name, Kind kind, Type type, SymbolTable scope) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.scope = scope;
        this.paramTypeList = new LinkedList<>();
        this.paramDimensionList = new LinkedList<>();
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

    public LinkedList<Integer> getParamDimensionList() {
        return paramDimensionList;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void addParamDimensionList(int paramDimensionList) {
        this.paramDimensionList.add(paramDimensionList);
    }

    public void setType(Type type) {
        this.type = type;
    }
}
