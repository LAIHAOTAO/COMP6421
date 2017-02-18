package lexical;

/**
 * Created by ERIC_LAI on 2017-01-17.
 */
public enum TokenType {

    IF, THEN, ELSE, FOR, CLASS, GET, PUT, RETURN, PROGRAM, INT, FLOAT,
    AND, OR, NOT,
    INT_NUM, FLOAT_NUM,
    IDENTIFIER,
    COMMENT,
    EQ, NEQ,
    GEQ, LEQ, GT, LT,
    ASSIGNMENT,
    SEMICOLON, DOT, COMMA,
    ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION,
    // (, ), {, }, [, ]
    LCP, RCP, LBP, RBP, LSP, RSP,
    LEXICAL_ERR
}
