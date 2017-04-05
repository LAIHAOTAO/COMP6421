package syntactic;

import exception.CompilerException;

import java.util.LinkedList;

/**
 * This class is used to describe the TerminalRule.
 *
 * @author Haotao Lai (haotao.lai@gmail.com) on 2017-02-15.
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
    public boolean isAction() {
        return false;
    }

    @Override
    public LinkedList<TerminalRule> getFirstSet() {
        // the first set of a terminal is itself
        LinkedList<TerminalRule> first = new LinkedList<>();
        first.add(this);
        return first;
    }

    @Override
    public LinkedList<TerminalRule> getFollowSet() {
        // terminals do not have follow set
        throw new CompilerException("terminal symbol do not have any follow set");
    }

    @Override
    public String toString() {
        return this.symbol;
    }
}
