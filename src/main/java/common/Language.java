package common;

/**
 * Created by ERIC_LAI on 2017-01-18.
 */
public class Language {

    public static final String[] KEYWORDS = {
            "if", "then", "else", "for",
            "class", "int", "float", "get",
            "put", "return", "program",
            "and", "or", "not"
    };

    public static final String[] TERMINALS = {
            "IF", "THEN", "ELSE", "FOR", "CLASS", "GET", "PUT", "RETURN", "PROGRAM",
            "AND", "OR", "NOT",
            "ADDITION", "SUBTRACTION", "MULTIPLICATION", "DIVISION",
            "INT", "FLOAT",
            "INT_NUM", "FLOAT_NUM",
            "ID",
            "EQ", "NEQ",
            "LEQ", "GEQ", "GT", "LT", "EQ",
            "COMMA", "DOT", "SEMICOLON", "ASSIGNMENT",
            "LCP", "RCP", "LBP", "RBP", "LSP", "RSP"
    };

}
