package util;

import semantic.*;

import java.util.Formatter;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by ERIC_LAI on 2017-03-19.
 */
public class SymbolTableHelper {

    public static SymbolTable getGlobalTable() {
        if (!SymbolTableActionHandler.symbolTableList.isEmpty()) {
            for (SymbolTable table : SymbolTableActionHandler.symbolTableList) {
                if ("global table".equals(table.getName())) {
                    return table;
                }
            }
        }
        System.err.println("The global table do not exit !!!");
        return null;
    }

    public static void print() {
        for (SymbolTable table : SymbolTableActionHandler.symbolTableList) {
            print(table);
        }
    }

    private static void print(SymbolTable table) {
        Formatter formatter = new Formatter(System.out);
        formatter.format("Table Name: %s,  Parent Table Name: %s\n", table.getName(), table.getParentName());
        formatter.format("--------------------------------------------------------------------------------------------------\n");
        formatter.format("| %-15s | %-15s | %-40s | %-15s |\n", "name", "kind", "type", "link");
        formatter.format("--------------------------------------------------------------------------------------------------\n");

        for (Map.Entry<String, SymbolTableEntry> entry : table.entrySet()) {
            SymbolTable scope = entry.getValue().getScope();

            StringBuilder paramStrBuilder = new StringBuilder();
            LinkedList<SymbolTableEntry.Type> paramTypes = entry.getValue().getParamTypeList();
            if (!paramTypes.isEmpty()) {
                for (int i = 0; i < paramTypes.size(); i++) {
                    if (i == 0) {
                        paramStrBuilder.append(": ");
                        paramStrBuilder.append(paramTypes.get(i).toString());
                        LinkedList<Integer> d = entry.getValue().getParamDimensionList();
                        if (d.size() != 0) {
                            paramStrBuilder.append("[").append(d.get(i)).append("]");
                        }
                    } else {
                        paramStrBuilder.append(", ").append(paramTypes.get(i).toString());
                        LinkedList<Integer> d = entry.getValue().getParamDimensionList();
                        if (d.size() != 0) {
                            paramStrBuilder.append("[").append(d.get(i)).append("]");
                        }
                    }
                }

            }

            SymbolTableEntry.Type type = entry.getValue().getType();
            StringBuilder typeStrBuilder = new StringBuilder();
            typeStrBuilder.append(type.toString());
            if (type.toString().contains("Array")) {
                typeStrBuilder.append("[").append(entry.getValue().getDimension()).append("]");
            }

            formatter.format("| %-15s | %-15s | %-40s | %-15s |\n",
                    entry.getValue().getName(),
                    entry.getValue().getKind(),
                    typeStrBuilder.toString() + paramStrBuilder.toString(),
                    (scope != null) ? scope.getName() : "null"
            );
        }

        formatter.format("--------------------------------------------------------------------------------------------------\n\n");
    }
}
