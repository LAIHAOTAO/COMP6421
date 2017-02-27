package syntactic;

import common.Const;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by ERIC_LAI on 2017-02-27.
 */
public class DerivationBuilder {

    private LinkedList<String> derivation = new LinkedList<>();
    private int dIdx = 0;
    private BufferedWriter bw;

    public DerivationBuilder(String fileNm) throws IOException {
        String prefix = Const.DIR_OUTPUT + "syntactic/derivation/";
        String suffix = "__Derivation.txt";
        String path = prefix + fileNm + suffix;
        this.bw = new BufferedWriter(new FileWriter(new File(path)));
        this.bw.write("========= Derivation begin =========");
        this.bw.newLine();
    }

    public void set(String tokenValue) {
        this.derivation.set(this.dIdx, tokenValue);
    }

    public void increaseIdx() {
        this.dIdx++;
    }

    public void remove() {
        this.derivation.remove(this.dIdx);
    }

    public boolean isEmpty() {
        return this.derivation.isEmpty();
    }

    public void addAll(LinkedList<String> symbols) {
        this.derivation.addAll(this.dIdx, symbols);
    }

    public void outputDerivation() throws IOException {
        this.bw.write("=> ");
        for (String str : this.derivation) {
            this.bw.write(str + " ");
        }
        this.bw.newLine();
    }

    public void close() throws IOException {
        clearDerivation();
        outputDerivation();
        this.bw.write("========= derivation end =========");
        this.bw.close();
    }

    private void clearDerivation() {
        if (this.dIdx < this.derivation.size()) {
            for (int i = this.dIdx; i <= this.derivation.size(); i++) {
                this.derivation.removeLast();
            }
        }
    }
}
