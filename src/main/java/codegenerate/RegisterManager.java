package codegenerate;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

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

    private Stack<Register> registers;
    private Set<Register> track;

    private RegisterManager() {
        this.registers = new Stack<>();
        this.track = new HashSet<>();
        this.registers.addAll(Register.freeRegisters);
    }

    public Register getAvailableRegister() {
        Register tmp;
        if (!registers.isEmpty()) {
            tmp = registers.pop();
            track.remove(tmp);
            return tmp;
        }
        throw new RuntimeException("You are run out of register ...");
    }

    public Register getAvailableRegister(Register tmp) {
        if (tmp.reserved) {
            tmp = getAvailableRegister();
        }
        return tmp;
    }

    public void freeRegister(Register reg) {
        this.registers.push(reg);
    }
}
