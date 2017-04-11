package lexicaltest;

import common.Const;
import lexical.LexicalScanner;
import lexical.Token;
import lexical.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

/**
 * Created by ERIC_LAI on 2017-01-18.
 */
public class LexicalScannerTest {

    @Test
    public void testValidProgram() {
        createTest("ValidProgram");
    }

    @Test
    public void testIdentifier() {
        createTest("Identifier");
    }

    @Test
    public void testNum() {
        createTest("Number");
    }

    @Test
    public void testKeyword() {
        createTest("Keyword");
    }

    @Test
    public void testOandD() {
        createTest("OptorAndDemter");
    }

    @Test
    public void testComment() {
        createTest("Comment");
    }

    @Test
    public void test() {
        createTest("Case");
    }

    private void createTest(String testFileNm) {
        String resultFileNm = testFileNm + "Result";
        String prefix = Const.DIR_RES + "lexical/Lexical";
        String suffix = ".txt";
        String src = prefix + testFileNm + suffix;
        String target = prefix + resultFileNm + suffix;
        compareTwoFiles(src, target, testFileNm);
    }

    private String readLine(BufferedReader br) {
        if (br != null) {
            try {
                return br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean compareTwoFiles(String src, String target, String fileNm) {
        String errFile = Const.DIR_OUTPUT + "lexical/err" + "Lexical" + fileNm + "Err.txt";
        String outFile = Const.DIR_OUTPUT + "lexical/token" + "Lexical" + fileNm + "Out.txt";

        BufferedReader br = null;
        BufferedWriter bwErr = null;
        BufferedWriter bwValid = null;
        try {
            br = new BufferedReader(new FileReader(target));
            bwErr = new BufferedWriter(new FileWriter(errFile));
            bwValid = new BufferedWriter(new FileWriter(outFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LexicalScanner scanner = new LexicalScanner(src);
        Token token = scanner.nextToken();
        while (token != null) {
            String str = token.toString();
            if (token.getType() == TokenType.LEXICAL_ERR) {
                try {
                    if (bwErr != null) {
                        bwErr.write(str + "\n");
                        bwErr.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (bwValid != null) {
                    bwValid.write(str + '\n');
                    bwValid.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String line = readLine(br);
            Assert.assertEquals(str, line);
            token = scanner.nextToken();
        }
        return true;
    }
}
