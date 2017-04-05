package semantic;

import common.Const;
import lexical.LexicalScanner;
import org.junit.Test;
import syntactic.Parser;
import syntactic.ParserDriver;

import java.io.IOException;

/**
 * Created by ERIC_LAI on 2017-03-23.
 */
public class SemanticMigrationTest {


    @Test
    public void test() {
        LexicalScanner scanner = new LexicalScanner(Const.DIR_RES + "semantic/MigrationTest.txt");

        ParserDriver parserDriver;
        try {

            parserDriver = new ParserDriver();
            Parser.turnOnDebug();

            Parser.firstParse(scanner, parserDriver.getTable());
            boolean isSuccess = Parser.secondParse(scanner, parserDriver.getTable());

            System.out.println("parsing result: " + ((isSuccess) ? "success" : "fail"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
