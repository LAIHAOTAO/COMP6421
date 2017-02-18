package syntactic;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by ERIC_LAI on 2017-02-16.
 */
public class ProductionRule {

    private LinkedList<GrammarRule> content;
    private LinkedList<TerminalRule> firstSet;

    public ProductionRule(LinkedList<GrammarRule> content) {
        this.content = content;
    }

    public ProductionRule() {
        this.content = new LinkedList<>();
    }

    public void add(GrammarRule rule) {
        this.content.add(rule);
    }

    public void addFirst(GrammarRule rule) {
        this.content.addFirst(rule);
    }

    public void addLast(GrammarRule rule) {
        this.content.addLast(rule);
    }

    public void addAll(Collection<GrammarRule> rules) {
        this.content.addAll(rules);
    }

    public GrammarRule removeFirst() {
        return this.content.removeFirst();
    }

    public GrammarRule remove() {
        return this.content.remove();
    }

    public GrammarRule removeLast() {
        return this.content.removeLast();
    }

    public void removeFirstN(int n) {
        if (this.content.size() > n) {
            for (int i = 0; i < n; i++) {
                this.content.removeFirst();
            }
        } else if (this.content.size() == n) {
            this.content = EpsilonRule.getEpsilonList();
        } else {
            throw new RuntimeException("ProductionRule, cannot remove more than " + n + " " +
                    "element," + "since the size of the content is " + this.content.size());
        }
    }

    public GrammarRule get(int i) {
        return this.content.get(i);
    }

    public GrammarRule getFirst() {
        return this.content.getFirst();
    }

    public GrammarRule getLast() {
        return this.content.getLast();
    }

    public LinkedList<GrammarRule> subList(int s, int e) {
        LinkedList<GrammarRule> subList = new LinkedList<>();
        for (int i = s; i < e; i++) {
            subList.add(this.content.get(i));
        }
        return subList;
    }

    public boolean contains(GrammarRule rule) {
        return this.content.contains(rule);
    }

    public int size() {
        return this.content.size();
    }

    public boolean isEmpty() {
        return this.content.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (GrammarRule rule : this.content) {
            sb.append(rule.symbol);
        }
        return sb.toString();
    }

    private boolean unionFirstSet(boolean update, LinkedList<TerminalRule> target) {
        for (TerminalRule t : target) {
            if (t != EpsilonRule.getEpsilonRule() && !this.firstSet.contains(t)) {
                this.firstSet.addLast(t);
                update = true;
            }
        }
        return update;
    }

    public void addFirstSet(TerminalRule t) {
        this.firstSet.add(t);
    }

    public boolean inFirstSet(TerminalRule t) {
        return this.firstSet.contains(t);
    }

    public LinkedList<GrammarRule> getContent() {
        return content;
    }

    public void setContent(LinkedList<GrammarRule> content) {
        this.content = content;
    }

    public LinkedList<TerminalRule> getFirstSet() {
        return firstSet;
    }

    public void setFirstSet(LinkedList<TerminalRule> firstSet) {
        this.firstSet = firstSet;
    }

    public void reverse() {
        LinkedList<GrammarRule> reverse = new LinkedList<>();
        while (!this.content.isEmpty()) {
            reverse.addFirst(content.removeFirst());
        }
        this.content = reverse;
    }
}
