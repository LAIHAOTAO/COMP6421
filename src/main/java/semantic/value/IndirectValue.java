package semantic.value;

import codegenerate.CodeGenerateContext;

/**
 * Created by ERIC_LAI on 2017-03-31.
 */
public class IndirectValue extends DynamicValue {

    private Value value;

    public IndirectValue(Value value) {
        this.value = value;
    }

    @Override
    public Value getUsedValue(CodeGenerateContext context) {
        return value.getUsedValue(context);
    }

    @Override
    public RegisterValue getRegisterValue(CodeGenerateContext context) {
        return value.getRegisterValue(context);
    }

}
