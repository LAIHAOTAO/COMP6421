package syntactic;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by ERIC_LAI on 2017-02-17.
 */
public class ParserTable {

    private HashMap<String, LinkedList<GrammarRule>> entries;
    private NonTerminalRule start;
    private TerminalRule dollar;

    public ParserTable(NonTerminalRule startRule, TerminalRule dollarRule) {
        this.entries = new HashMap<>();
        this.start = startRule;
        this.dollar = dollarRule;
    }

    public void put(NonTerminalRule non, TerminalRule ter, LinkedList<GrammarRule> value) {
        if (null == value) return;
        this.entries.put(non.getSymbol() + ter.getSymbol(), value);
    }

    public LinkedList<GrammarRule> get(NonTerminalRule non, TerminalRule ter) {
        String key = non.getSymbol() + ter.getSymbol();
        return entries.get(key);
    }

    public LinkedList<GrammarRule> get(NonTerminalRule nonKey, String terStr) {
        return this.entries.get(nonKey.getSymbol() + terStr);
    }

    public LinkedList<GrammarRule> remove(NonTerminalRule nonKey, TerminalRule terStr) {
        return this.entries.remove(nonKey.getSymbol() + terStr.getSymbol());
    }

    public NonTerminalRule getStart() {
        return start;
    }

    public void setStart(NonTerminalRule start) {
        this.start = start;
    }

    public TerminalRule getDollar() {
        return dollar;
    }

    public void setDollar(TerminalRule dollar) {
        this.dollar = dollar;
    }

    public void printTable() {
        for (String str : entries.keySet()) {
            System.out.println(str + " --> " + entries.get(str).toString());
        }
    }
}
