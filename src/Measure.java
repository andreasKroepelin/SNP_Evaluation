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
public class Measure {

    public static enum Standardization {
        RAW, SUBTRACT_MEAN, Z_STANDARDIZATION
    }

    public SNPEvaluation.SingleMeasure sinM;
    public SNPEvaluation.WindowMeasure winM;
    public int windowsize;
    public String name;
    public Standardization standardization;

    public Measure(SNPEvaluation.SingleMeasure sinM, SNPEvaluation.WindowMeasure winM, int windowsize, String name, Standardization standardization) {
        this.sinM = sinM;
        this.winM = winM;
        this.windowsize = windowsize;
        this.name = name;
        this.standardization = standardization;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getDescription() {
        return getWinMName() + " " + getSinMName() + (windowsize > 1 ? " window (" + windowsize + ")" : " local")
                + (standardization == Standardization.Z_STANDARDIZATION ? " z-standardized" : (standardization == Standardization.SUBTRACT_MEAN ? " subtracted mean" : ""));
    }

    public String getSinMName() {
        return SNPEvaluation.getSingleMeasureName(sinM);
    }

    public String getWinMName() {
        return SNPEvaluation.getWindowMeasureName(winM);
    }

}
