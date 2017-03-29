package semantic.value;

/**
 * Created by ERIC_LAI on 2017-03-28.
 */
public abstract class DynamicValue implements Value {

    @Override
    public boolean isStatic() {
        return false;
    }
}
