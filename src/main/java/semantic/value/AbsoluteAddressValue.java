package semantic.value;

import codegenerate.CodeGenerateContext;
import codegenerate.Register;
import codegenerate.instruction.LWInstruction;

/**
 * Created by ERIC_LAI on 2017-03-31.
 */
public class AbsoluteAddressValue extends DynamicValue {

    // represent the current address value's base address
    private RegisterValue baseAddr;
    // represent the current address value's offset from the base address
    private StaticValue offset;

    public AbsoluteAddressValue(RegisterValue baseAddr, StaticValue offset) {
        this.baseAddr = baseAddr;
        this.offset = offset;
    }

    /**
     * This method is used to calculate the absolute address in runtime
     *   --> absAddr = baseAddr + offset
     */
    @Override
    public Value getUsedValue(CodeGenerateContext context) {

        Register baseAddrReg = context.registerManager.getAvailableRegister(baseAddr.getRegister());
        RegisterValue tmpRegVal = new RegisterValue(baseAddrReg);

        // add instruction to generate code
        context.appendInstruction(new LWInstruction(tmpRegVal, baseAddr, offset));

        return tmpRegVal;
    }

    @Override
    public RegisterValue getRegisterValue(CodeGenerateContext context) {
        return (RegisterValue) getUsedValue(context);
    }

    public RegisterValue getBaseAddr() {
        return baseAddr;
    }

    public StaticValue getOffset() {
        return offset;
    }
}
