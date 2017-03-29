package semantic.value;

import codegenerate.CodeGenerateContext;
import codegenerate.MathOpt;
import codegenerate.Register;
import codegenerate.instruction.MathOptImmInstruction;

/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public class StaticNumValue extends StaticValue implements Value {

    private final int value;

    public StaticNumValue(int value) {
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public RegisterValue getRegisterValue(CodeGenerateContext context) {
        // give that value a register
        RegisterValue regVal = new RegisterValue(context.registerManager.getAvailableRegister());

        // generate the assembly code, stored them in the code list
        //   for a static number (so far just Int), just need to add the offset (that number itself)
        //   with the R0 we can get the register number of that static number
        context.appendInstruction(
                new MathOptImmInstruction(
                        MathOpt.fromToken("+").immediateOpcode,
                        regVal.getRegister(),
                        Register.ZERO,
                        this.value
                )
        );

        return regVal;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof StaticNumValue && ((StaticNumValue) other).intValue() == value;
    }
}
