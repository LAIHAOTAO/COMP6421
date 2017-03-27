package semantic.expression;

import semantic.ActionHandler;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.type.SymbolTableEntryType;
import semantic.value.Value;
import semantic.value.VoidValue;

/**
 * Created by ERIC_LAI on 2017-03-25.
 */
public class VariableElementFragment extends ExpressionElement {

    private String name;

    private SymbolTable currentScope;
    private SymbolTableEntryType currentType;

    private boolean isReference;
    private boolean functionCall;

    public VariableElementFragment(String id) {
        this(id, ActionHandler.getCurrentSymbolTable());
    }

    public VariableElementFragment(String id, SymbolTable curScope) {
        this.name = id;
        this.currentScope = curScope;
        this.currentType = null;
        this.functionCall = false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Value getValue() {
        return VoidValue.get();
    }

    @Override
    public void pushID(String varName) {

    }

    @Override
    public void accept(ExpressionElement expr) {

    }
}
