/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author guestmpi
 */
public class ProtTableParser {

    public static ArrayList<ProtTableEntry> parse(String filename) throws Exception {
        ArrayList<ProtTableEntry> entries = new ArrayList<>();

        BufferedReader read = new BufferedReader(new FileReader(filename));
        read.readLine();

        String line;
        while ((line = read.readLine()) != null) {
            String[] separated = line.split("\t");

            int start = Integer.parseInt(separated[2]);
            int stop = Integer.parseInt(separated[3]);
            boolean strand = separated[4].equals("+");
            String locus = separated[6];
            String locusTag = separated[7];
            String proteinDescription = separated[11];

            entries.add(new ProtTableEntry(start, stop, strand, locus, locusTag, proteinDescription));
        }

        read.close();

        return entries;
    }
}
