package codegenerate;

import common.Const;
import lexical.LexicalScanner;
import org.junit.Test;
import syntactic.Parser;
import syntactic.ParserDriver;

import java.io.IOException;

/**
 * Created by ERIC_LAI on 2017-04-02.
 */
@SuppressWarnings("Duplicates")
public class AssignmentStatementTest {

    @Test
    public void test1() {
        LexicalScanner scanner = new LexicalScanner(Const.DIR_RES + "codegenerate/OnlyProgram.txt");

        ParserDriver parserDriver;
        try {

            parserDriver = new ParserDriver();
            Parser.turnOnDebug = false;

            Parser.firstParse(scanner, parserDriver.getTable());
            boolean isSuccess = Parser.secondParse(scanner, parserDriver.getTable());
            System.out.println("parsing result: " + ((isSuccess) ? "success" : "fail"));

            CodeGenerateContext codeContext = new CodeGenerateContext();
            CodeGenerator generator = new CodeGenerator();
            generator.setDebug(true);
            generator.generate();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        LexicalScanner scanner = new LexicalScanner(Const.DIR_RES + "codegenerate/ClassAssignment.txt");

        ParserDriver parserDriver;
        try {

            parserDriver = new ParserDriver();
            Parser.turnOnDebug = false;

            Parser.firstParse(scanner, parserDriver.getTable());
            boolean isSuccess = Parser.secondParse(scanner, parserDriver.getTable());
            System.out.println("parsing result: " + ((isSuccess) ? "success" : "fail"));

            CodeGenerateContext codeContext = new CodeGenerateContext();
            CodeGenerator generator = new CodeGenerator();
            generator.setDebug(true);
            generator.generate();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        LexicalScanner scanner = new LexicalScanner(Const.DIR_RES + "codegenerate/FreeFuncAssignment.txt");

        ParserDriver parserDriver;
        try {

            parserDriver = new ParserDriver();
            Parser.turnOnDebug = false;

            Parser.firstParse(scanner, parserDriver.getTable());
            boolean isSuccess = Parser.secondParse(scanner, parserDriver.getTable());
            System.out.println("parsing result: " + ((isSuccess) ? "success" : "fail"));

            CodeGenerateContext codeContext = new CodeGenerateContext();
            CodeGenerator generator = new CodeGenerator();
            generator.setDebug(true);
            generator.generate();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
