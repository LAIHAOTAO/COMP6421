package syntactic;

import common.Const;
import lexical.LexicalScanner;
import lexical.Token;
import semantic.ActionHandler;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.SymbolTableActionHandler;
import util.ErrorFileGenerator;
import util.SymbolTableHelper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by ERIC_LAI on 2017-02-15.
 */
public class Parser {

    public static boolean turnOnDebug = false;
    private static Token inputToken;
    private static Token prevToken;
    private static boolean isSuccess = true;

    public static final int FIRST_PARSE = 1;
    public static final int SECOND_PARSE = 2;

    public static boolean firstParse(LexicalScanner scanner, ParserTable table) throws IOException {
        return parse(scanner, table, FIRST_PARSE);
    }

    public static boolean secondParse(LexicalScanner scanner, ParserTable table) throws IOException {
        scanner.close();
        scanner = new LexicalScanner(scanner.getPath());
        return parse(scanner, table, SECOND_PARSE);
    }


    private static boolean parse(LexicalScanner scanner, ParserTable table, int parseNum) throws IOException {

        DerivationBuilder derivation = null;
        StringBuilder syntacticErrorCollector = new StringBuilder();
        StringBuilder semanticErrorCollector = SymbolTableActionHandler.symActionErrorCollector;

        // ******************************************************************************* //
        if (turnOnDebug) {
            derivation = new DerivationBuilder(scanner.getFileNm());
        }
        // ******************************************************************************* //

        LinkedList<GrammarRule> parsingStack = new LinkedList<>();
        GrammarRule topRule;

        parsingStack.push(table.getDollar());
        parsingStack.push(table.getStart());
        inputToken = scanner.nextToken();

        // create global symbol table and add the the semContext stack
        if (parseNum == FIRST_PARSE) {
            SymbolTable globalTable = new SymbolTable(null);
            globalTable.setName("global table");
            SymbolTableActionHandler.symbolTableList.add(globalTable);
            ActionHandler.symContext.push(globalTable);
        }

        // if the stack is not empty or the input is not empty, just keep parsing
        while (!parsingStack.isEmpty() && inputToken != null) {

            // peek the top rule of the stack
            topRule = parsingStack.peek();

            if (topRule.isAction()) {
                ActionRule actionRule = (ActionRule) parsingStack.pop();
                actionRule.execute(prevToken, parseNum);
            }
            // if the top rule is not action, keep the procedure we did in syntactic
            else {
                // if top rule is a terminal symbol
                if (topRule.isTerminal()) {
                    // if the input token match to the top rule symbol (successfully parse), pop it from the stack and
                    // move to the next token
                    if (Objects.equals(topRule.getSymbol(), inputToken.getType().toString())) {
                        parsingStack.pop();

                        // ******************************************************************************* //
                        if (derivation != null) {
                            derivation.set(inputToken.getValue());
                            derivation.increaseIdx();
                        }
                        // ******************************************************************************* //

                        prevToken = inputToken;
                        inputToken = scanner.nextToken();
                    }
                    // if the top rule cannot match the grammar, throw an error
                    else if (!topRule.equals(EpsilonRule.getEpsilonRule())) {
                        skipError(topRule, parsingStack, scanner, syntacticErrorCollector);
                        isSuccess = false;
                    }
                    // if top rule is epsilon, just pop the top element in the stack
                    else {
                        parsingStack.pop();

                        // ******************************************************************************* //
                        if (derivation != null) {
                            derivation.remove();
                        }
                        // ******************************************************************************* //
                    }
                }
                // if the top rule is a non-terminal symbol
                else {
                    // get the rule from the table and expand that rule
                    LinkedList<GrammarRule> expandedRule
                            = table.get((NonTerminalRule) topRule, inputToken.getType().toString());

                    // if the rule you get from the table is null, throw a exception
                    if (expandedRule == null) {
                        skipError(topRule, parsingStack, scanner, syntacticErrorCollector);
                        isSuccess = false;
                        continue;
                    }
                    // if the rule can be expanded, pop the rule out
                    parsingStack.pop();

                    LinkedList<String> expandedSymbols = new LinkedList<>();

                    for (GrammarRule gr : expandedRule) {
                        expandedSymbols.add(gr.getSymbol());
                    }

                    // ******************************************************************************* //
                    if (derivation != null) {
                        if (!derivation.isEmpty()) derivation.remove();
                        derivation.addAll(expandedSymbols);
                    }
                    // ******************************************************************************* //

                    // do what refer in to ppt as "inverseRHSMultiplePush"
                    for (int i = expandedRule.size() - 1; i >= 0; i--) {
                        parsingStack.push(expandedRule.get(i));
                    }

                }

                // ******************************************************************************* //
                if (turnOnDebug && derivation != null) {
                    derivation.outputDerivation();
                }
                // ******************************************************************************* //
            }
        }

        // output syntactic error to file
        ErrorFileGenerator.outputError(scanner.getFileNm(), syntacticErrorCollector.toString(), Const.ErrorLevel.Syntactic);
        ErrorFileGenerator.outputError(scanner.getFileNm(), semanticErrorCollector.toString(), Const.ErrorLevel.Semantic);

        // ******************************************************************************* //
        if (turnOnDebug && derivation != null) {
            derivation.close();
        }
        if (semanticErrorCollector.toString().isEmpty()) {
            SymbolTableHelper.print();
            SymbolTableHelper.outputToFile(scanner.getFileNm());
        } else {
            System.err.println(semanticErrorCollector.toString());
        }
        // ******************************************************************************* //

        return Parser.isSuccess && ActionHandler.isSuccess;
    }

    private static void skipError(GrammarRule rule, LinkedList<GrammarRule> stack,
                                  LexicalScanner scanner, StringBuilder sb) {
        sb.append("syntax error at line: ").append(inputToken.getLocation())
                .append(", around token: ").append(inputToken.getValue()).append('\n');
        if (rule.isTerminal()) {
            stack.pop();
            return;
        }
        if (isInFollowSet((NonTerminalRule) rule)) {
            stack.pop();
        } else {
            while (!isInFirstSet((NonTerminalRule) stack.peek()) ||
                    (stack.peek().getFirstSet().contains(EpsilonRule.getEpsilonRule())
                            && !isInFollowSet((NonTerminalRule) stack.peek()))) {
                inputToken = scanner.nextToken();
                if (inputToken == null) {
                    return;
                }
            }
        }
    }

    private static boolean isInFirstSet(NonTerminalRule rule) {
        boolean isInFirstSet = false;
        LinkedList<TerminalRule> firstSet = rule.getFirstSet();
        for (TerminalRule ter : firstSet) {
            if (ter.getSymbol().equals(inputToken.getType().toString())) {
                isInFirstSet = true;
                break;
            }
        }
        return isInFirstSet;
    }

    private static boolean isInFollowSet(NonTerminalRule rule) {
        boolean isInFollowSet = false;
        LinkedList<TerminalRule> followSet = rule.getFollowSet();
        for (TerminalRule ter : followSet) {
            if (ter.getSymbol().equals(inputToken.getType().toString())) {
                isInFollowSet = true;
                break;
            }
        }
        return isInFollowSet;
    }

    private static void printLinkedList(LinkedList<String> list) {
        System.out.print("=> ");
        for (String aList : list) {
            System.out.print(aList + " ");
        }
        System.out.println();
    }

    private static String currentStackString(LinkedList<GrammarRule> list) {
        String ret = "Current Stack: ";
        for (int i = list.size() - 1; i >= 0; i--) {
            ret += list.get(i).getSymbol() + " ";
        }
        return ret.trim();
    }

}
