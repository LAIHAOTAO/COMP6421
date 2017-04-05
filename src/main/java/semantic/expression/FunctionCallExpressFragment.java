package semantic.expression;

import exception.CompilerException;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.entry.FunctionAbstractEntry;
import semantic.symboltable.entry.FunctionEntry;
import semantic.symboltable.entry.MemberFunctionEntry;
import semantic.symboltable.entry.SymbolTableEntry;
import semantic.symboltable.type.SymbolTableEntryType;
import semantic.value.FunctionCallValue;
import semantic.value.LateBindingDynamicValue;
import semantic.value.Value;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public class FunctionCallExpressFragment extends TypedExpressionElement {

    private String id;
    private List<TypedExpressionElement> expressions;
    private SymbolTable currentScope;

    public FunctionCallExpressFragment(String id, SymbolTable s) {
        this.id = id;
        this.currentScope = s;
        this.expressions = new ArrayList<>();
    }

    @Override
    public void accept(ExpressionElement expr) {
        // here should be the parameters, since each expression follow
        // the relation --> addition --> multiplication
        if (expr instanceof RelationExpressionFragment) {
            expressions.add((TypedExpressionElement) expr);
        } else {
            super.accept(expr);
        }
    }

    @Override
    public SymbolTableEntryType getType() {
        SymbolTableEntry entry = currentScope.getParent().search(id);
        if (entry instanceof FunctionAbstractEntry) {
            return entry.getType();
        } else {
            throw new CompilerException("Cannot find the function with name " + id);
        }
    }

    @Override
    public Value getValue() {
        return new LateBindingDynamicValue() {
            @Override
            protected Value get() {
                SymbolTableEntry entry = currentScope.getParent().search(id);
                SymbolTable outerScope = currentScope.getParent();
                if (entry instanceof MemberFunctionEntry) {
                    // entry is member function
                    if (outerScope.exist(id)) {
                        // todo need to do something before a function call
//                        expressions.addFirst(new VariableElementFragment());
                        return new FunctionCallValue((FunctionAbstractEntry) entry, expressions);
                    } else {
                        throw new CompilerException(
                                "Cannot find member function " + id + "in Class " + currentScope.getName()
                        );
                    }
                } else {
                    // entry is free function
                    // todo need to do something before a function call
                    return new FunctionCallValue((FunctionEntry)entry, expressions);
                }
            }
        };
    }
}
