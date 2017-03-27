package semantic.value;

/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public abstract class StaticValue implements Value{

    public abstract int intValue();

    public abstract float floatValue();

    @Override
    public Value getUsedValue() {
        return this;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StaticValue && intValue() == ((StaticValue)obj).intValue();
    }
}
