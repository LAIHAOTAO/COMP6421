package semantic.value;

/**
 * Created by ERIC_LAI on 2017-03-27.
 */
public class StaticIntValue extends StaticValue implements Value {

    private final int value;

    public StaticIntValue(int value) {
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
    public RegisterValue getRegisterValue() {
        return null;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof StaticIntValue && ((StaticIntValue)other).intValue() == value;
    }
}
