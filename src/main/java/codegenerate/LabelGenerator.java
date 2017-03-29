package codegenerate;

/**
 * Created by ERIC_LAI on 2017-03-28.
 */
public class LabelGenerator {

    public static LabelGenerator instance = new LabelGenerator();

    private int uniqueLabel;

    private LabelGenerator() {
        this.uniqueLabel = 0;
    }

    public int getUniqueLabel() {
        return ++uniqueLabel;
    }

}
