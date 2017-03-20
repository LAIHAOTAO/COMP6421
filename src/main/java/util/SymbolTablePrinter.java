package util;

import semantic.*;

import java.util.Formatter;
import java.util.Map;

/**
 * Created by ERIC_LAI on 2017-03-19.
 */
public class SymbolTablePrinter {

    public static void print() {
        for (SymbolTable table : SymbolTableActionHandler.symbolTableList) {
            print(table);
        }
    }

    private static void print(SymbolTable table) {
        Formatter formatter = new Formatter(System.out);
        formatter.format("Table Name: %-15s \n", table.getName());
        formatter.format("-------------------------------------------------------------------------\n");
        formatter.format("| %-15s | %-15s | %-15s | %-15s |\n", "name", "kind", "type", "link");
        formatter.format("-------------------------------------------------------------------------\n");


        for (Map.Entry<String, SymbolTableEntry> entry : table.entrySet()) {
            SymbolTable scope = entry.getValue().getScope();
            formatter.format("| %-15s | %-15s | %-15s | %-15s |\n",
                    entry.getValue().getName(),
                    entry.getValue().getKind(),
                    entry.getValue().getType(),
                    (scope != null)? scope.getName() : "null"
            );
        }

        formatter.format("-------------------------------------------------------------------------\n\n");
    }
}
