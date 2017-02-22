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

    private ParserTable parseTable;

    public ParserGenerator(String[] terminals, String[] nonTerminals, String start) {
        this.terminalMap = new HashMap<>();
        this.nonTerminalMap = new HashMap<>();
        for (String t : terminals) {
            if (t.equals(EpsilonRule.getEpsilonRule().getSymbol()))
                this.terminalMap.put(t, EpsilonRule.getEpsilonRule());
            else
                this.terminalMap.put(t, new TerminalRule(t));
        }
        for (String nt : nonTerminals)
            this.nonTerminalMap.put(nt, new NonTerminalRule(nt));
        TerminalRule dollarRule = new TerminalRule("$");
        this.terminalMap.put("$", dollarRule);
        NonTerminalRule startRule = nonTerminalMap.get(start);
        startRule.getFollowSet().add(dollarRule);
        this.parseTable = new ParserTable(startRule, dollarRule);
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
            if (newRule != null)
                this.nonTerminalRules.addLast(newRule);
        }
    }

    private void removeLeftFactoring() {
        NonTerminalRule current;
        for (int i = 0; i < this.nonTerminalRules.size(); i++) {
            // get the rule
            current = this.nonTerminalRules.get(i);
            LinkedList<NonTerminalRule> resultList = new LinkedList<>();
            LinkedList<NonTerminalRule> tmp;
            while ((tmp = current.removeLeftFactoring()) != null)
                resultList.addAll(tmp);
            for (NonTerminalRule non : resultList)
                this.nonTerminalRules.addLast(non);
        }
    }

    private void buildFirstSet() {
        boolean wasChange = true;
        while (wasChange) {
            wasChange = false;
            for (NonTerminalRule non : this.nonTerminalRules)
                wasChange = non.buildFirstSet(wasChange);
        }
    }

    private void buildFollowSet() {
        boolean wasChanged = true;
        while (wasChanged) {
            wasChanged = false;
            for (NonTerminalRule non : this.nonTerminalRules)
                wasChanged = non.buildFollowSet(wasChanged);
        }
    }

    private void buildParserTable() {
        this.terminalRules.remove(EpsilonRule.getEpsilonRule());
        for (NonTerminalRule non : this.nonTerminalRules) {
            for (TerminalRule ter : this.terminalRules)
                this.parseTable.put(non, ter, non.getProductionRuleForTerminal(ter));
            if (non.getProductionRuleForTerminal(EpsilonRule.getEpsilonRule()) != null) {
                LinkedList<TerminalRule> followList = non.getFollowSet();
                for (TerminalRule followEntry : followList)
                    this.parseTable.put(non, followEntry, EpsilonRule.getEpsilonList());
            }
        }
    }

    public void preparationOne() {
        this.convertHashMapToLinkedList();
        this.removeLeftRecursion();
        this.removeLeftFactoring();
    }

    public void preparationTwo() {
        this.buildFirstSet();
        this.buildFollowSet();
        this.buildParserTable();
    }

    public void toDetailString() {
        for (NonTerminalRule non : nonTerminalRules) {
            System.out.println(non.toDetailString());
        }
    }

    public void printFirstSet() {
        for (NonTerminalRule non : nonTerminalRules) {
            System.out.print(non.symbol + ": ");
            for (TerminalRule t : non.getFirstSet()) {
                System.out.print(t.toString() + " ");
            }
            System.out.println();
        }
    }

    public void printFollowSet() {
        for (NonTerminalRule non : nonTerminalRules) {
            System.out.print(non.symbol + ": ");
            for (TerminalRule t : non.getFollowSet()) {
                System.out.print(t.toString() + " ");
            }
            System.out.println();
        }
    }

    public void printTable() {
        parseTable.printTable();
    }
}
