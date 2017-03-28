package semantic.handler;

import lexical.Token;
import semantic.Statement.AssignmentStatement;
import semantic.expression.AdditionExpressionFragment;
import semantic.expression.ExpressionContext;
import semantic.expression.MultiplicationExpressionFragment;
import semantic.expression.RelationExpressionFragment;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class SemanticActionHandler extends ActionHandler {

    private static ExpressionContext exprContext = ExpressionContext.instance;

    public static void process(String action, Token token) {

        switch (action) {

            case "sem_FinishVariable":
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
                exprContext.getCurrent().pushID(token.getValue());
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
            case "sem_EndFunctionCall":

            case "sem_StartReturnStatement":

            case "sem_StartGetStatement":
            case "sem_StartPutStatement":

            default:
                break;
        }
    }

}
