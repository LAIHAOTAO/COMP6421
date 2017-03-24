package semantic;

import lexical.Token;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class SemanticActionHandler {


    public static void process(String action, Token prevToken) {

        switch (action) {
            case "sem_PushVariableName":
            case "sem_FinishVariable":

            case "sem_StartAssignmentStatment":

            case "sem_StartRelationExpression":
            case "sem_EndRelationExpression":
            case "sem_PushRelationOperation":

            case "sem_StartAdditionExpression":
            case "sem_EndAdditionExpression":

            case "sem_StartMultiplicationExpression":
            case "sem_EndMultiplicationExpression":

            case "sem_PushIntLiteral":
            case "sem_PushFloatLiteral":

            case "sem_PushAdditionOperation":
            case "sem_PushMultiplicationOperation":

            case "sem_StartIfStatement":

            case "sem_StartForStatement":

            case "sem_StartBlock":
            case "sem_EndBlock":

            case "sem_StartFunctionCall":
            case "sem_EndFunctionCall":

            case "sem_StartReturnStatement":

            case "sem_StartGetStatement":
            case "sem_StartPutStatement":
        }
    }

}
