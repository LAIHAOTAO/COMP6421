package syntactic;

import common.Const;
import lexical.LexicalScanner;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by ERIC_LAI on 2017-02-15.
 */


public class ParserTest {

    @Test
    public void testRemoveLeftRecursion() {
        NonTerminalRule nonTerminal = new NonTerminalRule("A");
        LinkedList<GrammarRule> grammar1 = new LinkedList<>();
        grammar1.add(new NonTerminalRule("A"));
        grammar1.add(new TerminalRule("a"));
        LinkedList<GrammarRule> grammar2 = new LinkedList<>();
        grammar2.add(new NonTerminalRule("A"));
        grammar2.add(new TerminalRule("b"));
        LinkedList<GrammarRule> grammar3 = new LinkedList<>();
        grammar3.add(new NonTerminalRule("A"));
        grammar3.add(new TerminalRule("e"));
        LinkedList<GrammarRule> grammar4 = new LinkedList<>();
        grammar4.add(new TerminalRule("f"));
        ProductionRule production1 = new ProductionRule(grammar1);
        ProductionRule production2 = new ProductionRule(grammar2);
        ProductionRule production3 = new ProductionRule(grammar3);
        ProductionRule production4 = new ProductionRule(grammar4);
        LinkedList<ProductionRule> prs = new LinkedList<>();
        prs.add(production1);
        prs.add(production2);
        prs.add(production3);
        prs.add(production4);
        nonTerminal.setRules(prs);
        System.out.println("======before remove left recursion======");
        System.out.println(nonTerminal.toDetailString());
        NonTerminalRule newNonterminalRule = nonTerminal.removeLeftRecursion();
        System.out.println("======after remove left recursion======");
        System.out.println(nonTerminal.toDetailString());
        System.out.println(newNonterminalRule.toDetailString());
    }

    @Test
    public void testRemoveLeftFactoring() {
        NonTerminalRule nonTerminal = new NonTerminalRule("C");
        LinkedList<GrammarRule> grammar1 = new LinkedList<>();
        grammar1.add(new NonTerminalRule("A"));
        grammar1.add(new NonTerminalRule("B"));
        grammar1.add(new NonTerminalRule("C"));
        grammar1.add(new TerminalRule("a"));
        LinkedList<GrammarRule> grammar2 = new LinkedList<>();
        grammar2.add(new NonTerminalRule("A"));
        grammar2.add(new NonTerminalRule("B"));
        grammar2.add(new TerminalRule("b"));
        LinkedList<GrammarRule> grammar3 = new LinkedList<>();
        grammar3.add(new NonTerminalRule("A"));
        grammar3.add(new NonTerminalRule("B"));
        grammar3.add(new NonTerminalRule("C"));
        grammar3.add(new TerminalRule("e"));
        LinkedList<GrammarRule> grammar4 = new LinkedList<>();
        grammar4.add(new NonTerminalRule("Z"));
        grammar4.add(new TerminalRule("e"));
        ProductionRule production1 = new ProductionRule(grammar1);
        ProductionRule production2 = new ProductionRule(grammar2);
        ProductionRule production3 = new ProductionRule(grammar3);
        ProductionRule production4 = new ProductionRule(grammar4);
        LinkedList<ProductionRule> prs = new LinkedList<>();
        prs.add(production1);
        prs.add(production2);
        prs.add(production3);
        prs.add(production4);
        nonTerminal.setRules(prs);
        System.out.println("======before left factoring======");
        System.out.println(nonTerminal.toDetailString());
        System.out.println("======after left factoring=======");
        LinkedList<NonTerminalRule> resultList = new LinkedList<>();
        LinkedList<NonTerminalRule> tmp;
        while ((tmp = nonTerminal.removeLeftFactoring()) != null) {
            resultList.addAll(tmp);
        }
        System.out.println(nonTerminal.toDetailString());
        for (NonTerminalRule non : resultList) {
            System.out.println(non.toDetailString());
        }
    }

    @Test
    public void testDerivation() throws IOException {

        Parser.turnOnDebug();

        String startSymbol = "prog";

        GrammarFileReader reader = new GrammarFileReader(Const.DIR_CONFIG + "/OriginalGrammar1.txt");
        ParserGenerator generator = new ParserGenerator(
                reader.getTerminalRuleSymbols(),
                reader.getNonTerminalRuleSymbols(),
                startSymbol
        );

        HashMap<String, String> map = reader.getMap();

        for (String key : map.keySet()) {
            String[] arr = map.get(key).split("\\|");
            for (String str : arr) {
                generator.addGrammarRule(key, str.trim().split(" "));
            }
        }

        generator.processPureGrammar();

        LexicalScanner scanner = new LexicalScanner(Const.DIR_RES + "syntactic/TestDerivation.txt");
        boolean isSuccess = Parser.firstParse(scanner, generator.getParseTable());
        System.out.println(isSuccess);
    }

    @Test
    public void test() throws IOException {
        Parser.turnOnDebug();
        for (int i = 1; i <= 18; i++) {
            testProgram(i);
        }

    }

    public void testProgram(int i) throws IOException {

        String startSymbol = "prog";

        GrammarFileReader reader = new GrammarFileReader(Const.DIR_CONFIG + "/OriginalGrammar1.txt");
        ParserGenerator generator = new ParserGenerator(
                reader.getTerminalRuleSymbols(),
                reader.getNonTerminalRuleSymbols(),
                startSymbol
        );

        HashMap<String, String> map = reader.getMap();

        for (String key : map.keySet()) {
            String[] arr = map.get(key).split("\\|");
            for (String str : arr) {
                generator.addGrammarRule(key, str.trim().split(" "));
            }
        }

        generator.processPureGrammar();

        LexicalScanner scanner = new LexicalScanner(Const.DIR_RES +
                "syntactic/testprogram/program" + i + ".txt");
        System.out.println("===============" + "parsing program" + i + "==================");
        boolean isSuccess = Parser.firstParse(scanner, generator.getParseTable());
        System.out.println("parsing result: " + ((isSuccess)? "success" : "fail"));
        System.out.println("parsing result file has been created");
    }


}
