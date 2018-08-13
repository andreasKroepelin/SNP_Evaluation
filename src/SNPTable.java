/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import java.util.ArrayList;
import javax.swing.JTable;

/**
 *
 * @author guestmpi
 */
public class SNPTable {

    public String[] samples;
    public ArrayList<SNPTableEntry> entries;

    public SNPTable(String[] samples, ArrayList<SNPTableEntry> entries) {
        this.samples = samples;
        this.entries = entries;
    }

    public SNPTable() {
        this.samples = new String[0];
        this.entries = new ArrayList<>();
    }

    public SNPTable filterForUniqueSNPsIn(int sample) {
        ArrayList<SNPTableEntry> filtered = new ArrayList<>();
        this.entries.stream().filter(e -> e.isUniqueSNP(sample)).forEach(filtered::add);

        return new SNPTable(samples, filtered);
    }

    public SNPTable filterForUniqueSNPsIn(String sample) {
        int i = 0;
        while (!samples[i].equals(sample)) {
            i++;

            if (i >= samples.length) {
                return null;
            }
        }

        return filterForUniqueSNPsIn(i);
    }

    public SNPTable filterForSharedSNPsIn(int sample) {
        ArrayList<SNPTableEntry> filtered = new ArrayList<>();
        this.entries.stream().filter(e -> e.isSharedSNP(sample)).forEach(filtered::add);

        return new SNPTable(samples, filtered);
    }

    public SNPTable filterForSharedSNPsIn(String sample) {
        int i = 0;
        while (!samples[i].equals(sample)) {
            i++;

            if (i >= samples.length) {
                return null;
            }
        }

        return filterForSharedSNPsIn(i);
    }

    public SNPTable filterForSNPsIn(int sample) {
        ArrayList<SNPTableEntry> filtered = new ArrayList<>();
        this.entries.stream().filter(e -> e.isIn(sample)).forEach(filtered::add);

        return new SNPTable(samples, filtered);
    }

    public SNPTable filterForSNPsIn(String sample) {
        int i = 0;
        while (!samples[i].equals(sample)) {
            i++;

            if (i >= samples.length) {
                return null;
            }
        }

        return filterForSNPsIn(i);
    }

    public ArrayList<SNPTableEntry> filterEntriesAccordingTo(JTable table) {
        ArrayList<SNPTableEntry> newEntries = new ArrayList<>();
        int posCol;
        for (posCol = 0; posCol < table.getColumnCount(); posCol++) {
            if (table.getColumnName(posCol).equals("Position")) {
                break;
            }
        }

        ArrayList<Integer> positions = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            positions.add(Integer.parseInt(table.getValueAt(i, posCol).toString()));
        }
        for (SNPTableEntry e : entries) {
            if (positions.contains(e.pos)) {
                newEntries.add(e);
            }
        }
        return newEntries;
    }

    public SNPTableEntry getAtPos(int pos) {
        for (SNPTableEntry e : entries) {
            if (e.pos == pos) {
                return e;
            }
        }
        return null;
    }

    public void print() {
        System.out.println();

        System.out.print("Pos\t");
        for (String s : samples) {
            System.out.print(s + "\t");
        }
        System.out.println();
        for (SNPTableEntry e : entries) {
            System.out.print(e.pos + "\t");
            for (char c : e.snps.toCharArray()) {
                System.out.print(c + "\t");
            }
            System.out.println();
        }
    }

}
