package semantic;

import lexical.Token;

/**
 * Created by ERIC_LAI on 2017-03-17.
 */
public interface SemanticAction {

    void execute(Token token);
}
