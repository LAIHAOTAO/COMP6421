package syntactic;

import java.util.LinkedList;
import java.util.Objects;

/**
 * Created by ERIC_LAI on 2017-02-15.
 */
public class NonTerminalRule extends GrammarRule {

    private LinkedList<ProductionRule> rules;
    private LinkedList<TerminalRule> followSet;
    private PrimeNumber primeNum;

    public NonTerminalRule(String sym) {
        this(sym, 0);
    }

    public NonTerminalRule(String sym, PrimeNumber primeNum) {
        this.symbol = sym;
        this.primeNum = primeNum;
        this.rules = new LinkedList<>();
        this.followSet = new LinkedList<>();
    }

    public void setRules(LinkedList<ProductionRule> rules) {
        this.rules = rules;
    }

    public void setPrimeNum(PrimeNumber primeNum) {
        this.primeNum = primeNum;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public LinkedList<TerminalRule> getFirstSet() {
        LinkedList<TerminalRule> first = new LinkedList<>();
        for (ProductionRule pr : this.rules) {
            first.addAll(pr.getFirstSet());
        }
        return first;
    }

    @Override
    public LinkedList<TerminalRule> getFollowSet() {
        return null;
    }

    @Override
    public String toString() {
        return this.symbol;
    }

    public String toDetailString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.symbol).append(" -> ");
        for (ProductionRule p : rules) {
//            if (!p.toString().isEmpty())
            sb.append(p.toString()).append(" | ");
        }
        String str = sb.toString().trim();
        int idx = str.lastIndexOf("|");
        if (idx == -1) return str;
        return str.substring(0, idx).trim();
    }

    public NonTerminalRule removeLeftRecursion() {
        LinkedList<ProductionRule> nonLeftRecursion = new LinkedList<>();
        LinkedList<ProductionRule> recursion = new LinkedList<>();
        // separate non-recursion and recursion symbols
        for (ProductionRule rule : this.rules) {
            if (Objects.equals(rule.get(0).symbol, this.symbol)) recursion.add(rule);
            else nonLeftRecursion.add(rule);
        }
        if (recursion.isEmpty()) return null;
        NonTerminalRule rulePrime =
                new NonTerminalRule(primeString(this.symbol, primeNum.getNum() + 1));
        // add rulePrime to non-recursion rule
        // i.e. A -> Aa | b, for (A -> b)  ==>  (A -> bA')
        for (ProductionRule r : nonLeftRecursion) {
            if (!Objects.equals(r.getFirst(), EpsilonRule.getEpsilonRule()))
                r.addLast(rulePrime);
        }
        // remove recursive rule and add rulePrime
        // i.e. A -> Aa | b, for (A -> Aa)  ==>  (A -> aA')
        for (ProductionRule r : recursion) {
            r.removeFirst();
            r.addLast(rulePrime);
        }
        // update the rules in both current object and rulePrime
        ProductionRule epsilon = new ProductionRule(EpsilonRule.getEpsilonList());
        recursion.add(epsilon);
        rulePrime.setRules(recursion);
        this.setRules(nonLeftRecursion);
        this.primeNum.increase();
        rulePrime.setPrimeNum(this.primeNum);
        return rulePrime;
    }

    public LinkedList<NonTerminalRule> removeLeftFactoring() {
        ProductionRule prod1, prod2, newProductionRule;
        NonTerminalRule newRule;
        LinkedList<ProductionRule> newProductionRules;
        LinkedList<NonTerminalRule> newGrammarRules = new LinkedList<>();
        LinkedList<ProductionRule> sharedPrefixList;
        // Loop through each combination of prod1 and prod2 and correct them if they have the
        // same prefix
        for (int i = 0; i < this.rules.size(); i++) {
            int longestSub = 0;
            int oldLongestSub = 1;
            int samePrefixNum = 0;
            // create new production rule list for this grammar rule
            newProductionRules = new LinkedList<>();
            sharedPrefixList = new LinkedList<>();
            prod1 = this.rules.get(i);
            // Find the longest common subsequence with prod1 and the other production rules (prod2)
            for (int j = i + 1; j < this.rules.size(); j++) {
                prod2 = this.rules.get(j);
                longestSub = this.countSamePrefix(prod1, prod2);
                if (longestSub > oldLongestSub) {
                    newProductionRules.addAll(sharedPrefixList);
                    sharedPrefixList.clear();
                    sharedPrefixList.add(prod2);
                    oldLongestSub = longestSub;
                } else if (longestSub == oldLongestSub) {
                    sharedPrefixList.add(prod2);
                } else {
                    newProductionRules.add(prod2);
                }
                if (longestSub > samePrefixNum) samePrefixNum = longestSub;
            }
            sharedPrefixList.add(prod1);
            // Modifying this grammar rule and creating another to adjust for common prefix
            if (samePrefixNum > 0) {
                // increment the prime count
                this.primeNum.increase();
                // create new rule
                newRule = new NonTerminalRule(
                        this.primeString(this.symbol, this.primeNum.getNum()), this.primeNum);
                //Fix current grammar rule's production rules
                newProductionRule = new ProductionRule();
                newProductionRule.addAll(sharedPrefixList.get(0).subList(0, samePrefixNum));
                newProductionRule.add(newRule);
                newProductionRules.add(newProductionRule);
                this.setRules(newProductionRules);
                // Remove the common prefix
                for (ProductionRule pr : sharedPrefixList) {
                    pr.removeFirstN(samePrefixNum);
                }
                // Add the production rules to the new grammar rule
                newRule.setRules(sharedPrefixList);
                newGrammarRules.add(newRule);
            }
        }

        if (newGrammarRules.isEmpty()) // no changes have been made
            return null;
        else // need to update grammar rules
            return newGrammarRules;
    }

    // ============================================================================================
    // =====================================PRIVATE METHOD=========================================
    // ============================================================================================

    public boolean buildFirstSet(boolean wasChange) {
        boolean containEpsilon;
        for (ProductionRule pr : this.rules) {
            // set containEpsilon to be default value which is true
            containEpsilon = true;
            for (GrammarRule gr : pr.getContent()) {
                // check whether the first set will change or not
                wasChange = pr.unionFirstSet(wasChange, gr.getFirstSet());
                // check whether that grammar rule contains epsilon or not
                if (!gr.getFirstSet().contains(EpsilonRule.getEpsilonRule())) {
                    containEpsilon = false;
                    break;
                }
            }
            // if this rule may produce epsilon
            if (containEpsilon && (!pr.getFirstSet().contains(EpsilonRule.getEpsilonRule()))) {
                pr.getFirstSet().addLast(EpsilonRule.getEpsilonRule());
            }
        }
        return wasChange;
    }

    private int countSamePrefix(ProductionRule cRule, ProductionRule nRule) {
        int num = 0;
        int maxNum = (cRule.size() < nRule.size()) ? cRule.size() : nRule.size();
        for (int i = 0; i < maxNum; i++) {
            if (!Objects.equals(cRule.get(i).symbol, nRule.get(i).symbol)) break;
            else num++;
        }
        return num;
    }

    private String primeString(String origin, int primeNum) {
        StringBuilder sb = new StringBuilder();
        sb.append(origin);
        for (int i = 0; i < primeNum; i++) {
            sb.append("'");
        }
        return sb.toString();
    }

    private NonTerminalRule(String sym, int num) {
        this.symbol = sym;
        this.primeNum = new PrimeNumber(num);
        this.rules = new LinkedList<>();
        this.followSet = new LinkedList<>();
    }

    public void addRule(LinkedList<GrammarRule> r) {
        this.rules.add(new ProductionRule(r));
    }


    // ============================================================================================
    // ======================================INNER CLASS===========================================
    // ============================================================================================

    private class PrimeNumber {

        int num;

        PrimeNumber(int num) {
            this.num = num;
        }

        void increase() {
            this.num++;
        }

        int getNum() {
            return this.num;
        }
    }

}
