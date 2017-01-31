package common;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ERIC_LAI on 2017-01-18.
 */
public class CommonMethod {

    public static ArrayList<String> getKeyWordsList(String[] keywords) {
        ArrayList<String> list = new ArrayList<String>();
        Collections.addAll(list, keywords);
        return list;
    }

    public static boolean isKeyWord(String word, ArrayList<String> kw) {
        for (String str : kw) {
            if (str.equals(word)) return true;
        }
        return false;
    }
}
