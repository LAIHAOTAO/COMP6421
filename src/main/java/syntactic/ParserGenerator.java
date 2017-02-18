package syntactic;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by ERIC_LAI on 2017-02-17.
 */
public class ParserGenerator {

    // contain all non-terminal symbol given in the grammar
    private HashMap<String, NonTerminalRule> nonTerminalMap;
    // contain all terminal symbol given in the grammar
    private HashMap<String, TerminalRule> terminalMap;

    private LinkedList<NonTerminalRule> nonTerminalRules;
    private LinkedList<TerminalRule> terminalRules;

    private ParserTable parserTable;

    public ParserGenerator(String[] terminals, String[] nonTerminals, String start) {
        this.terminalMap = new HashMap<>();
        this.nonTerminalMap = new HashMap<>();
        for (String t : terminals) {
            if (t.equals(EpsilonRule.getEpsilonRule().getSymbol()))
                this.terminalMap.put(t, EpsilonRule.getEpsilonRule());
            else
                this.terminalMap.put(t, new TerminalRule(t));
        }
        for (String nt : nonTerminals) {
            this.nonTerminalMap.put(nt, new NonTerminalRule(nt));
        }
        TerminalRule dollarRule = new TerminalRule("$");
//        this.terminalMap.put("$", dollarRule);
//        NonTerminalRule startRule = nonTerminalMap.get(start);
//        startRule.getFollow().add(dollarRule);
//        this.parserTable = new ParserTable(startRule, dollarRule);
    }

    public void addGrammarRule(String symbol, String[] productionRules) {
        if (!this.nonTerminalMap.containsKey(symbol)) {
            throw new RuntimeException
                    ("ParserGenerator, symbol: " + symbol + " is not in the grammar");
        } else if (symbol.equals("$")) {
            throw new RuntimeException("ParserGenerator, $ cannot be used as a symbol");
        }
        LinkedList<GrammarRule> rules = new LinkedList<>();
        for (String pr : productionRules) {
            if (this.nonTerminalMap.containsKey(pr))
                rules.add(this.nonTerminalMap.get(pr));
            else if (this.terminalMap.containsKey(pr))
                rules.add(this.terminalMap.get(pr));
            else
                throw new RuntimeException(pr + " is not in the grammar");
        }
        this.nonTerminalMap.get(symbol).addRule(rules);
    }

    private void convertHashMapToLinkedList() {
        nonTerminalRules = new LinkedList<>();
        terminalRules = new LinkedList<>();
        this.nonTerminalRules.addAll(this.nonTerminalMap.values());
        this.terminalRules.addAll(this.terminalMap.values());
        // clear the hash map to release the memory
        nonTerminalMap.clear();
        terminalMap.clear();
        nonTerminalMap = null;
        terminalMap = null;
    }

    private void removeLeftRecursion() {
        NonTerminalRule current, newRule;
        for (int i = 0; i < this.nonTerminalRules.size(); i++) {
            current = this.nonTerminalRules.get(i);
            newRule = current.removeLeftRecursion();
            if (newRule != null) {
                this.nonTerminalRules.addLast(newRule);
            }
        }
    }

    private void removeLeftFactoring() {
        NonTerminalRule current;
        for (int i = 0; i < this.nonTerminalRules.size(); i++) {
            // get the rule
            current = this.nonTerminalRules.get(i);
            LinkedList<NonTerminalRule> resultList = new LinkedList<>();
            LinkedList<NonTerminalRule> tmp;
            while ((tmp = current.removeLeftFactoring()) != null) {
                resultList.addAll(tmp);
            }
            for (NonTerminalRule non : resultList) {
                this.nonTerminalRules.addLast(non);
            }
        }
    }

    public void preparationOne() {
        this.convertHashMapToLinkedList();
        this.removeLeftRecursion();
        this.removeLeftFactoring();
    }

    public void toDetailString() {
        for (NonTerminalRule rule : nonTerminalRules) {
            System.out.println(rule.toDetailString());
        }
    }
}
