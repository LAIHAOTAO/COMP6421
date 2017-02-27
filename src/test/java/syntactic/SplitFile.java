package syntactic;

import common.Const;

import java.io.*;

/**
 * Split the test file into several test cases
 *
 * Created by ERIC_LAI on 2017-02-27.
 */
public class SplitFile {

    public static void main(String[] args) {
        String input = Const.DIR_RES + "/syntactic/grammarTest.txt";
        File file = new File(input);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String prefix = Const.DIR_RES + "/syntactic/testprogram/program";
            String surfix = ".txt";
            int lineNum = 1;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    String path = prefix + (lineNum++) + surfix;
                    FileWriter writer = new FileWriter(new File(path));
                    writer.write(line);
                    writer.close();
                }
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
