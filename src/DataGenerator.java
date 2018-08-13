/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

/**
 *
 * @author kroepelin
 */
public class DataGenerator {

    private static Random rnd;
    private static final char[] ACGT = new char[]{'A', 'C', 'G', 'T'};

    public static void generate(String samplename, int numSamples, int numSNPs, int genomeSize, long seed, String outfile) {
        rnd = seed > 0 ? new Random(seed) : new Random();
        if (!outfile.endsWith(File.separator)) {
            outfile = outfile + File.separator;
        }

        new File(outfile).mkdirs();

        try {
            BufferedWriter table = new BufferedWriter(new FileWriter(outfile + samplename + "_SNPTable.tsv"));
            BufferedWriter vcfH = new BufferedWriter(new FileWriter(outfile + samplename + "_high.vcf"));
            BufferedWriter vcfL = new BufferedWriter(new FileWriter(outfile + samplename + "_low.vcf"));

            vcfH.write("##high stringency mapping. just some random stuff...");
            vcfH.newLine();
            vcfH.write("##bla bla bla bla bla bla bla bla bla");
            vcfH.newLine();
            vcfH.write("##and now it begins...");
            vcfH.newLine();
            vcfH.write("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\tFORMAT\t" + samplename);
            vcfH.newLine();
            vcfL.write("low stringency mapping. just some random stuff...");
            vcfL.newLine();
            vcfL.write("bla bla bla bla bla bla bla bla bla");
            vcfL.newLine();
            vcfL.write("and now it begins...");
            vcfL.newLine();
            vcfL.write("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\tFORMAT\t" + samplename);
            vcfL.newLine();

            table.write("Position\tRef\t");
            for (int i = 1; i < numSamples; i++) {
                table.write("Sample" + i + "\t");
            }
            table.write(samplename);
            table.newLine();

            int pos = rnd.nextInt(100);
            int snpCount = 0;
            char alt;
            char ref;
            int covH = 20;
            int covL;
            int qualH;
            int qualL;
            for (int j = 1; j <= genomeSize; j++) {
                ref = rndACGT();
                vcfH.write("chrom\t" + pos + "\tx\t" + ref + "\t");
                vcfL.write("chrom\t" + pos + "\tx\t" + ref + "\t");

                if (j == pos) {
                    do {
                        alt = rndACGT();
                    } while (alt == ref);
                    snpCount++;

                    vcfH.write(alt + "\t");
                    vcfL.write(alt + "\t");

                    table.write(pos + "\t");
                    table.write(rndACGT() + "\t");

                    for (int i = 1; i <= numSamples; i++) {
                        if (rnd.nextInt(numSamples) < 1) {
                            table.write(alt + "");
                        } else {
                            table.write(".");
                        }
                        if (i < numSamples) {
                            table.write('\t');
                        }
                    }
                    if (j < genomeSize) {
                        table.newLine();
                    }

                } else {
                    vcfH.write(".\t");
                    vcfL.write(".\t");
                }

                covH += 2 * rnd.nextGaussian();
                covH = Math.min(covH, 40);
                covH = Math.max(covH, 1);
                covL = rnd.nextInt(4) + covH;
                covL = Math.min(covL, 50);
                covL = Math.max(covL, 1);
                qualH = (int) (covH + 3 * rnd.nextGaussian());
                qualL = (int) (covL + 3 * rnd.nextGaussian());
                vcfH.write(qualH + "\tx\tx\tx\t");
                vcfL.write(qualL + "\tx\tx\tx\t");

                if (j == pos) {
                    vcfH.write("1/1:" + covH);
                    vcfL.write("1/1:" + covL);

                    if (snpCount < numSNPs) {
                        pos = rnd.nextInt(100) + pos;
                    } else {
                        pos = -1;
                    }
                } else {
                    vcfH.write("0/0:" + covH);
                    vcfL.write("0/0:" + covL);
                }

                if (j < genomeSize) {
                    vcfH.newLine();
                    vcfL.newLine();
                }

            }

            table.close();
            vcfH.close();
            vcfL.close();
        } catch (Exception e) {
        }
    }

    public static char rndACGT() {
        return ACGT[rnd.nextInt(4)];
    }
}
