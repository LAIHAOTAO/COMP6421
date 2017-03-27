package semantic.symboltable;


import lexical.Token;
import lexical.TokenType;
import semantic.ActionHandler;
import semantic.DuplicateDeclHandler;
import semantic.expression.ExpressionContext;
import semantic.symboltable.entry.*;
import semantic.symboltable.type.*;
import syntactic.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class SymbolTableActionHandler extends ActionHandler {

    public static List<SymbolTable> symbolTableList = new ArrayList<>();
    public static StringBuilder symActionErrorCollector = new StringBuilder();

    public static void process(String action, Token prevToken, int parseNum) {

        SymbolTable table = getCurrentSymbolTable();

        if (parseNum == Parser.FIRST_PARSE) {
            // if in the first parse, need to build the symbol table
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
                    startFunction(table, true);
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
                    cacheDimensionList.add(prevToken);
                    break;
                case "sym_EndScope":
                    // pop out the top symbol table of the semContext stack
                    if (!symContext.isEmpty()) {
                        symbolTableList.add(symContext.pop());
                    } else {
                        throw new RuntimeException("missing parentheses");
                    }
                    break;
//                default:
//                    throw new RuntimeException("No such symbol table action");
            }
        } else {
            // if in the second parse, need to maintain
            // the symContext for SemanticActionHandler
            switch (action) {
                case "sym_CreateProgram":
                    symContext.push(getSymbolTableByName(prevToken.getValue()));
                    ExpressionContext.setCurrentFunction(
                            (FunctionAbstractEntry) getSymbolTableByName("global table").search("program")
                    );
                    break;
                case "sym_CreateClassScope":
                    symContext.push(getSymbolTableByName(prevToken.getValue()));
                    break;
                case "sym_CreateFunction":
                    symContext.push(getSymbolTableByName(cacheFunction));
                    ExpressionContext.setCurrentFunction(
                            (FunctionAbstractEntry) getSymbolTableByName(cacheFunction).getParent().search(cacheFunction)
                    );
                    break;
                case "sym_EndScope":
                    // pop out the top symbol table of the semContext stack
                    if (!symContext.isEmpty()) {
                        symContext.pop();
                    } else {
                        throw new RuntimeException("missing parentheses");
                    }
                    break;
                case "sym_CreateVariable":
                case "sym_StartFunction":
                case "sym_StartMemberFunction":
                case "sym_AddFunctionParameter":
                case "sym_StoreType":
                case "sym_StoreId":
                case "sym_StoreDimension":
                    break;
//                default:
//                    throw new RuntimeException("Unknown error happen during second parse");
            }
        }
    }

    private static SymbolTable getSymbolTableByName(String name) {
        String tableNm = name + " table";
        if (!symbolTableList.isEmpty()) {
            for (SymbolTable table : symbolTableList) {
                if (tableNm.equals(table.getName())) {
                    return table;
                }
            }
        }
        throw new RuntimeException("The " + name + "table do not exit !!!");
    }

    private static void startMemberFunction(SymbolTable currentTable) {
        startFunction(currentTable, false);
    }

    private static void createFunction(SymbolTable currentTable) {
        // create a new function table for the new function
        SymbolTable functionTable = new SymbolTable(currentTable);
        functionTable.setName(cacheFunction + " table");
        // get the function entry from the current table
        FunctionAbstractEntry functionEntry = (FunctionAbstractEntry) currentTable.search(cacheFunction);
        functionEntry.setScope(functionTable);

        // add parameters to the function table
        List<SymbolTableEntryType> paramTypeList = functionEntry.getParamTypeList();
        if (!paramTypeList.isEmpty()) {
            for (int i = 0; i < paramTypeList.size(); i++) {
                String name = cacheParamList.get(i);
                SymbolTableEntryType type = paramTypeList.get(i);
                ParameterEntry paramEntry = new ParameterEntry(name, type);
                functionTable.insert(cacheParamList.get(i), paramEntry);
            }
        }

        // clear the parameters list
        cacheParamList.clear();

        symContext.push(functionTable);
    }

    private static void addFunctionParameter(SymbolTable currentTable) {
        // get the function entry
        FunctionAbstractEntry functionEntry = (FunctionAbstractEntry) currentTable.search(cacheFunction);
        // get the temporary type of the parameter
        SymbolTableEntryType type = getType(cacheTypeToken);
        ArrayType type1;
        if (!cacheDimensionList.isEmpty()) {
            // if the parameter is an array, cast its type to ArrayType
            type1 = changeTypeToArray(type);
            for (Token token : cacheDimensionList) {
                type1.addDimension(Integer.parseInt(token.getValue()));
            }
            functionEntry.addParamType(type1);
            // reset the cache
            cacheDimensionList.clear();
        } else {
            // if the parameter is not an array, just add it into the parameter list
            functionEntry.addParamType(type);
        }

        // cache the parameter name
        String paramName = cacheIdToken.getValue();
        cacheParamList.add(paramName);
    }

    private static void startFunction(SymbolTable currentTable, boolean freeFunction) {
        String name = cacheIdToken.getValue();

        if (currentTable.exist(name)) {
            // duplicate declaration
            symActionErrorCollector.append("Duplicate function declaration of function name ")
                    .append(name).append(" around line: ")
                    .append(cacheIdToken.getLocation()).append("\n");
            name = DuplicateDeclHandler.getNewName(currentTable, name);
            isSuccess = false;
        }

        SymbolTableEntry entry;
        if (freeFunction)
            entry = new FunctionEntry(name, getType(cacheTypeToken), null);
        else
            entry = new MemberFunctionEntry(name, getType(cacheTypeToken), null);

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
            name = DuplicateDeclHandler.getNewName(currentTable, name);
            isSuccess = false;
        }

        SymbolTableEntryType type = getType(cacheTypeToken);
        SymbolTableEntry entry;
        entry = new VariableEntry(name, type, null);
        entry.setType(type);

        // check this variable is an array or not
        if (!cacheDimensionList.isEmpty()) {
            ArrayType type1 = changeTypeToArray(type);
            for (Token token : cacheDimensionList) {
                type1.addDimension(Integer.parseInt(token.getValue()));
            }
            entry.setType(type1);
            // reset the cache
            cacheDimensionList.clear();
        }

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
            name = DuplicateDeclHandler.getNewName(currentTable, name);
            isSuccess = false;
        }

        SymbolTable classScope = new SymbolTable(currentTable);
        classScope.setName(name + " table");
        SymbolTableEntry entry = new ClassEntry(name, classScope);

        currentTable.insert(name, entry);
        symContext.push(classScope);

    }

    private static void createProgram(SymbolTable currentTable, Token prevToken) {

        String name = "program";

        if (currentTable.exist(name)) {
            // nothing to do since the grammar do not allow two program, it will be issued
            // as a syntax error, just keep the structure for extract method later
        }

        SymbolTable programScope = new SymbolTable(currentTable);
        programScope.setName(name + " table");
        SymbolTableEntry entry = new FunctionEntry(name, new NoneType(), programScope);

        currentTable.insert(name, entry);
        symContext.push(programScope);

    }

    private static ArrayType changeTypeToArray(SymbolTableEntryType type) {
        return new ArrayType(type);
    }

    private static SymbolTableEntryType getType(Token cacheType) {
        TokenType type = cacheType.getType();
        switch (type) {
            case INT:
                return new IntType();
            case FLOAT:
                return new FloatType();
            case ID:
                return new ClassType(cacheType.getValue());
        }
        throw new RuntimeException("No such type defined in the grammar");
    }

}
