package semantic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ERIC_LAI on 2017-03-18.
 */
public class SymbolTable {

    private HashMap<String, SymbolTableEntry> entries;
    private String name;
    private SymbolTable parent;

    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
        this.entries = new HashMap<>();
    }

    public SymbolTableEntry search(String key) {
        return this.entries.get(key);
    }

    public void insert(String key, SymbolTableEntry entry) {
        this.entries.put(key, entry);
    }

    public void delete(String key) {
        this.entries.remove(key);
    }

    public boolean exist(String key) {
        return this.entries.containsKey(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key : entries.keySet()) {
            sb.append(key).append(" -> ").append(entries.get(key).toString()).append('\n');
        }
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public Set<Map.Entry<String, SymbolTableEntry>> entrySet() {
        return entries.entrySet();
    }

    public void setName(String name) {
        this.name = name;
    }
}
