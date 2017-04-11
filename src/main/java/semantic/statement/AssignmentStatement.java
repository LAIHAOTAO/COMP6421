package semantic.statement;

import codegenerate.CodeGenerateContext;
import codegenerate.instruction.Instruction;
import codegenerate.instruction.SWInstruction;
import exception.CompilerException;
import semantic.expression.ExpressionElement;
import semantic.expression.RelationExpressionFragment;
import semantic.expression.VariableElementFragment;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.type.SymbolTableEntryType;
import semantic.value.*;

import java.util.Objects;

/**
 * Created by ERIC_LAI on 2017-03-24.
 */
public class AssignmentStatement extends ExpressionElement implements Statement {

    public enum State {
        WAIT_LHS,
        LHS,
        WAIT_RHS,
        RHS,
        DONE,
    }

    private State currentState;
    private Value lhs;
    private Value rhs;
    private SymbolTableEntryType leftType;
    private SymbolTableEntryType rightType;


    public AssignmentStatement() {
        this.currentState = State.WAIT_LHS;
    }


    @Override
    public Value getValue() {
        return VoidValue.get();
    }

    @Override
    public void pushID(String varName) {
        if (currentState == State.WAIT_LHS) {
            currentState = State.LHS;
            // put a new expression in the top of the symContext
            VariableElementFragment v = new VariableElementFragment(varName);
            context.push(v);
            leftType = v.getType();
        } else if (currentState == State.WAIT_RHS) {
            currentState = State.RHS;
            // put a new expression in the top of the symContext
            context.push(new VariableElementFragment(varName));
        }
    }

    @Override
    public void accept(ExpressionElement expr) {
        if (currentState == State.LHS) {
            lhs = expr.getValue();
            currentState = State.RHS;
//            if (expr instanceof RelationExpressionFragment) {
//                leftType = ((RelationExpressionFragment) expr).getType();
//            }
        } else if (currentState == State.RHS) {
            rhs = expr.getValue();
            currentState = State.DONE;
            if (expr instanceof RelationExpressionFragment) {
                rightType = ((RelationExpressionFragment) expr).getType();
            }

            // temporary solution
//            if (!Objects.equals(leftType, rightType)) {
//                throw new CompilerException("Type error in the assignment statement.");
//            }
            // end of temporary solution

            context.finish();
            System.out.println("AssignmentStatement: " + lhs + " = " + rhs);
        } else {
            throw new CompilerException("Unexpected " + expr + " while in state " + currentState);
        }
    }

    @Override
    public void generateCode(CodeGenerateContext c) {
        if (this.currentState == State.DONE) {
            if (lhs instanceof StoredValue) {
                RegisterValue rhsRegVal = rhs.getRegisterValue(c);
                AbsoluteAddressValue lhsAddrVal = ((StoredValue) lhs).getAbsAddress(c);
                Instruction storeWord = new SWInstruction(
                        lhsAddrVal.getOffset().intValue(),
                        lhsAddrVal.getBaseAddr().getRegister(),
                        rhsRegVal.getRegister()
                );

                c.appendInstruction(storeWord);

                if (!lhsAddrVal.getBaseAddr().getRegister().reserved) {
                    c.registerManager.freeRegister(lhsAddrVal.getBaseAddr().getRegister());
                }
                if (!rhsRegVal.getRegister().reserved) {
                    c.registerManager.freeRegister(rhsRegVal.getRegister());
                }

            } else {
                throw new CompilerException("Expected a stored value left hand side in assignment" +
                        " statement");
            }
        } else {
            throw new CompilerException("Cannot generate the code, since the assignment statement" +
                    " is not complete !");
        }
    }
}
