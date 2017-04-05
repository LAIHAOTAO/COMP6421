package util;

import common.Const;
import semantic.symboltable.SymbolTable;
import semantic.handler.SymbolTableActionHandler;
import semantic.symboltable.entry.SymbolTableEntry;
import semantic.symboltable.type.ClassType;
import semantic.symboltable.type.SymbolTableEntryType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Map;

/**
 * Created by ERIC_LAI on 2017-03-19.
 */
public class SymbolTablePrinter {

    private static Formatter formatter;;

    public static void print() {
        formatter = new Formatter(System.out);
        for (SymbolTable table : SymbolTableActionHandler.symbolTableList) {
            print(table);
        }
        formatter.close();
    }

    public static void outputToFile(String fileNm) throws FileNotFoundException {
        File file = new File(Const.DIR_OUTPUT + "semantic/table/" + fileNm + "__SymbolTable.txt");
        formatter = new Formatter(file);

        for (SymbolTable table : SymbolTableActionHandler.symbolTableList) {
            print(table);
        }
        formatter.close();
    }

    private static void print(SymbolTable table) {

        formatter.format("Table Name: %s,  Parent Table Name: %s\n", table.getName(), table.getParentName());
        formatter.format("--------------------------------------------------------------------------------------------------\n");
        formatter.format("| %-15s | %-15s | %-30s | %-10s | %-10s |\n", "name", "kind", "type", "offset", "link");
        formatter.format("--------------------------------------------------------------------------------------------------\n");

        for (Map.Entry<String, SymbolTableEntry> entry : table.entrySet()) {
            String link;
            SymbolTable scope = entry.getValue().getScope();
            if (scope == null) {
                SymbolTableEntryType type = entry.getValue().getType();
                if (type instanceof ClassType) {
                    link = ((ClassType) type).getName();
                }else link = "null";
            } else {
                link = scope.getName();
            }
            formatter.format("| %-15s | %-15s | %-30s | %-10s | %-10s |\n",
                    entry.getValue().getName(),
                    entry.getValue().getKind(),
                    entry.getValue().getType().toString(),
                    entry.getValue().getOffset(),
                    link
            );
        }

        formatter.format("--------------------------------------------------------------------------------------------------\n\n");
    }
}
