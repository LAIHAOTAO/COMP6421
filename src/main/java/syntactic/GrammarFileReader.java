package syntactic;

import common.Language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This class is used to read grammar from a file to get the Terminal and the NonTerminal symbols
 * The only thing you need to do is pass the path of the grammar file to the constructor.
 *
 * @author Haotao Lai (haotao.lai@gmail.com) on 2017-02-17
 */
public class GrammarFileReader {

    private BufferedReader br;
    private List<String> nonTerminalRuleSymbols;
    private List<String> terminalRuleSymbols;
    private List<String> actionRuleSymbols;
    private HashMap<String, String> map;
    private HashSet<String> terminalSet;

    // constructor, initialize everything
    public GrammarFileReader(String path) throws IOException {
        File file = new File(path);
        br = new BufferedReader(new FileReader(file));
        this.nonTerminalRuleSymbols = new ArrayList<>();
        this.terminalRuleSymbols = new ArrayList<>();
        this.actionRuleSymbols = new ArrayList<>();
        this.map = new HashMap<>();
        terminalSet = new HashSet<>();
        Collections.addAll(terminalSet, Language.TERMINALS);
        constructRules();
    }

    // get all NonTerminal in a string array
    public String[] getNonTerminalRuleSymbols() {
        String[] strings = new String[nonTerminalRuleSymbols.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = nonTerminalRuleSymbols.get(i);
        }
        return strings;
    }

    // get all Terminal symbol in a string array
    public String[] getTerminalRuleSymbols() {
        String[] strings = new String[terminalRuleSymbols.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = terminalRuleSymbols.get(i);
        }
        return strings;
    }

    // get action symbol in a string array
    public String[] getActionRuleSymbols() {
        String[] strings = new String[actionRuleSymbols.size()];
        for (int i= 0; i < strings.length; i++) {
            strings[i] = actionRuleSymbols.get(i);
        }
        return strings;
    }

    // the get rule map
    public HashMap<String, String> getMap() {
        return map;
    }
    // end of public methods

    // three private methods to read grammar from file and parse it and build the grammar rule
    private void constructRules() throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.isEmpty()) {
                parse(line);
            }
        }
    }

    private void parse(String line) {
        String[] arr = line.split("->");
        String left = arr[0].trim();
        String[] right = arr[1].split("\\|");
        nonTerminalRuleSymbols.add(left.trim());
        parse(right);
        if (!map.containsKey(left) && !left.isEmpty()) {
            map.put(left, arr[1]);
        }
    }

    private void parse(String[] right) {
        for (String str : right) {
            if (!str.isEmpty()) {
                String[] grammarRules = str.trim().split(" ");
                for (String s : grammarRules) {
                    if (!actionRuleSymbols.contains(s.trim()) && (s.contains("sym") || s.contains("sem"))) {
                        actionRuleSymbols.add(s.trim());
                    } else if (!"EPSILON".equals(s) && !terminalSet.contains(s)) {
                        nonTerminalRuleSymbols.add(s.trim());
                    } else if (!s.equals("|") && !terminalRuleSymbols.contains(s)) {
                        terminalRuleSymbols.add(s);
                    }
                }
            }
        }
    }
    // end of private method

} // end of file