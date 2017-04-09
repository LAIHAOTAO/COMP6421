package semantic.expression;

import codegenerate.CodeGenerateContext;
import semantic.statement.Statement;
import semantic.value.Value;
import semantic.value.VoidValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ERIC_LAI on 2017-04-05.
 */
public class StatementBlockElement extends ExpressionElement implements Statement {

    private List<Statement> statements;

    public StatementBlockElement() {
        statements = new ArrayList<>();
    }

    @Override
    public void accept(ExpressionElement expr) {
        if (expr instanceof Statement) {
            statements.add((Statement) expr);
        } else {
            super.accept(expr);
        }
    }

    @Override
    public void generateCode(CodeGenerateContext c) {
        for (Statement s : statements) {
            s.generateCode(c);
        }
    }

    @Override
    public Value getValue() {
        return VoidValue.get();
    }
}
