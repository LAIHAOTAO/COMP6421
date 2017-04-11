package semantic;

import common.Const;
import lexical.LexicalScanner;
import org.junit.Test;
import syntactic.GrammarFileReader;
import syntactic.Parser;
import syntactic.ParserGenerator;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by ERIC_LAI on 2017-03-20.
 */
@SuppressWarnings("Duplicates")
public class CorrectSemanticTest {

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

        LexicalScanner scanner = new LexicalScanner(Const.DIR_RES + "semantic/CorrectProgram.txt");
        Parser.turnOnDebug();
        boolean isSuccess = Parser.firstParse(scanner, pureGenerator.getParseTable());
        System.out.println("parsing result: " + ((isSuccess) ? "success" : "fail"));
    }



}
