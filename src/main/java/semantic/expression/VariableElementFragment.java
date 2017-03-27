package semantic.expression;

import semantic.ActionHandler;
import semantic.symboltable.SymbolTable;

/**
 * Created by ERIC_LAI on 2017-03-25.
 */
public class VariableElementFragment extends ExpressionElement {

    SymbolTable currentScope;

    public VariableElementFragment(String id) {
        this(id, ActionHandler.getCurrentSymbolTable());
    }

    public VariableElementFragment(String id, SymbolTable curScope) {
        this.currentScope = curScope;

    }

    @Override
    public void pushID(String varName) {

    }

    @Override
    public void accept(ExpressionElement expr) {

    }
}
