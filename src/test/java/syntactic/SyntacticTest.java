package syntactic;

import common.Const;
import lexical.LexicalScanner;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by ERIC_LAI on 2017-04-09.
 */
public class SyntacticTest {

    @Test
    public void testWithoutBracket() {
        test(Const.DIR_RES + "syntactic/StatementWithoutBracket.txt");
    }

    private void test(String sourceFilePath) {
        LexicalScanner scanner = new LexicalScanner(sourceFilePath);

        ParserDriver parserDriver;
        try {

            parserDriver = new ParserDriver();
            Parser.turnOnDebug();

            boolean isSuccess = Parser.firstParse(scanner, parserDriver.getTable());
            System.out.println("parsing result: " + ((isSuccess) ? "success" : "fail"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
