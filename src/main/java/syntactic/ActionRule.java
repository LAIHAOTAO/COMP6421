package syntactic;

import java.util.LinkedList;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class ActionRule extends GrammarRule {

    public ActionRule(String sym) {
        this.symbol = sym;
    }

    public void execute() {

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
