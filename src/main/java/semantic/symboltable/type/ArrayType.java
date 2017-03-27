package semantic.symboltable.type;

import semantic.symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        return 0;
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

    public List<Integer> getDimension() {
        return dimension;
    }

    public void setDimension(ArrayList<Integer> dimension) {
        this.dimension = dimension;
    }

    public void addDimension(int d) {
        this.dimension.add(d);
    }
}
