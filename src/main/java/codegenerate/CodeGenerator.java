package codegenerate;

import codegenerate.instruction.*;
import common.Const;
import semantic.Statement.Statement;
import semantic.handler.SymbolTableActionHandler;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.entry.FunctionAbstractEntry;
import semantic.symboltable.entry.SymbolTableEntry;

/**
 * Created by ERIC_LAI on 2017-03-31.
 */
public class CodeGenerator {

    private boolean debug = false;

    public CodeGenerator() {
    }

    public void generate() {

        if (debug) {
            System.out.println("==========================================================");
            System.out.println("Code Generation");
            System.out.println("==========================================================");
        }
        generateFunctions();
        generateProgram();
    }

    private void generateFunctions() {
        SymbolTable global = SymbolTableActionHandler.getSymbolTableByName("global");

        for (SymbolTableEntry entry : global.valueSet()) {
            if (entry instanceof FunctionAbstractEntry && !entry.getName().equals("program")) {
                generate((FunctionAbstractEntry) entry);
            }
        }
    }

    private void generateProgram() {
        CodeGenerateContext context = new CodeGenerateContext();
        SymbolTable table = SymbolTableActionHandler.getSymbolTableByName("global");
        FunctionAbstractEntry programEntry = (FunctionAbstractEntry) table.search("program");

        context.appendInstruction(new EntryInstruction().setComment("======program entry======"));
        context.appendInstruction(new AlignInstruction().setComment("following instruction align"));

        // initialize the stack pointer and frame
        // pointer (the highest address of the memory)
        context.appendInstruction(new MathOptImmInstruction(
                MathOpt.ADD.immediateOpcode,
                Register.STACK_POINTER,
                Register.ZERO,
                "topaddr"
        ).setComment("initialize the stack pointer"));
        context.appendInstruction(new MathOptImmInstruction(
                MathOpt.ADD.immediateOpcode,
                Register.FRAME_POINTER,
                Register.ZERO,
                "topaddr"
        ).setComment("initialize the frame pointer"));

        // move stack pointer to the top of the stack
        context.appendInstruction(new MathOptImmInstruction(
                MathOpt.SUBTRACT.immediateOpcode,
                Register.STACK_POINTER,
                Register.STACK_POINTER,
                programEntry.getSize()
        ).setComment("set the stack pointer to the top position of the stack"));

        // add program statement instruction into the list
        for (Statement statement : programEntry.getStatementList()) {
            statement.generateCode(context);
        }

        context.appendInstruction(new HltInstruction().setComment("======end of program======"));

        // output all instructions
        for (Instruction instruction : context.instructions) {
            // todo output the code to file
            if (debug) System.out.println(instruction);
        }

    }

    private void generate(FunctionAbstractEntry entry) {
        CodeGenerateContext context = new CodeGenerateContext();

        // store the return address
        context.appendInstruction(new SWInstruction(4, Register.FRAME_POINTER, Register.RETURN)
                .setComment("store return address").setLabel(entry.getLabel()));

        for (Statement statement : entry.getStatementList()) {
            statement.generateCode(context);
        }

        // get the return address
        Register tmp = context.registerManager.getAvailableRegister();
        context.appendInstruction(new LWInstruction(tmp, Register.FRAME_POINTER, 4)
                .setComment("get return address"));

        // reset the stack pointer and frame pointer
        context.appendInstruction(new LWInstruction(Register.FRAME_POINTER, Register.FRAME_POINTER, 0)
                .setComment("load the previous frame pointer address"));
        context.appendInstruction(new MathOptImmInstruction(
                MathOpt.ADD.immediateOpcode, Register.STACK_POINTER,
                Register.STACK_POINTER, entry.getScope().getSize())
                .setComment("reset the stack pointer"));

        // jump back to the calling place
        context.appendInstruction(new JumpAndLinkInstruction(tmp));
        context.registerManager.freeRegister(tmp);

        // output all instructions
        for (Instruction instruction : context.instructions) {
            // todo output the code to file
            if (debug) System.out.println(instruction);
        }
    }

    public void printInstruction() {

    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
