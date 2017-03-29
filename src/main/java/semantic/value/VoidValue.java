package semantic.value;

import codegenerate.CodeGenerateContext;

/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public class VoidValue implements Value {

    private static VoidValue instance = new VoidValue();

    private VoidValue() {}

    public static VoidValue get() {
        return instance;
    }

    @Override
    public Value getUsedValue(CodeGenerateContext context) {
        return null;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public RegisterValue getRegisterValue(CodeGenerateContext context) {
        return null;
    }
}
