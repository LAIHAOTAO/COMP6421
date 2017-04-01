package codegenerate;

import common.Const;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ERIC_LAI on 2017-03-28.
 */
public enum  Register {

    ZERO("R0"),
    STACK_POINTER("R1"),
    FRAME_POINTER("R2"),
    R3,
    R4,
    R5,
    R6,
    R7,
    R8,
    R9,
    R10,
    R11,
    R12,
    R13,
    RETURN("R14"),
    PC("R15"),

    ;

    static Set<Register> freeRegisters = new HashSet<>(values().length - Const.REVERSED_REG_NUM);

    static {
        for (Register reg : values()) {
            if (!reg.reserved) {
                freeRegisters.add(reg);
            }
        }
    }

    // use for output code
    public final String alias;
    // R0, R14, R15 are reversed
    public boolean reserved;

    private final String name;

    Register() {
        this.name = this.toString();
        this.alias = this.toString();
        this.reserved = false;
    }

    Register(String a) {
        this.name = this.toString();
        this.alias = a;
        this.reserved = true;
    }


}
