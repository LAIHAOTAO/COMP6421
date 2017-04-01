package codegenerate;

import codegenerate.instruction.AlignInstruction;
import codegenerate.instruction.EntryInstruction;
import codegenerate.instruction.Instruction;
import codegenerate.instruction.MathOptImmInstruction;
import semantic.Statement.Statement;
import semantic.handler.SymbolTableActionHandler;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.entry.FunctionAbstractEntry;

/**
 * Created by ERIC_LAI on 2017-03-31.
 */
public class CodeGenerator {


    public void generateEntry() {

        CodeGenerateContext context = new CodeGenerateContext();
        SymbolTable table = SymbolTableActionHandler.getSymbolTableByName("global");
        FunctionAbstractEntry programEntry = (FunctionAbstractEntry) table.search("program");

        String stackLabel = "stack";

        context.appendInstruction(new EntryInstruction());

        // todo need code to setup the stack pointer to the highest address
        
        Instruction setAlign = new MathOptImmInstruction(
                MathOpt.ADD.immediateOpcode,
                Register.STACK_POINTER,
                Register.ZERO,
                stackLabel
        );
        Instruction initStackPointer = new MathOptImmInstruction(
                MathOpt.ADD.immediateOpcode,
                Register.STACK_POINTER,
                Register.ZERO,
                programEntry.getSize()
        );

        context.appendInstruction(setAlign);
        context.appendInstruction(initStackPointer);

        for (Statement statement : programEntry.getStatementList()) {
            statement.generateCode(context);
        }

        Instruction alignInstruction = new AlignInstruction();
        alignInstruction.setLabel(stackLabel);
        context.appendInstruction(alignInstruction);

        // todo output the code to file
    }

    public void printInstruction() {

    }
}
