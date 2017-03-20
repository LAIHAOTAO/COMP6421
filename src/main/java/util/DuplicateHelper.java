package util;

import semantic.SymbolTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by ERIC_LAI on 2017-03-20.
 */
public class DuplicateHelper {

    public static String getNewName (SymbolTable table, String name) {
        Set<String> tableKeySet = table.keySet();
        String curNm = getLongestName(getRelativeNameList(tableKeySet, name));
        return curNm + "#";
    }

    private static String getLongestName(List<String> nameList) {
        String res = "";
        for (String name : nameList) {
            if (name.length() > res.length()) {
                res = name;
            }
        }
        return res;
    }

    private static List<String> getRelativeNameList(Set<String> keySet, String name) {
        List<String> res = new ArrayList<>();
        for (String keyNmae : keySet) {
            if (match(keyNmae, name)) {
                res.add(keyNmae);
            }
        }
        return res;
    }

    private static boolean match(String target, String source) {
        if (Objects.equals(target, source)) return true;
        if (target.contains(source)) {
            char[] sub = target.substring(source.length()).toCharArray();
            for (char ch : sub) {
                if (ch != '#') return false;
            }
        }
        return true;
    }
}
