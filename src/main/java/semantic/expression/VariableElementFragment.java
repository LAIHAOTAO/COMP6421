package semantic.expression;

import codegenerate.MathOpt;
import codegenerate.Register;
import common.Const;
import exception.CompilerException;
import semantic.handler.ActionHandler;
import semantic.handler.SemanticActionHandler;
import semantic.handler.SymbolTableActionHandler;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.entry.ParameterEntry;
import semantic.symboltable.entry.SymbolTableEntry;
import semantic.symboltable.entry.VariableEntry;
import semantic.symboltable.type.*;
import semantic.value.*;
import sun.tools.java.CompilerError;

/**
 * Created by ERIC_LAI on 2017-03-25.
 */
public class VariableElementFragment extends TypedExpressionElement {

    private String name;

    private SymbolTable currentScope;
    private SymbolTable enclosingScope;

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
        this.enclosingScope = curScope;
        this.currentType = null;
        this.functionCall = false;

        // get the entry with that id as name
        final SymbolTableEntry e = getEntry(id);

        if (!curScope.getParentName().equals("global table")) {
            // means we are in a member function
            SymbolTable outerClass = curScope.getParent();
            if (outerClass.exist(id) && !curScope.exist(id)) {
                // member function call
                SymbolTable global = SymbolTableActionHandler.getSymbolTableByName("global");
                // get outer class entry
                SymbolTableEntry entry = global.search(outerClass.getName());
                init(entry);
                pushID(id);
            } else {
                init(e);
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
        } else if (functionCall) {
            // todo
            return null;
        } else if (isReference) {
            return baseAddr;
        } else return new IndirectValue(new MathValue(MathOpt.ADD, baseAddr, offset));
    }

    @Override
    public void pushID(String varName) {
        // if access a member variable the program will come here
        // get the class structure and calculate the relative offset

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
                IndexingExpressionFragment child
                        = new IndexingExpressionFragment((ArrayType) currentType);
                context.push(child);
                child.accept(expr);
            } else {
                throw new CompilerError("Cannot index non-array type " + currentType);
            }

        } else if (expr instanceof FunctionCallExpressFragment) {
            // todo: member function call

        } else if (expr instanceof IndexingExpressionFragment) {

            IndexingExpressionFragment e = (IndexingExpressionFragment) expr;
            offset = new MathValue(MathOpt.ADD, offset, e.getValue());
            currentType = ((ArrayType) currentType).getArrayTypeType();
            currentScope = currentType.getScope();

        } else {
            super.accept(expr);
        }
    }

    private void init(final SymbolTableEntry entry) {

        Value offsetValue = new LateBindingDynamicValue() {
            @Override
            protected Value get() {
                return new MathValue(
                        MathOpt.SUBTRACT,
                        new StaticNumValue(entry.getOffset()),
                        new StaticNumValue(enclosingScope.getSize())
                );
            }
        };

        // if the entry is a variable
        if (entry instanceof VariableEntry ||
                entry.getType() instanceof IntType ||
                entry.getType() instanceof FloatType) {

            this.isReference = false;

            // if entry is not parameter
            if (!(entry instanceof ParameterEntry)) {
                this.baseAddr = new RegisterValue(Register.FRAME_POINTER);
                this.offset = offsetValue;
            }
            // if entry is parameter but primitive type
            else {
                this.baseAddr = new RegisterValue(Register.FRAME_POINTER);
                this.offset = new StaticNumValue(entry.getOffset() + Const.EXTRA_SPACE);
            }

        }

        // if the entry is a parameter, and it is class type, we need to pass it by reference
        else if (entry instanceof ParameterEntry) {

            this.isReference = true;

            // calculate the offset of this parameter, tricky part, since we use the content
            // which is
            // the address of its real address !!!
            this.baseAddr = new AbsoluteAddressValue(
                    new RegisterValue(Register.FRAME_POINTER),
                    new StaticNumValue(entry.getOffset() + Const.EXTRA_SPACE)
            );

            this.offset = new StaticNumValue(0);

        }

        // the above conditions can not be satisfied then throw an error
        else {
            throw new CompilerException("Something wrong with that entry in " +
                    "VariableElementFragment");
        }
        currentType = entry.getType();
        currentScope = currentType.getScope();
    }

    /**
     * Get the entry with the specific id
     */
    private SymbolTableEntry getEntry(String id) {

        if (currentScope == null) {
            throw new CompilerException("Cannot get entry of name " + id);
        }

        SymbolTableEntry entry;

        if (!"global table".equals(currentScope.getParentName())) {
            // if current scope's parent is global table means we are in a
            // member function, search the fields of that class
            entry = currentScope.memberSearch(id);
        } else {
            // if we are in a free function
            entry = currentScope.search(id);
        }

        // otherwise this id maybe a free function name (if it is a member function name, the
        // class name should
        // be met first)
        if (entry == null) {
            throw new CompilerException("Cannot find entry with " + id + " in current scope " +
                    currentScope.getName());
        }

        return entry;
    }

    @Override
    public SymbolTableEntryType getType() {
        return currentType;
    }
}
