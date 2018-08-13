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
public class ProtTableEntry {

    int start;
    int stop;
    boolean strand;
    String locus;
    String locusTag;
    String proteinDescription;

    public ProtTableEntry(int start, int stop, boolean strand, String locus, String locusTag, String proteinDescription) {
        this.start = start;
        this.stop = stop;
        this.strand = strand;
        this.locus = locus;
        this.locusTag = locusTag;
        this.proteinDescription = proteinDescription;
    }

    public boolean isInside(int pos) {
        return start <= pos && pos <= stop;
    }
}
