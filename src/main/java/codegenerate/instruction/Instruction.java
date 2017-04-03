package codegenerate.instruction;

/**
 * Created by ERIC_LAI on 2017-03-28.
 */
public abstract class Instruction {

    protected String label;
    protected String comment;

    public Instruction() {
        this.label = "";
        this.comment = "";
    }

    protected abstract String generateAssemblyCode();

    public String getAssemblyCode() {
        return this.label + "\t" + generateAssemblyCode() + "\t% " + this.comment;
    }

    public Instruction setLabel(String label) {
        this.label = label;
        return this;
    }

    public Instruction setComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public String toString() {
        return getAssemblyCode();
    }
}
