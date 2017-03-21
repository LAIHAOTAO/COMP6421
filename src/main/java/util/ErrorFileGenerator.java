package util;

import common.Const;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by ERIC_LAI on 2017-03-20.
 */
public class ErrorFileGenerator {

    public static void outputError(String fileNm, String errMsg, Const.ErrorLevel level) throws IOException {
        String prefix = Const.DIR_OUTPUT + level + "/result/";
        String suffix = "__" + level + "Result.txt";
        String path = prefix + fileNm + suffix;
        File file = new File(path);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        if (errMsg.isEmpty()) {
            bw.write(level + "analysis successfully");
        } else {
            bw.write(errMsg);
        }
        bw.close();
    }
}
