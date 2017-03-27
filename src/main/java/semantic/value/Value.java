package semantic.value;

/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public interface Value {

    Value getUsedValue();

    boolean isStatic();

    RegisterValue getRegisterValue();
}
