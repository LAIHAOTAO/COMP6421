package semantic.value;

import codegenerate.CodeGenerateContext;
import codegenerate.Register;

/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public class RegisterValue extends DynamicValue{

    private final Register register;

    public RegisterValue(Register reg){
        this.register = reg;
    }

    public Register getRegister() {
        return register;
    }

    @Override
    public String toString() {
        return register.alias;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RegisterValue && ((RegisterValue)other).getRegister().equals(register);
    }

    @Override
    public Value getUsedValue(CodeGenerateContext context) {
        return this;
    }

    @Override
    public RegisterValue getRegisterValue(CodeGenerateContext context) {
        return this;
    }
}
