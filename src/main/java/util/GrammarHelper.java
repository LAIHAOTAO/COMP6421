package util;

import syntactic.*;

import java.util.*;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class GrammarHelper {


    public static void addSemanticActionInGrammar(ParserGenerator generator, HashMap<String, NonTerminalRule> map) {
        ParserTable table = generator.getParseTable();
        HashMap<String, LinkedList<GrammarRule>> entries = table.getEntries();
        Set<String> tableKeySet = generator.getParseTable().keySet();

        for (String mapKey : map.keySet()) {
            List<String> needReplace = getNeedReplaceKey(tableKeySet, mapKey);

            if (!needReplace.isEmpty()) {

                for (String needReplaceKey : needReplace) {

                    // get table the right side rule with the current key
                    LinkedList<GrammarRule> tableRuleRightSide = entries.get(needReplaceKey);

                    // get the non-terminal
                    NonTerminalRule non = map.get(mapKey);
                    LinkedList<ProductionRule> pdrs = non.getRules();

                    // I really don't know what happen here but it is a bug exist for now just
                    // manually cast "sym_" and "sem_" to ActionGrammar
                    for (ProductionRule p : pdrs) {
                        LinkedList<GrammarRule> gs = p.getContent();
                        for (int i = 0; i < gs.size(); i++) {
                            String sym = gs.get(i).getSymbol();
                            if (sym.contains("sem_") || sym.contains("sym_")) {
                                gs.set(i, new ActionRule(sym));
                            }
                        }
                    }

                    // use pdrs to replace the rule in tableRuleRightSide, according to their first rule
                    for (ProductionRule pd : pdrs) {
                        if (match(tableRuleRightSide, pd)) {
                            tableRuleRightSide = pd.getContent();
                            entries.put(needReplaceKey, tableRuleRightSide);
                            break;
                        }
                    }
                }
            }
        }

    }

    private static boolean match(LinkedList<GrammarRule> gr, ProductionRule pdrs) {
        int i = 0, j = 0, counter = 0;
        int len = (gr.size() < pdrs.size())? gr.size() : pdrs.size();
        while (j < len) {
            if (Objects.equals(gr.get(i).getSymbol(), pdrs.get(j).getSymbol())) {
                i++;
                j++;
                counter++;
            } else {
                j++;
            }
        }
        return counter == gr.size();
    }

    private static List<String> getNeedReplaceKey(Set<String> tableKeySet, String key) {
        List<String> list = new ArrayList<>();

        for (String tableKey : tableKeySet) {
            if (tableKey.contains(key)) {
                list.add(tableKey);
            }
        }

        return list;
    }
}
