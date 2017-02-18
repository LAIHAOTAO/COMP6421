package syntactic;

/**
 * Created by ERIC_LAI on 2017-02-17.
 */
public interface SymbolConfig {

    String[] TERMINALS = {
            "IF", "THEN", "ELSE", "FOR",
            "CLASS", "INT", "FLOAT", "GET",
            "PUT", "RETURN", "PROGRAM",
            "AND", "OR", "NOT",
            "ADDITION", "SUBSTATION", "MULTIPLICATION", "DIVISION",
            "INT_NUM", "FLOAT_NUM",
            "IDENTIFIER",
            "EQ", "NEQ",
            "GEQ", "LEQ", "GT", "LT",
            "SEMICOLON", "DOT", "COMMA",
            // (, ), {, }, [, ]
            "LCP", "RCP", "LBP", "RBP", "LSP", "RSP"
    };

    String[] NONTERMINALS = {};
}
