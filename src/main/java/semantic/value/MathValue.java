package semantic.value;

import codegenerate.CodeGenerateContext;
import codegenerate.MathOpt;
import codegenerate.Register;
import codegenerate.instruction.MathOptImmInstruction;
import codegenerate.instruction.MathOptInstruction;

/**
 * Created by ERIC_LAI on 2017-03-28.
 */
public class MathValue extends DynamicValue {

    private final Value a;
    private final Value b;
    private MathOpt operator;
    private CodeGenerateContext codeContext;

    public MathValue(MathOpt operator, Value first, Value second) {
        this.operator = operator;
        this.a = first;
        this.b = second;
    }

    @Override
    public Value getUsedValue(CodeGenerateContext context) {

        this.codeContext = context;
        Value tmpA = a.getUsedValue(context);
        Value tmpB = b.getUsedValue(context);
        Value res = null;

        if (tmpA instanceof StaticValue && tmpB instanceof StaticNumValue) {
            res = subGetUsedValue((StaticValue) tmpA, (StaticValue) tmpB);
        } else if (tmpA instanceof StaticValue && tmpB instanceof RegisterValue) {
            res = subGetUsedValue((StaticValue) tmpA, (RegisterValue) tmpB);
        } else if (tmpA instanceof RegisterValue && tmpB instanceof StaticValue) {
            res = subGetUsedValue((RegisterValue) tmpA, (StaticValue) tmpB);
        } else if (tmpA instanceof RegisterValue && tmpB instanceof RegisterValue) {
            res = subGetUsedValue((RegisterValue) tmpA, (RegisterValue) tmpB);
        }

        if (res == null)
            throw new RuntimeException("Something wrong inside MathValue getUsedValue()");
        return res;
    }

    @Override
    public RegisterValue getRegisterValue(CodeGenerateContext context) {
        return getUsedValue(context).getRegisterValue(context);
    }

    private Value subGetUsedValue(StaticValue tmpA, StaticValue tmpB) {
        // this case means processing expression like: 1 + 2, 3 + 2, ...
        // we already known the result before we execute the expression,
        // so we can do it in compiler time

        return new StaticNumValue(this.operator.operate(tmpA.intValue(), tmpB.intValue()));
    }

    private Value subGetUsedValue(StaticValue tmpA, RegisterValue tmpB) {
        // if the operation respect commutative law
        if (this.operator.commulative) {
            return subGetUsedValue(tmpB, tmpA);
        }
        // otherwise case the first parameter to RegisterValue
        else {
            return subGetUsedValue(tmpA.getRegisterValue(this.codeContext), tmpB);
        }
    }

    private Value subGetUsedValue(RegisterValue tmpA, StaticValue tmpB) {
        // this case we need to use immediate operation

        // apply an free register to stored the result
        Register reg = tmpA.getRegister();
        // make sure the reg is not occupied by others
        Register tmp = this.codeContext.registerManager.getAvailableRegister(reg);

        this.codeContext.appendInstruction(
                new MathOptImmInstruction(
                        this.operator.immediateOpcode,
                        tmp,
                        reg,
                        tmpB.intValue()
                )
        );

        return new RegisterValue(tmp);
    }

    private Value subGetUsedValue(RegisterValue tmpA, RegisterValue tmpB) {
        Register regA = tmpA.getRegister();
        Register regB = tmpB.getRegister();

        Register tmp;
        boolean free = false;
        if (regA.reserved) {
            tmp = this.codeContext.registerManager.getAvailableRegister(regB);
        } else {
            tmp = regA;
            if (!regB.reserved) {
                free = true;
            }
        }

        this.codeContext.appendInstruction(new MathOptInstruction(operator.opcode, tmp, regA, regB));

        if (free) {
            this.codeContext.registerManager.freeRegister(regB);
        }

        return new RegisterValue(tmp);
    }


}
