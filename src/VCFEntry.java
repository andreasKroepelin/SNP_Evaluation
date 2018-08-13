/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author guestmpi
 */
public class VCFEntry {

    public String chrom;
    public int id;
    public int pos;
    public char ref;
    public char alt;
    public double qual;
    public int coverage;
    public Genotype gt;
    public int suppR;

    public VCFEntry(String chrom, int id, int pos, char ref, char alt, double qual, int coverage, Genotype gt, int suppR) {
        this.chrom = chrom;
        this.id = id;
        this.pos = pos;
        this.ref = ref;
        this.alt = alt;
        this.qual = qual;
        this.coverage = coverage;
        this.gt = gt;
        this.suppR = suppR;
    }

    public boolean isHomZSNP() {
        if (alt != '.' && alt != 'N') {
            double dCoverage = (double) coverage;
            double dSuppR = (double) suppR;
            if (dSuppR / dCoverage < 0.1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isHetZSNP() {
        if (alt != '.' && alt != 'N') {
            double dCoverage = (double) coverage;
            double dSuppR = (double) suppR;
            if (dSuppR / dCoverage > 0.1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static HashMap<String, ArrayList<VCFEntry>> decomposeIntoContigs(ArrayList<VCFEntry> vcfentries) {
        HashMap<String, ArrayList<VCFEntry>> contigs = new HashMap<>();

        for (int i = 1; i < vcfentries.size(); i++) {
            String chrom = vcfentries.get(i).chrom;
            if (!contigs.containsKey(chrom)) {
                ArrayList<VCFEntry> newContig = new ArrayList<>();
                newContig.add(null); // for index reasons
                contigs.put(chrom, newContig);
            }
            contigs.get(chrom).add(vcfentries.get(i));

        }

        return contigs;
    }

    public enum Genotype {

        RR, RA, AA;
    }

}
