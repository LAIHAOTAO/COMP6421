package util;

import common.Const;
import semantic.symboltable.SymbolTable;
import semantic.symboltable.SymbolTableActionHandler;
import semantic.symboltable.SymbolTableEntry;
import semantic.symboltable.entry.FunctionAbstractEntry;
import semantic.symboltable.type.ArrayType;
import semantic.symboltable.type.SymbolTableEntryType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
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
        formatter.format("| %-15s | %-15s | %-40s | %-15s |\n", "name", "kind", "type", "link");
        formatter.format("--------------------------------------------------------------------------------------------------\n");

        for (Map.Entry<String, SymbolTableEntry> entry : table.entrySet()) {
            SymbolTable scope = entry.getValue().getScope();
            formatter.format("| %-15s | %-15s | %-40s | %-15s |\n",
                    entry.getValue().getName(),
                    entry.getValue().getKind(),
                    entry.getValue().getType().toString(),
                    (scope != null) ? scope.getName() : "null"
            );
        }

        formatter.format("--------------------------------------------------------------------------------------------------\n\n");
    }
}
