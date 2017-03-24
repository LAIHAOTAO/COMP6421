package syntactic;

import common.Const;
import semantic.GrammarInjector;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by ERIC_LAI on 2017-03-23.
 */
public class ParserDriver {

    private ParserTable table;

    public ParserDriver() throws IOException {

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
        GrammarInjector.addSemanticActionInGrammar(pureGenerator, pg.getNonTerminalMap());
        // end of update grammar

        this.table = pureGenerator.getParseTable();
    }

    public ParserTable getTable() {
        return table;
    }
}
