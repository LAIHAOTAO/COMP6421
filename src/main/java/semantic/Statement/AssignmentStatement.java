package semantic.Statement;

import codegenerate.CodeGenerateContext;
import codegenerate.instruction.Instruction;
import codegenerate.instruction.SWInstruction;
import semantic.expression.ExpressionElement;
import semantic.expression.VariableElementFragment;
import semantic.value.*;

/**
 * Created by ERIC_LAI on 2017-03-24.
 */
public class AssignmentStatement extends ExpressionElement implements Statement{

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
            context.push(new VariableElementFragment(varName));
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
        } else if (currentState == State.RHS) {
            rhs = expr.getValue();
            currentState = State.DONE;
            context.finish();
            System.out.println("AssignmentStatement: " + lhs + " = " + rhs);
        } else {
            throw new RuntimeException("Unexpected " + expr + " while in state " + currentState);
        }
    }

    @Override
    public String generateCode(CodeGenerateContext c) {
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

                return storeWord.toString();
            } else {
                throw new RuntimeException("Expected a stored value left hand side in assignment statement");
            }
        } else {
            throw new RuntimeException("Cannot generate the code, since the assignment statement is not complete !");
        }
    }
}
