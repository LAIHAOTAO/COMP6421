package exception;

/**
 * Created by ERIC_LAI on 2017-01-26.
 */
public class NoSuchLexemeException extends Exception {

    private String unexpectedLexeme;
    private String errorFilePath;
    private int location;

    public NoSuchLexemeException(String unexpectedLexeme, int location, String path) {
        this.unexpectedLexeme = unexpectedLexeme;
        this.location = location;
        this.errorFilePath = path;
    }

    @Override
    public String toString() {
        return "There is no " + unexpectedLexeme + " defined in the language in line " + location
                + " of file " + errorFilePath;
    }
}
