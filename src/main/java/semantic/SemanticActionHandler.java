package semantic;

import lexical.Token;
import semantic.Statement.AssignmentStatement;
import semantic.expression.ExpressionContext;
import semantic.expression.ExpressionElement;
import semantic.symboltable.SymbolTable;

import java.util.Stack;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class SemanticActionHandler extends ActionHandler {

    private static ExpressionContext exprContext = ExpressionContext.instance;

    public static void process(String action, Token token) {
        switch (action) {
            case "sem_PushVariableName":
                exprContext.getCurrent().pushID(token.getValue());
                break;
            case "sem_FinishVariable":
                exprContext.finish();
                break;

            case "sem_StartAssignmentStatment":
                exprContext.push(new AssignmentStatement());
                break;

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
//
//    private static void pushVariableName(SymbolTable currentTable, Token prevToken) {
//
//        String varName = prevToken.getValue();
//        // if the current table's parent is the global table means we are in
//        // program or free function (the function defined under program function)
//        if ("program table".equals(currentTable.getName()) ||
//                "global table".equals(currentTable.getParentName())) {
//            if (currentTable.exist(varName)) {
//                statementStack.peek().pushID(varName);
//            } else {
//                throw new RuntimeException("Variable " + varName + " undefined, at line: " + prevToken.getLocation());
//            }
//        }
//        // otherwise we are in the member function, need to take care of the member variable
//        else {
//            // search local and its parent scope
//            if (currentTable.exist(varName) || currentTable.getParent().exist(varName)) {
//                statementStack.peek().pushID(varName);
//            }
//            else {
//                throw new RuntimeException("Variable " + varName + " undefined, at line: " + prevToken.getLocation());
//            }
//        }
//
//    }

}
