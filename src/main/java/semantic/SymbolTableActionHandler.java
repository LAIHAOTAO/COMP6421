package semantic;


import lexical.Token;
import lexical.TokenType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class SymbolTableActionHandler extends ActionHandler {

    private static Token cacheTypeToken;
    private static Token cacheIdToken;
    private static Token cacheDimension;
    private static String cacheFunction;
    private static ArrayList<String> cacheParamNameList = new ArrayList<>();
    public static List<SymbolTable> symbolTableList = new ArrayList<>();
    public static StringBuilder symActionErrorCollector = new StringBuilder();

    public static void process(String action, Token prevToken) {

        SymbolTable table = getCurrentSymbolTable();

        switch (action) {
            case "sym_CreateProgram":
                createProgram(table, prevToken);
                break;
            case "sym_CreateClassScope":
                createClassScope(table, prevToken);
                break;
            case "sym_CreateFunction":
                createFunction(table);
                break;
            case "sym_CreateVariable":
                createVariable(table);
                break;
            case "sym_StartFunction":
                startFunction(table);
                break;
            case "sym_StartMemberFunction":
                startMemberFunction(table);
                break;
            case "sym_AddFunctionParameter":
                addFunctionParameter(table);
                break;
            case "sym_StoreType":
                cacheTypeToken = prevToken;
                break;
            case "sym_StoreId":
                cacheIdToken = prevToken;
                break;
            case "sym_StoreDimension":
                cacheDimension = prevToken;
                break;
            case "sym_EndScope":
                // pop out the top symbol table of the context stack
                if (!context.isEmpty()) {
                    symbolTableList.add(context.pop());
                } else {
                    throw new RuntimeException("missing parentheses");
                }
                break;
            default:
                throw new RuntimeException("No such symbol table action");
        }
    }

    public static SymbolTable getGlobalTable() {
        if (!symbolTableList.isEmpty()) {
            for (SymbolTable table : symbolTableList) {
                if ("global table".equals(table.getName())) {
                    return table;
                }
            }
        }
        System.err.println("The global table do not exit !!!");
        return null;
    }

    private static void startMemberFunction(SymbolTable currentTable) {
        startFunction(currentTable);
    }

    private static void createFunction(SymbolTable currentTable) {
        SymbolTable functionTable = new SymbolTable(currentTable);
        functionTable.setName(cacheFunction + " table");
        SymbolTableEntry entry = currentTable.search(cacheFunction);
        entry.setScope(functionTable);

        // add parameters to the function table
        LinkedList<SymbolTableEntry.Type> paramTypeList = entry.getParamTypeList();
        if (paramTypeList.size() > 1) {
            for (int i = 0; i < paramTypeList.size(); i++) {
                String name = cacheParamNameList.get(i);
                SymbolTableEntry.Type type = paramTypeList.get(i);
                SymbolTableEntry.Kind kind = SymbolTableEntry.Kind.Parameter;
                SymbolTableEntry paramEntry = new SymbolTableEntry(name, kind, type, null);
                if (entry.getParamDimensionList().size() != 0) {
                    paramEntry.setDimension(entry.getParamDimensionList().get(i));
                }
                functionTable.insert(cacheParamNameList.get(i), paramEntry);
            }
        }

        // clear the parameters list
        cacheParamNameList.clear();

        context.push(functionTable);
    }

    private static void addFunctionParameter(SymbolTable currentTable) {
        SymbolTableEntry entry = currentTable.search(cacheFunction);
        SymbolTableEntry.Type type = getType(cacheTypeToken);

        // check this variable is an array or not
        if (cacheDimension != null) {
            entry.addParamDimensionList(Integer.parseInt(cacheDimension.getValue()));
            type = changeTypeToArray(type);
            // reset the cache
            cacheDimension = null;
        }
        entry.addParameterType(type);

        // cache the parameter name
        cacheParamNameList.add(cacheIdToken.getValue());
    }

    private static void startFunction(SymbolTable currentTable) {
        String name = cacheIdToken.getValue();

        if (currentTable.exist(name)) {
            // duplicate declaration
            symActionErrorCollector.append("Duplicate function declaration of function name ")
                    .append(name).append(" around line: ")
                    .append(cacheIdToken.getLocation()).append("\n");
            name = DuplicateHelper.getNewName(currentTable, name);
            isSuccess = false;
        }
        SymbolTableEntry entry = new SymbolTableEntry(
                name,
                SymbolTableEntry.Kind.Function,
                getType(cacheTypeToken),
                null
        );
        currentTable.insert(name, entry);

        // cache the function name for adding parameter
        cacheFunction = name;
    }

    private static void createVariable(SymbolTable currentTable) {
        String name = cacheIdToken.getValue();
        if (currentTable.exist(name)) {
            symActionErrorCollector.append("Duplicate variable declaration of variable name ")
                    .append(name)
                    .append(" around line: ")
                    .append(cacheIdToken.getLocation()).append("\n");
            name = DuplicateHelper.getNewName(currentTable, name);
            isSuccess = false;
        }
        SymbolTableEntry.Type type = getType(cacheTypeToken);

        SymbolTableEntry entry = new SymbolTableEntry(
                name,
                SymbolTableEntry.Kind.Variable,
                null,
                null
        );

        // check this variable is an array or not
        if (cacheDimension != null) {
            entry.setDimension(Integer.parseInt(cacheDimension.getValue()));
            type = changeTypeToArray(type);
            // reset the cache
            cacheDimension = null;
        }
        entry.setType(type);

        currentTable.insert(name, entry);
    }

    private static void createClassScope(SymbolTable currentTable, Token prevToken) {
        String name = prevToken.getValue();
        if (currentTable.exist(name)) {
            // duplicate declaration
            symActionErrorCollector.append("Duplicate class declaration of class name ")
                    .append(name)
                    .append(" around line: ")
                    .append(prevToken.getLocation()).append("\n");
            name = DuplicateHelper.getNewName(currentTable, name);
            isSuccess = false;
        }

        SymbolTable classScope = new SymbolTable(currentTable);
        classScope.setName(name + " table");
        SymbolTableEntry entry = new SymbolTableEntry(
                name,
                SymbolTableEntry.Kind.Class,
                SymbolTableEntry.Type.Null,
                classScope
        );

        currentTable.insert(name, entry);
        context.push(classScope);


    }

    private static void createProgram(SymbolTable currentTable, Token prevToken) {

        String name = "program";

        if (currentTable.exist(name)) {
            // nothing to do since the grammar do not allow two program, it will be issued
            // as a syntax error, just keep the structure for extract method later
        }

        SymbolTable programScope = new SymbolTable(currentTable);
        programScope.setName(name + " table");
        SymbolTableEntry entry = new SymbolTableEntry(
                name,
                SymbolTableEntry.Kind.Function,
                SymbolTableEntry.Type.Null,
                programScope
        );

        currentTable.insert(name, entry);
        context.push(programScope);

    }

    private static SymbolTableEntry.Type changeTypeToArray(SymbolTableEntry.Type type) {
        switch (type) {
            case Int:
                return SymbolTableEntry.Type.IntArray;
            case Float:
                return SymbolTableEntry.Type.FloatArray;
            case Class:
                return SymbolTableEntry.Type.ClassArray;
        }
        return null;
    }

    private static SymbolTableEntry.Type getType(Token cacheType) {
        TokenType type = cacheType.getType();
        switch (type) {
            case INT:
                return SymbolTableEntry.Type.Int;
            case FLOAT:
                return SymbolTableEntry.Type.Float;
            case ID:
                return SymbolTableEntry.Type.Class;
        }
        return null;
    }

}
