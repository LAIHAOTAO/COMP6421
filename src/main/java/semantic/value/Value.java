package semantic.value;

import codegenerate.CodeGenerateContext;

/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public interface Value {

    Value getUsedValue(CodeGenerateContext context);

    boolean isStatic();

    RegisterValue getRegisterValue(CodeGenerateContext context);
}
