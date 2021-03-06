package common;

/**
 * Created by ERIC_LAI on 2017-01-17.
 */
public class Const {

    public static final char EOF = '\uFFFF';

    public static final int LINE_VALUE = 0;

    public static final int LINE_NUM = 1;

    public static final int LINE_RESULT_SIZE = 2;

    public static final String DIR_RES = "/Users/ERIC_LAI/IdeaProjects/COMP6421/src/main/resources/";

    public static final String DIR_OUTPUT = "/Users/ERIC_LAI/IdeaProjects/COMP6421/out/";

    public static final String DIR_CONFIG = DIR_RES + "configuration/";

    public static final int OFFSET_INIT = -1;

    public static final int REVERSED_REG_NUM = 3;

    public static final String FUNC_RETURN_ADDR = "@returnAddr";

    public static final String PREV_FRAME_POINTER = "@prevFp";

    public static final int PREV_FRAME_POINTER_SPACE = 4;

    public static final int FUNC_RETURN_ADDR_SPACE = 4;

    public static final int EXTRA_SPACE = 8;

    public enum ErrorLevel {
        Lexical, Syntactic, Semantic;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

}
