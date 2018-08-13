/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import java.util.ArrayList;
import java.util.function.BiFunction;

/**
 *
 * @author kroepelin
 */
public class ComparisonList {

    private ArrayList<Boolean> toApplys;
    private ArrayList<BiFunction<Double, Double, Double>> functions;
    private ArrayList<String> names;

    public ComparisonList() {
        toApplys = new ArrayList<>();
        functions = new ArrayList<>();
        names = new ArrayList<>();
    }

    public void add(boolean toApply, BiFunction<Double, Double, Double> function, String name) {
        toApplys.add(toApply);
        functions.add(function);
        names.add(name);
    }

    public boolean toApply(int i) {
        return toApplys.get(i);
    }

    public BiFunction<Double, Double, Double> function(int i) {
        return functions.get(i);
    }

    public String name(int i) {
        return names.get(i);
    }
}
