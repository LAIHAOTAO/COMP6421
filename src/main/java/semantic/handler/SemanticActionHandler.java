package semantic.handler;

import exception.CompilerException;
import lexical.Token;
import semantic.statement.*;
import semantic.expression.*;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.entry.FunctionEntry;
import semantic.symboltable.entry.MemberFunctionEntry;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class SemanticActionHandler extends ActionHandler {

    private static boolean DEBUG = false;

    private static ExpressionContext exprContext = ExpressionContext.instance;
    private static boolean skipFinishVariable = false;
    private static boolean skipFunctionCall = false;

    public static void process(String action, Token token) {

        ExpressionElement top;

        switch (action) {
            case "sem_FinishVariable":
                if (!skipFinishVariable) {
                    debugInfo(action, token);
                    exprContext.finish();
                } else {
                    skipFinishVariable = false;
                }
                break;

            case "sem_EndFunctionCall":
                debugInfo(action, token);
                skipFunctionCall = false;
                exprContext.finish();
                break;

            case "sem_EndRelationExpression":
                top = exprContext.getCurrent();
                if (top instanceof RelationExpressionFragment) {
                    debugInfo(action, token);
                    exprContext.finish();
                } else if (top instanceof ForStatementElement) {
                    // do nothing so far, it should not be an error, since the relation finish
                    // by itself when the state reach to DONE
                    debugInfo(action, token);
                    break;
                } else {
                    throw new CompilerException("Expected element RelationExpression " +
                            "element but found " + top.getClass());
                }
                top = null;
                break;

            case "sem_EndAdditionExpression":
                top = exprContext.getCurrent();
                if (top instanceof AdditionExpressionFragment) {
                    debugInfo(action, token);
                    exprContext.finish();
                } else {
                    throw new CompilerException("Expected AdditionExpression element but found "
                            + top.getClass());
                }
                top = null;
                break;

            case "sem_EndMultiplicationExpression":
                top = exprContext.getCurrent();
                if (top instanceof MultiplicationExpressionFragment) {
                    debugInfo(action, token);
                    exprContext.finish();
                } else {
                    throw new CompilerException("Expected MultiplicationExpression " +
                            "element but found " + top.getClass());
                }
                top = null;
                break;

            // ************************************************************************************
            case "sem_StartAssignmentStatment":
                debugInfo(action, token);
                exprContext.push(new AssignmentStatement());
                break;
            case "sem_StartRelationExpression":
                debugInfo(action, token);
                exprContext.push(new RelationExpressionFragment());
                break;
            case "sem_StartAdditionExpression":
                debugInfo(action, token);
                exprContext.push(new AdditionExpressionFragment());
                break;
            case "sem_StartMultiplicationExpression":
                debugInfo(action, token);
                exprContext.push(new MultiplicationExpressionFragment());
                break;

            // ************************************************************************************
            case "sem_PushVariableName":
                String id = token.getValue();

                if (!isFunctionCall(id)) {
                    debugInfo(action, token);
                    exprContext.getCurrent().pushID(token.getValue());
                } else {
                    // if that variable is a function name, actually it is a
                    // function call happen
                    debugInfo("sem_StartFunctionCall", token);
                    exprContext.push(new FunctionCallExpressFragment(id, symContext.peek()));
                    skipFinishVariable = true;
                    skipFunctionCall = true;
                }
                break;

            case "sem_PushIntLiteral":
                debugInfo(action, token);
                exprContext.getCurrent().pushIntNum(Integer.parseInt(token.getValue()));
                break;
            case "sem_PushFloatLiteral":
                // todo floating point number stuff ...
                break;

            // ************************************************************************************
            case "sem_PushRelationOperation":
                debugInfo(action, token);
                exprContext.getCurrent().pushRelationOp(token.getValue());
                break;
            case "sem_PushAdditionOperation":
                debugInfo(action, token);
                exprContext.getCurrent().pushAdditionOp(token.getValue());
                break;
            case "sem_PushMultiplicationOperation":
                debugInfo(action, token);
                exprContext.getCurrent().pushMultiplicationOp(token.getValue());
                break;

            // ************************************************************************************
            case "sem_StartIfStatement":
                debugInfo(action, token);
                exprContext.push(new IfStatement());
                break;

            case "sem_StartForStatement":
                debugInfo(action, token);
                exprContext.push(new ForStatementElement());
                break;

            case "sem_StartBlock":
                debugInfo(action, token);
                exprContext.push(new StatementBlockElement());
                break;

            case "sem_EndBlock":
                debugInfo(action, token);
                top = exprContext.getCurrent();
                if (top instanceof StatementBlockElement) {
                    exprContext.finish();
                } else {
                    throw new CompilerException("Expected StatementBlockElement but it was "
                            + top.getClass());
                }
                top = null;
                break;

            // ************************************************************************************
            case "sem_StartFunctionCall":
                if (skipFunctionCall) break;
                else {
                    debugInfo(action, token);
                    exprContext.push(
                            new FunctionCallExpressFragment(token.getValue(), symContext.peek())
                    );
                }
                break;

            // ************************************************************************************
            case "sem_StartReturnStatement":
                debugInfo(action, token);
                exprContext.push(new ReturnStatement());
                break;

            case "sem_StartGetStatement":
                debugInfo(action, token);
                exprContext.push(new GetStatement());
                break;

            case "sem_StartPutStatement":
                debugInfo(action, token);
                exprContext.push(new PutStatement());
                break;

            // ************************************************************************************
            default:
                break;
        }
    }

    private static void debugInfo(String action, Token token) {
        if (DEBUG) {
            System.out.println(action + ": " + token.getValue());
            System.out.println(symContext.peek().getName());
            System.out.println();
        }
    }

    private static boolean isFunctionCall(String id) {
        // if id is in the current table, it cannot be a function call
        if (symContext.peek().exist(id)) return false;

        SymbolTable outerScope = symContext.peek().getParent();

        if ("global table".equals(outerScope.getName())) {
            // inside the free function
            return outerScope.exist(id) && outerScope.search(id) instanceof FunctionEntry;
        } else {
            // inside the member function
            return outerScope.exist(id) && outerScope.search(id) instanceof MemberFunctionEntry;
        }
    }

    public static void turnOnDebug() {
        DEBUG = true;
    }

}
