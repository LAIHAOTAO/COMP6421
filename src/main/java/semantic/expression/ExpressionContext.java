package semantic.expression;

import semantic.Statement.Statement;
import semantic.symboltable.entry.FunctionAbstractEntry;

import java.util.Stack;

/**
 * Created by ERIC_LAI on 2017-03-25.
 */
public class ExpressionContext {

    public static ExpressionContext instance = new ExpressionContext();
    private final Stack<ExpressionElement> exprStack;
    private FunctionAbstractEntry currentFunction;


    public ExpressionContext() {
        this.exprStack = new Stack<>();
    }

    public void push(ExpressionElement exprElement) {
        exprStack.push(exprElement);
    }

    public void finish() {

        ExpressionElement child;

        if (exprStack.isEmpty()) {
            throw new RuntimeException("Try to pop from empty stack");
        } else {
            child = exprStack.pop();
        }

        if (exprStack.isEmpty()) {
            // if the stack is empty
            if (child instanceof Statement && currentFunction != null) {
                // if child is a statement and now we are inside a function
                // then just add the child variable to the current function
                currentFunction.addStatement((Statement) child);
            } else {
                throw new RuntimeException("Something wrong here ...");
            }
        } else {
            // if the stack is not empty, do the "migration", passing
            // child variable to the top element in the stack
            exprStack.peek().accept(child);
        }
    }

    public ExpressionElement getCurrent() {
        if (exprStack.isEmpty()) return null;
        return exprStack.peek();
    }


    public static void setCurrentFunction(FunctionAbstractEntry functionEntry) {
        instance.currentFunction = functionEntry;
    }
}
