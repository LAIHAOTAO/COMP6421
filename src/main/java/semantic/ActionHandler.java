package semantic;

import lexical.Token;
import semantic.expression.ExpressionContext;
import semantic.symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class ActionHandler {

    public static Stack<SymbolTable> symContext = new Stack<>();
    public static boolean isSuccess = true;

    protected static Token cacheTypeToken;
    protected static Token cacheIdToken;
    protected static String cacheFunction;
    protected static ArrayList<Token> cacheDimensionList = new ArrayList<>(5);
    protected static ArrayList<String> cacheParamList = new ArrayList<>(5);


    public static SymbolTable getCurrentSymbolTable() {
        if (!symContext.isEmpty()) {
            return symContext.peek();
        }
        return null;
    }
}
