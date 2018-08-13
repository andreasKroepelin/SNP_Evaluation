/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

/**
 *
 * @author guestmpi
 */
public class SNPTableEntry {

    public String chrom;
    public int pos;
    public String snps;

    public SNPTableEntry(String chrom, int pos, String snps) {
        this.chrom = chrom;
        this.pos = pos;
        this.snps = snps;
    }

    @Override
    public String toString() {
        return chrom + " " + pos + " " + snps;
    }

    public boolean isUniqueSNP(int sample) {
        if (snps.charAt(sample) == '.' || snps.charAt(sample) == 'N') {
            return false;
        }
        for (int i = 1; i < snps.length(); i++) {
            if (i != sample && snps.charAt(i) != '.' && snps.charAt(i) != 'N') {
                return false;
            }
        }

        return true;
    }

    public boolean isSharedSNP(int sample) {
        if (snps.charAt(sample) == '.' || snps.charAt(sample) == 'N') {
            return false;
        }
        for (int i = 1; i < snps.length(); i++) {
            if (i != sample && snps.charAt(i) != '.' && snps.charAt(i) != 'N') {
                return true;
            }
        }

        return false;
    }

    public boolean isIn(int sample) {
        return !(snps.charAt(sample) == '.' || snps.charAt(sample) == 'N');
    }

    public int getNumberOfOccurences() {
        int n = 0;
        for (int i = 1; i < snps.length(); i++) {
            if (snps.charAt(i) != '.' && snps.charAt(i) != 'N') {
                n++;
            }
        }

        return n;
    }

}
