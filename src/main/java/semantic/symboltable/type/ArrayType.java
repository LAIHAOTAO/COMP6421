package semantic.symboltable.type;

import semantic.symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by ERIC_LAI on 2017-03-26.
 */
public class ArrayType implements SymbolTableEntryType {

    private final SymbolTableEntryType type;
    private List<Integer> dimension;

    public ArrayType(SymbolTableEntryType type) {
        this.type = type;
        this.dimension = new ArrayList<>(5);
    }

    @Override
    public int getSize() {
        int d = 1;
        for (Integer integer : dimension) {
            if (integer != null) {
                d *= integer;
            }
        }
        return d * type.getSize();
    }

    @Override
    public SymbolTable getScope() {
        return null;
    }

    @Override
    public String toString() {
        String str = this.type.toString();
        for (int d : dimension) {
            str += "[" + d + "]";
        }
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ArrayType) {
            ArrayType other = (ArrayType) obj;
            if (Objects.equals(this.type, other.getArrayTypeType())) {
                List<Integer> otherDimension = other.getDimension();
                for (int i = 0; i < this.dimension.size(); i++) {
                    if (!Objects.equals(this.dimension.get(i), otherDimension.get(i))) {
                        return false;
                    }
                }
                // if the type and dimension size are identical
                return true;

            } else return false;
        }return false;
    }

    public List<Integer> getDimension() {
        return dimension;
    }

    public void setDimension(ArrayList<Integer> dimension) {
        this.dimension = dimension;
    }

    public void addDimension(int d) {
        this.dimension.add(d);
    }

    public SymbolTableEntryType getArrayTypeType() {
        return this.type;
    }
}
