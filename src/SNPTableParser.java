/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author guestmpi
 */
public class SNPTableParser {

    public static SNPTable parse(String filename) throws Exception {
        SNPTable table = new SNPTable();

        BufferedReader read;
        if (filename.toLowerCase().endsWith(".gz")) {
            read = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(filename))));
        } else {
            read = new BufferedReader(new FileReader(filename));
        }

        String line = read.readLine();

        String[] head = line.split("\t");
        table.samples = Arrays.copyOfRange(head, 1, head.length);

        while ((line = read.readLine()) != null) {
            String[] separated = line.split("\t");
            String chrom = "";
            int pos;
            int refCol;

            try {
                pos = Integer.parseInt(separated[0]);
                refCol = 1;
            } catch (NumberFormatException nfe) {
                chrom = separated[0];
                pos = Integer.parseInt(separated[1]);
                refCol = 2;
            }
            String snps = "";
            for (int i = refCol; i < separated.length; i++) {
                snps += separated[i].charAt(0);
            }

            table.entries.add(new SNPTableEntry(chrom, pos, snps));
        }

        read.close();

        return table;
    }
}
