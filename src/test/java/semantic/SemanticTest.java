package semantic;

import common.Const;
import lexical.LexicalScanner;
import org.junit.Test;
import syntactic.*;
import util.GrammarHelper;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by ERIC_LAI on 2017-02-15.
 */
@SuppressWarnings("Duplicates")
public class SemanticTest {



    @Test
    public void testProgram() throws IOException {

        String startSymbol = "prog";

        // pure grammar

        GrammarFileReader pureGrammarReader = new GrammarFileReader(Const.DIR_CONFIG + "/ng.txt");
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

//        pureGenerator.printTable();


        GrammarFileReader ar = new GrammarFileReader(Const.DIR_CONFIG + "/action-grammar.txt");
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
        GrammarHelper.addSemanticActionInGrammar(pureGenerator, pg.getNonTerminalMap());
        // end of update grammar

//        System.out.println("============================================================");

//        pureGenerator.printTable();

        LexicalScanner scanner = new LexicalScanner(Const.DIR_RES + "semantic/TestParsingProcess.txt");
        Parser.turnOnDebug = true;
        boolean isSuccess = Parser.parse(scanner, pureGenerator.getParseTable());
        System.out.println("parsing result: " + ((isSuccess)? "success" : "fail"));
    }


}
