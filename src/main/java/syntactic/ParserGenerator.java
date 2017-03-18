package syntactic;

import common.Const;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class is used to generate the parse table
 *
 * @author Haotao Lai (haotao.lai@gmail.com) on 2017-02-17
 */
public class ParserGenerator {

    private HashMap<String, NonTerminalRule> nonTerminalMap;
    private HashMap<String, TerminalRule> terminalMap;
    private HashMap<String, ActionRule> actionMap;

    private LinkedList<NonTerminalRule> nonTerminalRules;
    private LinkedList<TerminalRule> terminalRules;

    private ParserTable parseTable;

    public ParserGenerator(String[] terminals, String[] nonTerminals, String start) {
        this.terminalMap = new HashMap<>();
        this.nonTerminalMap = new HashMap<>();
        this.actionMap = new HashMap<>();

        // create terminal rule map
        for (String t : terminals) {
            if (t.equals(EpsilonRule.getEpsilonRule().getSymbol())) {
                this.terminalMap.put(t, EpsilonRule.getEpsilonRule());
            } else {
                this.terminalMap.put(t, new TerminalRule(t));
            }
        }

        // create non-terminal rule map
        for (String nt : nonTerminals) {
            this.nonTerminalMap.put(nt, new NonTerminalRule(nt));
        }
        TerminalRule dollarRule = new TerminalRule("$");
        this.terminalMap.put("$", dollarRule);

        NonTerminalRule startRule = nonTerminalMap.get(start);
        startRule.getFollowSet().add(dollarRule);

        // build an empty parsing table, wait for adding rule inside
        this.parseTable = new ParserTable(startRule, dollarRule);

    }

    public ParserGenerator(String[] terminals, String[] nonTerminals, String[] actions, String start) {
        this(terminals, nonTerminals, start);

        // create action rule map
        for (String act : actions) {
            this.actionMap.put(act, new ActionRule(act));
        }
    }

    public HashMap<String, NonTerminalRule> getNonTerminalMap() {
        return nonTerminalMap;
    }

    // API for driver to add its constructed rule
    public void addGrammarRule(String symbol, String[] grammarRules) {
        if (!this.nonTerminalMap.containsKey(symbol)) {
            throw new RuntimeException("ParserGenerator, symbol: " + symbol + " is not in the grammar");
        } else if (symbol.equals("$")) {
            throw new RuntimeException("ParserGenerator, $ cannot be used as a symbol");
        }

        LinkedList<GrammarRule> rules = new LinkedList<>();

        for (String gr : grammarRules) {

            if (this.nonTerminalMap.containsKey(gr)) {
                rules.add(this.nonTerminalMap.get(gr));
            }

            else if (this.terminalMap.containsKey(gr)) {
                rules.add(this.terminalMap.get(gr));
            }

            else if (this.actionMap.containsKey(gr)) {
                rules.add(this.actionMap.get(gr));
            }

            else {
                throw new RuntimeException(gr + " is not in the grammar");
            }
        }
        this.nonTerminalMap.get(symbol).addRule(rules);
    }

    // do the preparation job to create the parse table
    public void processPureGrammar() {
        this.preparationOne();
        this.preparationTwo();
    }

    // print out all NonTerminal symbol
    public void toDetailString() {
        for (NonTerminalRule non : nonTerminalRules) {
            System.out.println(non.toDetailString());
        }
    }

    // print out all NonTerminal's first set
    public void printFirstSet() {
        for (NonTerminalRule non : nonTerminalRules) {
            System.out.print(non.symbol + ": ");
            for (TerminalRule t : non.getFirstSet()) {
                System.out.print(t.toString() + " ");
            }
            System.out.println();
        }
    }

    // print out all Terminal's follow set
    public void printFollowSet() {
        for (NonTerminalRule non : nonTerminalRules) {
            System.out.print(non.symbol + ": ");
            for (TerminalRule t : non.getFollowSet()) {
                System.out.print(t.toString() + " ");
            }
            System.out.println();
        }
    }

    // print out all parse table
    public void printTable() {
        parseTable.printTable();
    }

    public ParserTable getParseTable() {
        return parseTable;
    }



    // =============================================================================
    // ==============================private methods================================
    // =============================================================================

    private void preparationOne() {
        // convert the hasp map to linked list, in order to remove left recursion and left factoring
        this.convertHashMapToLinkedList();
        this.removeLeftRecursion();
        this.removeLeftFactoring();
        // construct the first set, follow set and parse table
        this.buildFirstSet();
        this.buildFollowSet();
    }

    private void preparationTwo() {
        this.buildParserTable();
    }

    // convert the hasp map to linked list, release the map variable to GC
    private void convertHashMapToLinkedList() {
        nonTerminalRules = new LinkedList<>();
        terminalRules = new LinkedList<>();
        this.nonTerminalRules.addAll(this.nonTerminalMap.values());
        this.terminalRules.addAll(this.terminalMap.values());
//        // clear the hash map to release the memory
//        nonTerminalMap.clear();
//        terminalMap.clear();
//        nonTerminalMap = null;
//        terminalMap = null;
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

    // build first set of all NonTerminal
    private void buildFirstSet() {
        boolean wasChange = true;
        while (wasChange) {
            wasChange = false;
            for (NonTerminalRule non : this.nonTerminalRules)
                wasChange = non.buildFirstSet(wasChange);
        }
    }

    // build follow set of all Terminal
    private void buildFollowSet() {
        boolean wasChanged = true;
        while (wasChanged) {
            wasChanged = false;
            for (NonTerminalRule non : this.nonTerminalRules) {
                wasChanged = non.buildFollowSet(wasChanged);
            }
        }
    }

    // build the whole parse table, add the rule into the table
    private void buildParserTable() {
        this.terminalRules.remove(EpsilonRule.getEpsilonRule());

        for (NonTerminalRule non : this.nonTerminalRules) {
            for (TerminalRule ter : this.terminalRules) {
                // the production rule here should contain the action rule
                this.parseTable.put(non, ter, non.getProductionRuleForTerminal(ter));
            }
            if (non.getProductionRuleForTerminal(EpsilonRule.getEpsilonRule()) != null) {
                LinkedList<TerminalRule> followList = non.getFollowSet();
                for (TerminalRule followEntry : followList) {
                    this.parseTable.put(non, followEntry, EpsilonRule.getEpsilonList());
                }
            }
        }
    }

    // end of private methods

} // end of file
