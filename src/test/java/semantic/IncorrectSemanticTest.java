package semantic;

import common.Const;
import lexical.LexicalScanner;
import org.junit.Test;
import semantic.handler.SemanticActionHandler;
import syntactic.*;
import syntactic.ParserDriver;

import java.io.IOException;

/**
 * Created by ERIC_LAI on 2017-02-15.
 */
@SuppressWarnings("Duplicates")
public class IncorrectSemanticTest {

    @Test
    public void testTypeCheckingReturn() throws IOException {
        test("semantic/TypeCheckingReturn.txt");
    }

    @Test
    public void testTypeCheckingAssignment() throws IOException {
        test("semantic/TypeCheckingAssignment.txt");
    }

    @Test
    public void testIncorrectArrayDimension() throws IOException {
        test("semantic/IncorrectArrayDimension.txt");
    }

    @Test
    public void testUndefineMember() throws IOException {
        test("semantic/UndefinedClassMember.txt");
    }

    @Test
    public void testUndefineFunction() throws IOException {
        test("semantic/UndefinedFunction.txt");
    }

    @Test
    public void testUndefineClass() throws IOException {
        test("semantic/UndefinedClass.txt");
    }

    @Test
    public void testDuplicate() throws IOException {
        test("semantic/IncorrectProgram.txt");
    }

    private void test(String path) throws IOException {
        LexicalScanner scanner = new LexicalScanner(Const.DIR_RES + path);

        ParserDriver parserDriver = new ParserDriver();
        Parser.turnOnDebug();
//        SemanticActionHandler.turnOnDebug();

        Parser.firstParse(scanner, parserDriver.getTable());
        boolean isSuccess = Parser.secondParse(scanner, parserDriver.getTable());
        System.out.println("parsing result: " + ((isSuccess)? "success" : "fail"));
    }


}
