package util;

import common.Language;
import syntactic.*;

import java.util.*;


/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class GrammarHelper {


    public static void addSemanticActionInGrammar(ParserGenerator generator, HashMap<String, NonTerminalRule> map) {

        // clean the map, remove useless entry
        {
            List<String> removeList = new ArrayList<>();
            for (String key : map.keySet()) {
                if (key.contains("sem_") || key.contains("sym_")) {
                    removeList.add(key);
                }
            }
            for (String key : removeList) {
                map.remove(key);
            }
        }

//        System.out.println("All NonTerminal rule, total " + map.size());
//        for (String key : map.keySet()) {
//            System.out.println(map.get(key).toDetailString());
//        }

        // get parse table key set
        ParserTable table = generator.getParseTable();
        HashMap<String, LinkedList<GrammarRule>> entries = table.getEntries();
        Set<String> tableKeySet = generator.getParseTable().keySet();

        for (String mapKey : map.keySet()) {
            // get the list of lefthand side contains the rule need to be replace
            List<String> needReplace = getNeedReplaceKey(tableKeySet, mapKey);

            if (!needReplace.isEmpty()) {
                // loop all key in the replace list
                for (String needReplaceKey : needReplace) {

                    // get table the righthand side rule with the current key
                    LinkedList<GrammarRule> tableRuleRightSide = entries.get(needReplaceKey);

                    // get the new data from the map which uses to replace the old data
                    NonTerminalRule non = map.get(mapKey);
                    LinkedList<ProductionRule> pdrs = non.getRules();

                    // I really don't know what happen here but there is a bug for now I have to
                    // manually cast all rules whose symbol contain "sym_" and "sem_" to ActionGrammar
                    for (ProductionRule p : pdrs) {
                        LinkedList<GrammarRule> gs = p.getContent();
                        for (int i = 0; i < gs.size(); i++) {
                            String sym = gs.get(i).getSymbol();
                            if (sym.contains("sem_") || sym.contains("sym_")) {
                                gs.set(i, new ActionRule(sym));
                            }
                        }
                    }

                    // debug
//                    for (ProductionRule p : pdrs) {
//                        LinkedList<GrammarRule> gs = p.getContent();
//                        System.out.println(gs);
//                    }
                    // end of debug

                    // use pdrs to replace the rule in tableRuleRightSide, according to their first rule
                    for (ProductionRule pd : pdrs) {
                        if (match(tableRuleRightSide, pd)) {

//                            System.out.println("match rule (old rule): " + tableRuleRightSide);
//                            System.out.println("match rule (new rule): " + pd.getContent());
//                            System.out.println();

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
        int grPtr = 0, pdPtr = 0, counter = 0;

        while (grPtr < gr.size() && pdPtr < pdrs.size()) {
            if (Objects.equals(gr.get(grPtr).getSymbol(), pdrs.get(pdPtr).getSymbol())) {
                grPtr++;
                pdPtr++;
                counter++;
            } else {
                pdPtr++;
            }
        }

        return counter == gr.size();
    }

    private static List<String> getNeedReplaceKey(Set<String> tableKeySet, String mapKey) {

        List<String> list = new ArrayList<>();

        for (String tableKey : tableKeySet) {
            for (String ter : Language.TERMINALS) {
                if (tableKey.equals(mapKey + ter)) {
                    list.add(tableKey);
                }
            }
        }

        return list;
    }
}
