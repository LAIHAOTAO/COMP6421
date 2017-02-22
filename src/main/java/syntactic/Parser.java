package syntactic;

import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by ERIC_LAI on 2017-02-15.
 */
public class Parser {

    public static boolean parse(LinkedList<String> input, ParserTable table) {
        LinkedList<GrammarRule> stack = new LinkedList<>();
        GrammarRule topRule;
        String inputToken;
        int tokenNum = 1;
        stack.push(table.getDollar());
        stack.push(table.getStart());
        while (!stack.isEmpty() && input.isEmpty()) {
            topRule = stack.peek();
            inputToken = input.peek();

            if (topRule.isTerminal()) {
                if (Objects.equals(topRule.getSymbol(), inputToken)) {
                    stack.pop();
                    input.pop();
                    tokenNum++;
                } else if (!topRule.equals(EpsilonRule.getEpsilonRule()))
                    throw new RuntimeException("\n Parse error at input token " + tokenNum + ": " +
                            "Input token " + inputToken +
                            " does not match with stack token " + topRule.getSymbol() + ".\n " +
                            currentStackString(stack));
                else stack.pop();
            } else {
                LinkedList<GrammarRule> expandedRule
                        = table.get((NonTerminalRule) topRule, inputToken);
                if (expandedRule == null)
                    throw new RuntimeException("\n Parse error at input token " + tokenNum +
                            ": A parse table entry for " + inputToken +
                            " does not exist for grammar rule " + topRule.getSymbol() + ".\n " +
                            currentStackString(stack));
                stack.pop();
                for (int i = expandedRule.size() - 1; i >= 0; i--) {
                    stack.push(expandedRule.get(i));
                }
            }
        }
        return true;
    }

    private static String currentStackString(LinkedList<GrammarRule> list) {
        String ret = "  Current Stack: \n    ";
        for (GrammarRule entry : list) {
            ret += entry.getSymbol() + " \n    ";
        }
        return ret;
    }
}
