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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

/**
 *
 * @author guestmpi
 */
public class VCFParser {

    public static ArrayList<VCFEntry> parse(String filename) throws Exception {
        ArrayList<VCFEntry> entries = new ArrayList<>();
        entries.add(null); // to avoid index shifts (genome starts at position 1)

        BufferedReader read;
        if (filename.toLowerCase().endsWith(".gz")) {
            read = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(filename))));
        } else {
            read = new BufferedReader(new FileReader(filename));
        }
        String line;
        int id = 0;
        read.readLine();
        while (read.readLine().startsWith("##")); // skip until table-head

        while ((line = read.readLine()) != null) {
            String[] separated = line.split("\t");

            String chrom = separated[0];
            id++;
            int pos = Integer.parseInt(separated[1]);
            char ref = separated[3].charAt(0);
            char alt = separated[4].charAt(0);
            double qual = 0;
            if (!separated[5].equals(".")) {
                qual = Double.parseDouble(separated[5]);
            }

            HashMap<String, Integer> indices = parseFormatCol(separated[8]);
            int covIdx = indices.getOrDefault("DP", -1);
            int gtIdx = indices.getOrDefault("GT", -1);
            int suppRIdx = indices.getOrDefault("AD", -1);

            int coverage = 0;
            VCFEntry.Genotype gt = null;
            int suppR = 0;

            String[] lastCol = separated[9].split(":");
            if (covIdx > -1) {
                coverage = Integer.parseInt(lastCol[covIdx]);
            }
            if (gtIdx > -1) {
                if (lastCol[gtIdx].equals("0/0")) {
                    gt = VCFEntry.Genotype.RR;
                } else if (lastCol[gtIdx].equals("1/1")) {
                    gt = VCFEntry.Genotype.AA;
                } else {
                    gt = VCFEntry.Genotype.RA;
                }
            }
            if (suppRIdx > -1) {
                suppR = Integer.parseInt(lastCol[suppRIdx].split(",")[0]);
            }

            entries.add(new VCFEntry(chrom, id, pos, ref, alt, qual, coverage, gt, suppR));
        }

        read.close();

        return entries;
    }

    private static HashMap<String, Integer> parseFormatCol(String formatCol) {
        HashMap<String, Integer> indices = new HashMap<>();

        String[] cols = formatCol.split(":");
        for (int i = 0; i < cols.length; i++) {
            indices.put(cols[i], i);
        }

        return indices;
    }

}
