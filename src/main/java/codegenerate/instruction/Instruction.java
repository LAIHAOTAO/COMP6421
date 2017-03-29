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
        return this.label + "\t" + getAssemblyCode() + "%\t" + this.comment;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
