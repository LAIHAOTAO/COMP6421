package syntactic;

import java.util.LinkedList;

/**
 * Created by ERIC_LAI on 2017-02-15.
 */
public class TerminalRule extends GrammarRule{

    public TerminalRule(String sym) {
        this.symbol = sym;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public LinkedList<TerminalRule> getFirst() {
        // the first set of a terminal is itself
        LinkedList<TerminalRule> first = new LinkedList<>();
        first.add(this);
        return first;
    }

    @Override
    public LinkedList<TerminalRule> getFollow() {
        // terminals do not have follow set
        throw new RuntimeException("terminal symbol do not have any follow set");
    }

    @Override
    public String toString() {
        return this.symbol;
    }
}
