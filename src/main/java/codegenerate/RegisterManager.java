package codegenerate;

import exception.CompilerException;

import java.util.*;

/**
 * Created by ERIC_LAI on 2017-03-28.
 */
public class RegisterManager {

    private static RegisterManager instance;

    public static RegisterManager geRegisterManager() {
        if (instance == null) {
            return new RegisterManager();
        }
        return instance;
    }

    private Queue<Register> registers;
    private Set<Register> track;

    private RegisterManager() {
        this.registers = new ArrayDeque<>();
        this.track = new HashSet<>();
        this.registers.addAll(Register.freeRegisters);
    }

    public Register getAvailableRegister() {
        Register tmp;
        if (!registers.isEmpty()) {
            tmp = registers.remove();
            track.remove(tmp);
            return tmp;
        }
        throw new CompilerException("You are run out of register ...");
    }

    /**
     * This method is used to detect the current register connected to the value is
     * reversed or not. Since a lot of value depend on the stack pointer register or
     * the frame pointer when they are initialized. This method can give this value a
     * free register when they need to participate in some expression calculation.
     */
    public Register getAvailableRegister(Register tmp) {
        if (tmp.reserved) {
            tmp = getAvailableRegister();
        }
        return tmp;
    }

    public void freeRegister(Register reg) {
        this.registers.add(reg);
    }
}
