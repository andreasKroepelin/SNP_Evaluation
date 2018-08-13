/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import com.apple.eawt.Application;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.prefs.Preferences;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author guestmpi
 */
public class SNPEvaluation {

    private static HashMap<String, SingleMeasure> singleMeasures;
    private static HashMap<String, WindowMeasure> windowMeasures;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        singleMeasures = new HashMap<>();
        windowMeasures = new HashMap<>();

        singleMeasures.put("coverage", e -> e.coverage);
        singleMeasures.put("quality", e -> e.qual);
        singleMeasures.put("not covered", e -> e.coverage == 0 ? 1 : 0);
        singleMeasures.put("homoz. SNP", e -> e.isHomZSNP() ? 1 : 0);
        singleMeasures.put("heteroz. SNP", e -> e.isHetZSNP() ? 1 : 0);
        singleMeasures.put("hetero- or homoz. SNP", e -> (e.isHetZSNP() || e.isHomZSNP()) ? 1 : 0);
        singleMeasures.put("abs. ref. support", e -> e.suppR);
        singleMeasures.put("rel. ref. support", e -> ((double) e.suppR) / ((double) e.coverage));

        windowMeasures.put("max", w -> {
            double m = Double.NEGATIVE_INFINITY;
            for (Double d : w) {
                if (d > m) {
                    m = d;
                }
            }
            return m;
        });
        windowMeasures.put("min", w -> {
            double m = Double.POSITIVE_INFINITY;
            for (Double d : w) {
                if (d < m) {
                    m = d;
                }
            }
            return m;
        });
        windowMeasures.put("sum", w -> {
            double s = 0;
            for (Double d : w) {
                s += d;
            }
            return s;
        });
        windowMeasures.put("mean", w -> {
            return windowMeasures.get("sum").apply(w) / ((double) w.size());
        });
        windowMeasures.put("variance", w -> {
            double m = windowMeasures.get("mean").apply(w);
            double squaredDiff = 0;
            for (Double d : w) {
                squaredDiff += (d - m) * (d - m);
            }
            return squaredDiff / ((double) w.size());
        });
        windowMeasures.put("SD", w -> {
            return Math.sqrt(windowMeasures.get("variance").apply(w));
        });

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("-resetFirst")) {
                Preferences p = Preferences.userNodeForPackage(SNPEvaluation.class);
                p.putBoolean("firstTime", true);
                System.out.println("You\'ve never been here...");
                return;
            } else if (args[0].equalsIgnoreCase("-generate")) {
                if (args.length == 7) {
                    String samplename = args[1];
                    int numSamples = Integer.parseInt(args[2]);
                    int numSNPs = Integer.parseInt(args[3]);
                    int genomeSize = Integer.parseInt(args[4]);
                    long seed = Long.parseLong(args[5]);
                    String outfile = args[6];

                    DataGenerator.generate(samplename, numSamples, numSNPs, genomeSize, seed, outfile);
                    System.out.println("Data generated");
                } else {
                    System.out.println("Arguments (in this order):\n\nName of the sample\nNumber of samples\nNumber of SNPs\nGenome size\nRandom generator seed\nOutput filename");
                }
                return;
            }
        }

        //System.getProperties().list(System.out);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }

        Surface surface = new Surface();
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            Application app = Application.getApplication();
            app.setDockIconImage(surface.getIconBig().getImage());
        }

    }

    public static SingleMeasure getSingleMeasure(String key) {
        return singleMeasures.get(key);
    }

    public static WindowMeasure getWindowMeasure(String key) {
        return windowMeasures.get(key);
    }

    public static String getSingleMeasureName(SingleMeasure sinM) {
        return singleMeasures.entrySet().stream().filter(e -> e.getValue() == sinM).findAny().get().getKey();
    }

    public static String getWindowMeasureName(WindowMeasure winM) {
        return windowMeasures.entrySet().stream().filter(e -> e.getValue() == winM).findAny().get().getKey();
    }

    public static String[] getSingleMeasureNames() {
        return singleMeasures.keySet().toArray(new String[0]);
    }

    public static String[] getWindowMeasureNames() {
        return windowMeasures.keySet().toArray(new String[0]);
    }

    public static double evalSNP(int pos, SingleMeasure sinM, WindowMeasure winM, int windowsize, ArrayList<VCFEntry> sampleGenome) {
        if (windowsize <= 1) {
            return sinM.apply(sampleGenome.get(pos));
        } else {

            int lo = Math.max(1, pos - windowsize / 2);
            int hi = Math.min(sampleGenome.size() - 1, pos + windowsize / 2);

            ArrayList<Double> windowEval = new ArrayList<>();

            for (int i = lo; i < hi; i++) {
                windowEval.add(sinM.apply(sampleGenome.get(i)));
            }

            return winM.apply(windowEval);
        }
    }

    public interface SingleMeasure {

        public double apply(VCFEntry entry);
    }

    public interface WindowMeasure {

        public double apply(ArrayList<Double> window);

    }

    public static ProtTableEntry getAnnotatedSection(int pos, ArrayList<ProtTableEntry> prottable) {
        int i = 0;
        int oldI;
        int lo = 0;
        int hi = prottable.size() - 1;

        while (lo <= hi) {
            oldI = i;
            i = (lo + hi) / 2;
            //System.out.println(pos + " " + i + " " + lo + " " + hi);

            if (prottable.get(i).start > pos) {
                hi = i;
            } else if (prottable.get(i).stop < pos) {
                lo = i;
            } else {
                return prottable.get(i);
            }

            if (i == oldI) {
                break;
            }
        }
        return null;
    }

    public static int getCodonPosition(int pos, ProtTableEntry pte) {
        if (pte != null && pte.isInside(pos)) {
            if (pte.strand) {
                return (pos - pte.start) % 3 + 1;
            } else {
                return (pte.stop - pos) % 3 + 1;
            }
        } else {
            return 0;
        }
    }

    public static String stackTraceString(Exception e) {
        StringBuilder st = new StringBuilder("");
        Arrays.asList(e.getStackTrace()).stream().map(ste -> ste.toString()).filter(s -> s.startsWith("snpevaluation")).forEachOrdered(s -> st.append(s).append("\n"));

        return e.getClass().getName() + "\n" + e.getMessage() + "\n" + st.toString();
    }

    public static void setDefaultPath(String path) {
        Preferences p = Preferences.userNodeForPackage(SNPEvaluation.class);
        p.put("default_path", path);
    }

    public static String getDefaultPath() {
        Preferences p = Preferences.userNodeForPackage(SNPEvaluation.class);
        return p.get("default_path", "");
    }

    public static boolean isFirstTime() {
        Preferences p = Preferences.userNodeForPackage(SNPEvaluation.class);
        if (p.getBoolean("firstTime", true)) {
            p.putBoolean("firstTime", false);
            return true;
        }
        return false;
    }

}
