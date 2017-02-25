package syntactic;

import lexical.LexicalScanner;
import lexical.Token;

import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by ERIC_LAI on 2017-02-15.
 */
public class Parser {

    public static boolean parse(LexicalScanner scanner, ParserTable table) {

        LinkedList<GrammarRule> stack = new LinkedList<>();
        GrammarRule topRule;
        Token inputToken;

        stack.push(table.getDollar());
        stack.push(table.getStart());
        inputToken = scanner.nextToken();
        // if the stack is not empty or the input is not empty, just keep parsing
        while (!stack.isEmpty() && inputToken != null) {
            // peek the top rule of the stack
            topRule = stack.peek();

            // if top rule is a terminal symbol
            if (topRule.isTerminal()) {
                // if the input token equal to the top rule symbol, pop it from the stack and
                // move to the next token
                if (Objects.equals(
                        topRule.getSymbol(), inputToken.getType().toString())) {
                    stack.pop();
                    inputToken = scanner.nextToken();
                }
                // if the top rule cannot match the grammar, throw an error
                else if (!topRule.equals(EpsilonRule.getEpsilonRule()))
                    throw new RuntimeException("\n Parse error at input token " +
                            inputToken.getValue() + " does not match with stack token " +
                            topRule.getSymbol() + ".\n " + currentStackString(stack));
                    // if top rule is epsilon, just pop the top element in the stack
                else stack.pop();
            }
            // if the top rule is a non-terminal symbol
            else {
                // get the rule from the table and expand that rule
                LinkedList<GrammarRule> expandedRule
                        = table.get((NonTerminalRule) topRule, inputToken.getType().toString());
                // if the rule you get from the table is null, throw a exception
                if (expandedRule == null)
                    throw new RuntimeException("\n Parse error, " +
                            "A parse table entry for " + inputToken.getValue() +
                            " does not exist for grammar rule " + topRule.getSymbol() + ".\n " +
                            currentStackString(stack));
                // if the rule can be expanded, pop the rule out
                stack.pop();
                // do what refer in to ppt as "inverseRHSMultiplePush"
                for (int i = expandedRule.size() - 1; i >= 0; i--) {
                    stack.push(expandedRule.get(i));
                }
            }
            System.out.println(currentStackString(stack));
        }
        return true;
    }

    private static String currentStackString(LinkedList<GrammarRule> list) {
        String ret = "Current Stack: ";
        for (int i = list.size() - 1; i >= 0; i--) {
            ret += list.get(i).getSymbol() + " ";
        }
        return ret.trim();
    }
}
