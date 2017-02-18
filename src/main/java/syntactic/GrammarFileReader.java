package syntactic;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ERIC_LAI on 2017-02-17.
 */
public class GrammarFileReader {

    private BufferedReader br;
    private List<String> nonTerminalRuleSymbols;
    private List<String> terminalRuleSymbols;
    private HashMap<String, String> map;

    public GrammarFileReader(String path) throws IOException {
        File file = new File(path);
        br = new BufferedReader(new FileReader(file));
        this.nonTerminalRuleSymbols = new ArrayList<>();
        this.terminalRuleSymbols = new ArrayList<>();
        this.map = new HashMap<>();
        constructRules();
    }


    public String[] getNonTerminalRuleSymbols() {
        String[] strings = new String[nonTerminalRuleSymbols.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = nonTerminalRuleSymbols.get(i);
        }
        return strings;
    }

    public String[] getTerminalRuleSymbols() {
        String[] strings = new String[terminalRuleSymbols.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = terminalRuleSymbols.get(i);
        }
        return strings;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    private void constructRules() throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            parse(line);
        }
    }

    private void parse(String line) {
        String[] arr = line.split(":");
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
                    if (s.contains("<"))
                        nonTerminalRuleSymbols.add(s.trim());
                    else if (!s.equals("|"))
                        terminalRuleSymbols.add(s);
                }
            }
        }
    }
}
