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
public class StatementTest {

    @Test
    public void testFunctionParameter() throws IOException {
        test(Const.DIR_RES + "semantic/IncorrectFunctionParameter.txt");
    }

    @Test
    public void testReadAndWrite() {
        // expect output: one character you input
        test(Const.DIR_RES + "codegenerate/ReadAndWrite.txt");
    }

    @Test
    public void testOnlyProgram() {
        // expect output 'F'
        test(Const.DIR_RES + "codegenerate/OnlyProgram.txt");
    }

    @Test
    public void testUsingClassInProgram() {
        // expect output 'C'
        test(Const.DIR_RES + "codegenerate/ClassAssignment.txt");
    }

    @Test
    public void testFreeFunctionWithParameter() {
        // expect output 'A'
        test(Const.DIR_RES + "codegenerate/FreeFuncAssignment.txt");
    }

    @Test
    public void testFreeFunctionWithClassParameter() {
        // expect output 'C'
        test(Const.DIR_RES + "codegenerate/FreeFunctionWithClassParameter.txt");
    }

    @Test
    public void testIfStatement() {
        // expect output 'B'
        test(Const.DIR_RES + "codegenerate/IfStatement.txt");
    }

    @Test
    public void testForStatement() {
        // expect output 'D'
        test(Const.DIR_RES + "codegenerate/ForStatement.txt");
    }

    @Test
    public void testIndexing() {
        // expect output 'B'
        test(Const.DIR_RES + "codegenerate/SimpleArray.txt");
    }

    @Test
    public void testRecursion() {
        // expect output 'A'
        test(Const.DIR_RES + "codegenerate/Recursion.txt");
    }

    // didn't finish yet !!!
    @Test
    public void testMemberFunction() {
        test(Const.DIR_RES + "codegenerate/MemberFunction.txt");
    }

    private void test(String sourceFilePath) {
        LexicalScanner scanner = new LexicalScanner(sourceFilePath);

        ParserDriver parserDriver;
        try {

            parserDriver = new ParserDriver();
            Parser.turnOnDebug();
//            SemanticActionHandler.turnOnDebug();

            Parser.firstParse(scanner, parserDriver.getTable());
            boolean isSuccess = Parser.secondParse(scanner, parserDriver.getTable());
            System.out.println("parsing result: " + ((isSuccess) ? "success" : "fail"));

            CodeGenerator generator = new CodeGenerator(scanner.getFileNm());
            generator.setDebug(true);
            generator.generate();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
