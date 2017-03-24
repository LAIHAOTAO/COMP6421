package semantic;

import lexical.Token;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class ActionHandler {

    public static Stack<SymbolTable> context = new Stack<>();
    public static boolean isSuccess = true;

    protected static Token cacheTypeToken;
    protected static Token cacheIdToken;
    protected static Token cacheDimension;
    protected static String cacheFunction;
    protected static ArrayList<String> cacheParamNameList = new ArrayList<>();


    protected static SymbolTable getCurrentSymbolTable() {
        if (!context.isEmpty()) {
            return context.peek();
        }
        return null;
    }
}
