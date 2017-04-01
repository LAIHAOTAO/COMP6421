package semantic.expression;

import codegenerate.MathOpt;
import codegenerate.Register;
import semantic.handler.ActionHandler;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.entry.ParameterEntry;
import semantic.symboltable.entry.SymbolTableEntry;
import semantic.symboltable.entry.VariableEntry;
import semantic.symboltable.type.ArrayType;
import semantic.symboltable.type.FloatType;
import semantic.symboltable.type.IntType;
import semantic.symboltable.type.SymbolTableEntryType;
import semantic.value.*;

/**
 * Created by ERIC_LAI on 2017-03-25.
 */
public class VariableElementFragment extends TypedExpressionElement {

    private String name;

    private SymbolTable currentScope;
    private SymbolTableEntryType currentType;

    private boolean isReference;
    private boolean functionCall;

    private Value baseAddr;
    private Value offset;

    public VariableElementFragment(String id) {
        this(id, ActionHandler.getCurrentSymbolTable());
    }

    public VariableElementFragment(String id, SymbolTable curScope) {
        this.name = id;
        this.currentScope = curScope;
        this.currentType = null;
        this.functionCall = false;

        // get the entry with that id as name
        final SymbolTableEntry e = getEntry(id);

        if (!curScope.getParent().getName().equals("global table")) {
            // means we are in a member function
            SymbolTable outerClass = curScope.getParent();
            if (outerClass.exist(id) && !curScope.exist(id)) {
//                init(getEntry());
//                pushID(id);
            }

        } else {
            // otherwise we are inside a free function or class
            init(e);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Value getValue() {
        if (currentType instanceof IntType || currentType instanceof FloatType) {
            return new StoredValue(baseAddr, offset);
        }
        if (functionCall) {
            // todo
            return null;
        } else {
            if (isReference) return baseAddr;
            else return new IndirectValue(new MathValue(MathOpt.ADD, baseAddr, offset));
        }
    }

    @Override
    public void pushID(String varName) {
        SymbolTableEntry e = getEntry(varName);

        currentType = e.getType();
        currentScope = currentType.getScope();
        offset = new MathValue(MathOpt.ADD, offset, new StaticNumValue(e.getOffset()));

        isReference = false;
    }

    @Override
    public void accept(ExpressionElement expr) {
        if (expr instanceof AdditionExpressionFragment) {
            if (currentType instanceof ArrayType) {

            }
        } else if (expr instanceof FunctionCallExpressFragment) {

        } else if (expr instanceof IndexingExpressionFragment) {

        }
    }

    private void init(final SymbolTableEntry entry) {

        Value offsetValue = new LateBindingDynamicValue() {
            @Override
            protected Value get() {
                return new MathValue(
                        // since we use the memory from the highest address
                        // so we need to do subtraction here
                        MathOpt.SUBTRACT,
                        new StaticNumValue(entry.getOffset()),
                        new StaticNumValue(currentScope.getSize())
                );
            }
        };

        if (entry instanceof VariableEntry ||
                entry.getType() instanceof IntType ||
                entry.getType() instanceof FloatType) {

            this.isReference = false;
            this.baseAddr = new RegisterValue(Register.STACK_POINTER);
            this.offset = offsetValue;

        } else if (entry instanceof ParameterEntry) {

        } else {
            throw new RuntimeException("Something wrong with that entry in VariableElementFragment");
        }

        currentType = entry.getType();
        currentScope = currentType.getScope();
    }

    /**
     * Get the entry with the specific id
     */
    private SymbolTableEntry getEntry(String id) {

        if (currentScope == null) {
            throw new RuntimeException("Cannot get entry of name " + id);
        }

        SymbolTableEntry e;

        if (!"global table".equals(currentScope.getParentName())) {
            // if we are in a member function, search the fields of that class
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

    @Override
    public SymbolTableEntryType getType() {
        return currentType;
    }
}
