package syntactic;

import lexical.Token;
import semantic.SemanticActionHandler;
import semantic.symboltable.SymbolTableActionHandler;

import java.util.LinkedList;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class ActionRule extends GrammarRule {

    public ActionRule(String sym) {
        this.symbol = sym;
    }

    public void execute(Token prevToken, int parseNum) {
        if (this.symbol.contains("sym_")) {
            SymbolTableActionHandler.process(this.symbol, prevToken, parseNum);
        }
        else if (this.symbol.contains("sem_")) {
            if (parseNum == Parser.SECOND_PARSE) {
                SemanticActionHandler.process(this.symbol, prevToken);
            }
            // if it is the first parse, just ignore the semantic action
        }
        else {
            throw new RuntimeException("No such semantic action");
        }
    }



    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isAction() {
        return true;
    }

    @Override
    public LinkedList<TerminalRule> getFirstSet() {
        return null;
    }

    @Override
    public LinkedList<TerminalRule> getFollowSet() {
        return null;
    }

    @Override
    public String toString() {
        return this.symbol;
    }

}
