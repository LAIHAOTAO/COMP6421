package semantic.statement;

import java.util.Stack;

/**
 * Created by ERIC_LAI on 2017-03-23.
 */
public class ExpressionContext {

    public static ExpressionContext instance = new ExpressionContext();

    private final Stack<ExpressionElement> stack;

    public ExpressionContext() {
        stack = new Stack<>();
    }

    public void finishTopElement() {
        ExpressionElement child;

        if (stack.isEmpty()) {
            // if the stack is empty it must be an error
            throw new RuntimeException("ExpressionContext, tried to pop from an empty stack");
        } else {
            // if not empty, since we finish that element, just pop it out
            child = stack.pop();
        }

        // maybe the element we pop out from the stack is part of the following element,
        // we need to handle this situation, pass that element to the following element in the stack

        if (stack.isEmpty()) {
            if (false) {
                // todo
            } else {
                // this is not an error, more a warning throw new InternalCompilerError("Emptied stack with non-"
                // + Statement.class.getSimpleName() + " element: " + child);
            }
        } else {
            stack.peek().acceptSubElement(child);
        }

    }

    public void push(ExpressionElement e) {
        this.stack.push(e);
    }

}
