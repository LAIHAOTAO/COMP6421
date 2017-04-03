package codegenerate;

import codegenerate.instruction.Instruction;

import java.util.LinkedList;

/**
 * Created by ERIC_LAI on 2017-03-28.
 */
public class CodeGenerateContext {

    public LabelGenerator labelGenerator;
    public RegisterManager registerManager;
    public LinkedList<Instruction> instructions;


    public CodeGenerateContext() {
        labelGenerator = LabelGenerator.instance;
        registerManager = RegisterManager.geRegisterManager();
        instructions = new LinkedList<>();
    }

    public void appendInstruction(Instruction i) {
        this.instructions.add(i);
    }

}
