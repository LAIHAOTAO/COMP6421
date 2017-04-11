package syntactic;

import common.Const;
import lexical.LexicalScanner;
import org.junit.Test;
import semantic.ActionGrammarInjector;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by ERIC_LAI on 2017-04-01.
 */
@SuppressWarnings("Duplicates")
public class GrammarOneTest {

    @Test
    public void test() throws IOException {
        String startSymbol = "prog";

        // pure grammar
        GrammarFileReader pureGrammarReader = new GrammarFileReader(Const.DIR_CONFIG + "/OriginalGrammar.txt");
        ParserGenerator pureGenerator = new ParserGenerator(
                pureGrammarReader.getTerminalRuleSymbols(),
                pureGrammarReader.getNonTerminalRuleSymbols(),
                startSymbol
        );

        HashMap<String, String> pureMap = pureGrammarReader.getMap();

        for (String key : pureMap.keySet()) {
            String[] productionRuleStrs = pureMap.get(key).split("\\|");
            for (String productionRuleStr : productionRuleStrs) {
                String[] grammarRuleStrs = productionRuleStr.trim().split(" ");
                pureGenerator.addGrammarRule(key, grammarRuleStrs);
            }
        }

        pureGenerator.processPureGrammar();

//        pureGenerator.getParseTable().printTable();
//
//        System.out.println("===================");
//        System.out.println();
//        System.out.println();
//        System.out.println();

        // action grammar
        GrammarFileReader ar = new GrammarFileReader(Const.DIR_CONFIG + "/ActionGrammar.txt");
        ParserGenerator pg = new ParserGenerator(
                ar.getTerminalRuleSymbols(),
                ar.getNonTerminalRuleSymbols(),
                ar.getActionRuleSymbols(),
                startSymbol
        );
        HashMap<String, String> map = ar.getMap();
        for (String key : map.keySet()) {
            String[] productionRuleStrs = map.get(key).split("\\|");
            for (String productionRuleStr : productionRuleStrs) {
                String[] grammarRuleStrs = productionRuleStr.trim().split(" ");
                pg.addGrammarRule(key, grammarRuleStrs);
            }
        }

        // update the grammar
        ActionGrammarInjector.addSemanticActionInGrammar(pureGenerator, pg.getNonTerminalMap());
        // end of update grammar

//        pureGenerator.getParseTable().printTable();


        LexicalScanner scanner = new LexicalScanner(Const.DIR_RES + "semantic/MigrationTest.txt");

        ParserDriver parserDriver;
        try {


            Parser.turnOnDebug();

            Parser.firstParse(scanner, pureGenerator.getParseTable());
            boolean isSuccess = Parser.secondParse(scanner, pureGenerator.getParseTable());

            System.out.println("parsing result: " + ((isSuccess) ? "success" : "fail"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
