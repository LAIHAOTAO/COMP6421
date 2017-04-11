package semantic.expression;

import codegenerate.MathOpt;
import codegenerate.Register;
import common.Const;
import common.SpecialValues;
import exception.CompilerException;
import semantic.handler.ActionHandler;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.entry.MemberFunctionEntry;
import semantic.symboltable.entry.ParameterEntry;
import semantic.symboltable.entry.SymbolTableEntry;
import semantic.symboltable.entry.VariableEntry;
import semantic.symboltable.type.*;
import semantic.value.*;

import java.util.List;

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
    private FunctionCallValue memberFunctionCallValue;

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
                init(getEntry(SpecialValues.THIS_POINTER_NAME));
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
                throw new CompilerException("Cannot index non-array type " + currentType);
            }

        } else if (expr instanceof FunctionCallExpressionFragment) {
            FunctionCallExpressionFragment f = (FunctionCallExpressionFragment) expr;
            // then this is a member function call
            if (currentType instanceof ClassType) {
                ClassType currentClass = (ClassType) currentType;

                // We don't want to do a recursive search for it, but rather check that it exists in the current scope
                if (currentScope.exist(f.getId())) {
                    SymbolTableEntry entry = currentScope.search(f.getId());
                    if (entry instanceof MemberFunctionEntry) {
                        MemberFunctionEntry function = (MemberFunctionEntry) entry;

                        List<TypedExpressionElement> expressions = f.getExpressions();

                        expressions.add(0, this);

                        memberFunctionCallValue = new FunctionCallValue(function, expressions);

                        functionCall = true;
                        currentType = function.getType();
                        context.finish();
                    } else {
                        throw new CompilerException("Cannot call non-function member " + f.getId() + " of class " + currentClass);
                    }
                } else {
                    throw new CompilerException("Cannot find method " + f.getId() + " of class " + currentClass);
                }
            } else {
                throw new CompilerException("Cannot call method " + f.getId() + " of non-class type " + currentType);
            }

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

        if (entry == null) {
            throw new CompilerException("Undefined error. Cannot find entry: " + id + ", in current scope: " +
                    currentScope.getName());
        }

        return entry;
    }

    @Override
    public SymbolTableEntryType getType() {
        return currentType;
    }
}
