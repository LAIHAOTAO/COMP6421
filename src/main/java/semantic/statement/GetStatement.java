package semantic.statement;

import codegenerate.CodeGenerateContext;
import codegenerate.Register;
import codegenerate.instruction.GetInstruction;
import codegenerate.instruction.SWInstruction;
import semantic.expression.ExpressionElement;
import semantic.expression.VariableElementFragment;
import semantic.value.AbsoluteAddressValue;
import semantic.value.StoredValue;
import semantic.value.Value;
import semantic.value.VoidValue;
import sun.tools.java.CompilerError;

/**
 * Created by ERIC_LAI on 2017-04-07.
 */
public class GetStatement extends ExpressionElement implements Statement {

    private StoredValue dest;

    @Override
    public void generateCode(CodeGenerateContext c) throws CompilerError {
        Register r = c.registerManager.getAvailableRegister();
        AbsoluteAddressValue addr = dest.getAbsAddress(c);

        c.appendInstruction(new GetInstruction(r));
        c.appendInstruction(new SWInstruction(
                addr.getOffset().intValue(), addr.getBaseAddr().getRegister(),  r)
        );

        c.registerManager.getAvailableRegister(r);
        c.registerManager.freeRegister(addr.getBaseAddr().getRegister());
    }

    @Override
    public void pushID(String id) throws CompilerError {
        context.push(new VariableElementFragment(id));
    }

    @Override
    public void accept(ExpressionElement e) throws CompilerError {
        if(e instanceof VariableElementFragment){
            dest = (StoredValue)e.getValue();
            context.finish();
        }else{
            super.accept(e);
        }
    }

    @Override
    public Value getValue() {
        return VoidValue.get();
    }
}
