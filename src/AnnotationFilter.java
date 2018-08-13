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
public class AnnotationFilter {
    String filterCodPos;
    String filterProtDes;
    boolean applyCodPos;
    boolean applyProtDes;

    public AnnotationFilter(String filterCodPos, String filterProtDes, boolean applyCodPos, boolean applyProtDes) {
        this.filterCodPos = filterCodPos;
        this.filterProtDes = filterProtDes;
        this.applyCodPos = applyCodPos;
        this.applyProtDes = applyProtDes;
    }
    
    
}
