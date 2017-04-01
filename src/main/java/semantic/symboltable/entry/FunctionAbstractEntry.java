package semantic.symboltable.entry;

import semantic.Statement.Statement;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.type.SymbolTableEntryType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ERIC_LAI on 2017-03-25.
 */
public class FunctionAbstractEntry extends SymbolTableEntry {

    private List<SymbolTableEntryType> paramTypeList;
    private List<Statement> statementList;

    public FunctionAbstractEntry(String name, Kind kind, SymbolTableEntryType type, SymbolTable scope) {
        super(name, kind, type, scope);
        paramTypeList = new ArrayList<>();
        statementList = new LinkedList<>();
    }

    @Override
    protected int calculateSize() {
        int size = 0;
        for (SymbolTableEntry e : getScope().valueSet()) {
            if (e instanceof VariableEntry || e instanceof ParameterEntry) {
                size += e.getSize();
            }
        }
        return size;
    }

    public void addParamType(SymbolTableEntryType type) {
        this.paramTypeList.add(type);
    }

    public List<SymbolTableEntryType> getParamTypeList() {
        return paramTypeList;
    }

    public void setParamTypeList(List<SymbolTableEntryType> paramTypeList) {
        this.paramTypeList = paramTypeList;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < paramTypeList.size(); i++) {
            str += paramTypeList.get(i).toString();
        }
        return str;
    }

    public void addStatement(Statement statement) {
        this.statementList.add(statement);
    }

    public List<Statement> getStatementList() {
        return statementList;
    }
}
