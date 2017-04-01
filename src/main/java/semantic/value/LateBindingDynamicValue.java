package semantic.value;

import codegenerate.CodeGenerateContext;

/**
 * Created by ERIC_LAI on 2017-03-29.
 */
public abstract class LateBindingDynamicValue extends DynamicValue {

    protected abstract Value get();

    @Override
    public Value getUsedValue(CodeGenerateContext context) {
        return get().getUsedValue(context);
    }

    @Override
    public RegisterValue getRegisterValue(CodeGenerateContext context) {
        return get().getRegisterValue(context);
    }
}
