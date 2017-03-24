package semantic;

import common.Const;
import lexical.LexicalScanner;
import org.junit.Test;
import syntactic.*;
import syntactic.ParserDriver;

import java.io.IOException;

/**
 * Created by ERIC_LAI on 2017-02-15.
 */
@SuppressWarnings("Duplicates")
public class IncorrectSemanticTest {

    @Test
    public void testProgram() throws IOException {

        LexicalScanner scanner = new LexicalScanner(Const.DIR_RES + "semantic/IncorrectProgram.txt");

        ParserDriver parserDriver = new ParserDriver();
        Parser.turnOnDebug = true;

        boolean isSuccess = Parser.firstParse(scanner, parserDriver.getTable());
        System.out.println("parsing result: " + ((isSuccess)? "success" : "fail"));
    }


}
