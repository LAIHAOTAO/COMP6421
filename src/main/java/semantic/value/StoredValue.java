package semantic.value;

import codegenerate.CodeGenerateContext;
import codegenerate.MathOpt;
import codegenerate.Register;
import codegenerate.instruction.MathOptInstruction;

/**
 * Created by ERIC_LAI on 2017-03-31.
 */
public class StoredValue extends DynamicValue {

    private final Value baseAddr;
    private final Value offset;

    public StoredValue(Value baseAddr, Value offset) {
        this.baseAddr = baseAddr;
        this.offset = offset;
    }

    @Override
    public Value getUsedValue(CodeGenerateContext context) {
        return getAbsAddress(context).getUsedValue(context);
    }

    @Override
    public RegisterValue getRegisterValue(CodeGenerateContext context) {
        return getAbsAddress(context).getRegisterValue(context);
    }

    public AbsoluteAddressValue getAbsAddress(CodeGenerateContext context) {
        Value usedOffset = this.offset.getUsedValue(context);
        RegisterValue baseAddrRegVal = this.baseAddr.getRegisterValue(context);

        if (usedOffset instanceof StaticValue) {
            return new AbsoluteAddressValue(baseAddrRegVal, (StaticValue) usedOffset);

        } else if (usedOffset instanceof RegisterValue) {
            Register reg = ((RegisterValue) usedOffset).getRegister();
            reg = context.registerManager.getAvailableRegister(reg);

            RegisterValue regVal = new RegisterValue(reg);
            context.appendInstruction(new MathOptInstruction(
                    // get absolute address using frame pointer as reference
                    MathOpt.SUBTRACT.opcode, regVal, baseAddrRegVal, (RegisterValue) usedOffset)
            );

            // release the register when this value is stored
            Register usedOffsetReg = ((RegisterValue) usedOffset).getRegister();
            if (!usedOffsetReg.reserved) {
                context.registerManager.freeRegister(usedOffsetReg);
            }

            return new AbsoluteAddressValue(regVal, new StaticNumValue(0));
        } else
            throw new RuntimeException("Something wrong inside StoredValue getAbsAddress method");
    }
}
