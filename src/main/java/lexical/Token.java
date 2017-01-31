package lexical;

/**
 * Created by ERIC_LAI on 2017-01-17.
 */
public class Token {

    private TokenType type;
    private String value;
    private int location;

    public Token(TokenType type, String value, int location) {
        this.type = type;
        this.value = value;
        this.location = location;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
//        String type = "";
        return "<" + type + ", " + value + ", " + location + ">";
    }
}
