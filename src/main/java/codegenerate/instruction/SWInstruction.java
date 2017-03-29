package codegenerate.instruction;

import codegenerate.Register;

/**
 * Created by ERIC_LAI on 2017-03-28.
 */
public class SWInstruction extends Instruction{

    private final Register destAddr;
    private final Register value;
    private final int offset;

    public SWInstruction(Register destAddr, Register value, int offset) {
        this.destAddr = destAddr;
        this.value = value;
        this.offset = offset;
    }


    @Override
    protected String generateAssemblyCode() {
        // according to the moon doc
        //   store word: sw  K(Rj), Ri => M_32[Rj + K] <-- Ri
        return "sw" + "\t" + offset + "(" + destAddr.alias + "), " + value.alias;
    }
}
