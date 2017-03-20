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
                createVariable(table, prevToken);
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
                System.out.println("something wrong here sym store dimension ...");
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
                functionTable.insert(cacheParamNameList.get(i), paramEntry);
            }
        }

        // clear the parameters list
        cacheParamNameList.clear();

        context.push(functionTable);
    }

    private static void addFunctionParameter(SymbolTable currentTable) {
        SymbolTableEntry entry = currentTable.search(cacheFunction);
        entry.addParameterType(getType(cacheTypeToken));
        // cache the parameter name
        cacheParamNameList.add(cacheIdToken.getValue());
    }

    private static void startFunction(SymbolTable currentTable) {
        if (!currentTable.exist(cacheIdToken.getValue())) {
            SymbolTableEntry entry = new SymbolTableEntry(
                    cacheIdToken.getValue(),
                    SymbolTableEntry.Kind.Function,
                    getType(cacheTypeToken),
                    null
            );
            currentTable.insert(cacheIdToken.getValue(), entry);

            // cache the function name for adding parameter
            cacheFunction = cacheIdToken.getValue();

        } else {
            throw new RuntimeException("Duplicate function declaration of function name " + cacheIdToken.getValue());
        }
    }

    private static void createVariable(SymbolTable currentTable, Token prevToken) {
        if (!currentTable.exist(cacheIdToken.getValue())) {
            SymbolTableEntry entry = new SymbolTableEntry(
                    cacheIdToken.getValue(),
                    SymbolTableEntry.Kind.Variable,
                    getType(cacheTypeToken),
                    null
            );
            currentTable.insert(cacheIdToken.getValue(), entry);
        } else {
            throw new RuntimeException("Duplicate variable declaration of variable name " + cacheIdToken.getValue());
        }
    }

    private static void createClassScope(SymbolTable currentTable, Token prevToken) {

        if (!currentTable.exist(prevToken.getValue())) {

            SymbolTable classScope = new SymbolTable(currentTable);
            classScope.setName(prevToken.getValue() + " table");
            SymbolTableEntry entry = new SymbolTableEntry(
                    prevToken.getValue(),
                    SymbolTableEntry.Kind.Class,
                    SymbolTableEntry.Type.Null,
                    classScope
            );

            currentTable.insert(prevToken.getValue(), entry);
            context.push(classScope);
        } else {
            // duplicate declaration
            throw new RuntimeException("Duplicate class declaration of class name " + prevToken.getValue()
                    + " around line: " + prevToken.getLocation());
        }
    }

    private static void createProgram(SymbolTable currentTable, Token prevToken) {

        if (!currentTable.exist("program")) {

            SymbolTable programScope = new SymbolTable(currentTable);
            programScope.setName("program table");
            SymbolTableEntry entry = new SymbolTableEntry(
                    "program",
                    SymbolTableEntry.Kind.Function,
                    SymbolTableEntry.Type.Null,
                    programScope
            );

            currentTable.insert("program", entry);
            context.push(programScope);
        } else {
            // duplicate declaration
            throw new RuntimeException("Duplicate program declaration around line: " + prevToken.getLocation());
        }
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
