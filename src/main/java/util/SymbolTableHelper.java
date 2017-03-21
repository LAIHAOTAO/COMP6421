package util;

import common.Const;
import semantic.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by ERIC_LAI on 2017-03-19.
 */
public class SymbolTableHelper {

    private static final int CONSOLE = 1;
    private static final int FILE = 2;
    private static Formatter formatter;;

    public static void print() {
        formatter = new Formatter(System.out);
        for (SymbolTable table : SymbolTableActionHandler.symbolTableList) {
            print(table, CONSOLE, null);
        }
        formatter.close();
    }

    public static void outputToFile(String fileNm) throws FileNotFoundException {
        File file = new File(Const.DIR_OUTPUT + "semantic/table/" + fileNm + "__SymbolTable.txt");
        formatter = new Formatter(file);

        for (SymbolTable table : SymbolTableActionHandler.symbolTableList) {
            print(table, FILE, fileNm);
        }
        formatter.close();
    }

    private static void print(SymbolTable table, int target, String fileNm) {

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
