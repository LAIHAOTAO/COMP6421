package lexical;

import common.Const;

import java.io.*;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ERIC_LAI on 2017-01-17.
 */
public class SrcFileReader {

    private BufferedReader br;
    private int currentLine;
    private String filePath;

    public SrcFileReader(String path) {
        File file = new File(path);
        this.filePath = file.getAbsolutePath();
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a line of source code from file without '\t', '\r', '\n' and inline comment
     *
     * @return a string array, where the first element is a line of source which has been
     * pre-handled and the second element is the line number of that code in the file
     */
    public String[] getLine() {
        String[] result = new String[Const.LINE_RESULT_SIZE];
        if (br != null) {
            try {
                String line = br.readLine();
                if (line != null) {
                    currentLine += 1;
                    result[Const.LINE_VALUE] = replaceBlank(line).trim();
                    result[Const.LINE_NUM] = String.valueOf(currentLine);
                    return result;
                }
            } catch (IOException e) {
                System.err.println("something wrong in SrcFilerReader next() method");
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Replace the '\t', '\r' and '\n' with a whitespace and remove inline comment
     *
     * @param line a line of string from the source code file
     * @return a line of source code that without special element for lexical analysis
     */
    private String replaceBlank(String line) {
        String dest = "";
        if (line != null) {
            // replace all `tabs`, `enter` and `next line` with whitespace
            Pattern p = Pattern.compile("\t|\r\n");
            Matcher m = p.matcher(line);
            dest = m.replaceAll(" ");
            // ignore comments and multiple-line codes
            // it can remove '//' and '/* */'
//            dest = dest.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\*\\/]*)*" +
//                    "\\*\\/|\\\\", "");
            dest = dest.replaceAll("\\/\\/[^\\n]*|\\/\\*[^\\*^\\/]*\\*\\/", "");
        }
        return dest;
    }

    public void close() throws IOException {
        this.br.close();
    }

    public String getFilePath() {
        return filePath;
    }
}
