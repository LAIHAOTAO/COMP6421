package semantic.handler;

import lexical.Token;
import semantic.Statement.AssignmentStatement;
import semantic.Statement.ReturnStatement;
import semantic.expression.*;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.entry.FunctionEntry;
import semantic.symboltable.entry.MemberFunctionEntry;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class SemanticActionHandler extends ActionHandler {

    private static ExpressionContext exprContext = ExpressionContext.instance;

    private static boolean skipFinishVariable = false;
    private static boolean skipFunctionCall = false;


    public static void process(String action, Token token) {

        System.out.println(action + ": " + token.getValue());
        System.out.println(symContext.peek().getName());
        System.out.println();

        switch (action) {

            case "sem_FinishVariable":
                if (!skipFinishVariable) {
                    exprContext.finish();
                } else {
                    skipFinishVariable = false;
                }
                break;

            case "sem_EndFunctionCall":
                skipFunctionCall = false;
                exprContext.finish();
                break;

            case "sem_EndRelationExpression":
            case "sem_EndAdditionExpression":
            case "sem_EndMultiplicationExpression":
                exprContext.finish();
                break;

            // ***************************************************************************************
            case "sem_StartAssignmentStatment":
                exprContext.push(new AssignmentStatement());
                break;
            case "sem_StartRelationExpression":
                exprContext.push(new RelationExpressionFragment());
                break;
            case "sem_StartAdditionExpression":
                exprContext.push(new AdditionExpressionFragment());
                break;
            case "sem_StartMultiplicationExpression":
                exprContext.push(new MultiplicationExpressionFragment());
                break;

            // ***************************************************************************************
            case "sem_PushVariableName":
                String id = token.getValue();

                if (!isFunctionCall(id)) {
                    exprContext.getCurrent().pushID(token.getValue());
                } else {
                    // if that variable is a function name, actually it is a
                    // function call happen
                    exprContext.push(new FunctionCallExpressFragment(id, symContext.peek()));
                    skipFinishVariable = true;
                    skipFunctionCall = true;
                }
                break;

            case "sem_PushIntLiteral":
                exprContext.getCurrent().pushIntNum(Integer.parseInt(token.getValue()));
                break;
            case "sem_PushFloatLiteral":
                // todo
                break;

            // ***************************************************************************************
            case "sem_PushRelationOperation":
                exprContext.getCurrent().pushRelationOp(token.getValue());
                break;
            case "sem_PushAdditionOperation":
                exprContext.getCurrent().pushAdditionOp(token.getValue());
                break;
            case "sem_PushMultiplicationOperation":
                exprContext.getCurrent().pushMultiplicationOp(token.getValue());
                break;

            // ***************************************************************************************
            case "sem_StartIfStatement":

            case "sem_StartForStatement":

            case "sem_StartBlock":
            case "sem_EndBlock":

            case "sem_StartFunctionCall":
                if (skipFunctionCall) break;
                else exprContext.push(new FunctionCallExpressFragment(token.getValue(), symContext.peek()));
//                System.out.println("call function: " + token.getValue());
                break;

            case "sem_StartReturnStatement":
                exprContext.push(new ReturnStatement());
                break;
            case "sem_StartGetStatement":
            case "sem_StartPutStatement":

            default:
                break;
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

}
