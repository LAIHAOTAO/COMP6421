package semantic.expression;

import semantic.handler.ActionHandler;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.SymbolTableEntry;
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

        final SymbolTableEntry e = getEntry(id);

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

    private SymbolTableEntry getEntry(String id) {

        if (currentScope == null) {
            throw new RuntimeException("Cannot get entry of name " + id);
        }

        SymbolTableEntry e;

        if (!"global table".equals(currentScope.getParentName())) {
            // if we are in a member function
            e = currentScope.memberSearch(id);
        } else {
            // if we are in a free function
            e = currentScope.search(id);
        }

        if (e == null) {
            throw new RuntimeException("Cannot find entry with " + id + "in current scope " + currentScope.getName());
        }

        return e;
    }
}
