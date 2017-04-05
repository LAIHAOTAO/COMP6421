package codegenerate;

import common.Const;
import lexical.LexicalScanner;
import org.junit.Test;
import semantic.handler.SemanticActionHandler;
import syntactic.Parser;
import syntactic.ParserDriver;

import java.io.IOException;

/**
 * Created by ERIC_LAI on 2017-04-02.
 */
@SuppressWarnings("Duplicates")
public class AssignmentStatementTest {

    @Test
    public void testOnlyProgram() {
        test(Const.DIR_RES + "codegenerate/OnlyProgram.txt");
    }

    @Test
    public void testUsingClassInProgram() {
        test(Const.DIR_RES + "codegenerate/ClassAssignment.txt");
    }

    @Test
    public void testFreeFunctionWithParameter() {
        test(Const.DIR_RES + "codegenerate/FreeFuncAssignment.txt");
    }

    @Test
    public void testFreeFunctionWithClassParameter() {
        test(Const.DIR_RES + "codegenerate/FreeFunctionWithClassParameter.txt");
    }

    private void test(String sourceFilePath) {
        LexicalScanner scanner = new LexicalScanner(sourceFilePath);

        ParserDriver parserDriver;
        try {

            parserDriver = new ParserDriver();
            Parser.turnOnDebug();
            SemanticActionHandler.turnOnDebug();

            Parser.firstParse(scanner, parserDriver.getTable());
            boolean isSuccess = Parser.secondParse(scanner, parserDriver.getTable());
            System.out.println("parsing result: " + ((isSuccess) ? "success" : "fail"));

            CodeGenerator generator = new CodeGenerator();
            generator.setDebug(true);
            generator.generate();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
