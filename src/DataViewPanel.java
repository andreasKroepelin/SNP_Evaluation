/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import java.awt.Component;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author kroepelin
 */
public class DataViewPanel extends javax.swing.JPanel {
    
    private PlotPanel pp;
    private PlotHeaderPanel pph;
    private SNPTable mySNPTable;
    private Surface parent;
    
    private double[][] data1;
    private double[][] data2;
    private double[][] dataC;

    /**
     * Creates new form DataViewPanel
     */
    public DataViewPanel(Surface parent, SNPTable snptable) {
        initComponents();
        
        this.parent = parent;
        this.mySNPTable = snptable;
        pp = new PlotPanel(this);
        pph = new PlotHeaderPanel(pp);
        plotScroll.setViewportView(pp);
        plotHeadScroll.setViewportView(pph);
        plotHeadScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        plotScroll.getHorizontalScrollBar().getModel().addChangeListener((ChangeEvent e) -> {
            int value = plotScroll.getHorizontalScrollBar().getModel().getValue();
            int min = plotScroll.getHorizontalScrollBar().getModel().getMinimum();
            int max = plotScroll.getHorizontalScrollBar().getModel().getMaximum();
            int ext = plotScroll.getHorizontalScrollBar().getModel().getExtent();
            plotHeadScroll.getHorizontalScrollBar().getModel().setRangeProperties(value, ext, min, max, false);
        });
        
        plotScroll.getHorizontalScrollBar().setUnitIncrement(16);
        plotScroll.getVerticalScrollBar().setUnitIncrement(16);
        
        outTab.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
    
    public void evaluate() {
        try {
            if (mySNPTable.entries.isEmpty()) {
                System.out.println("nix");
                return;
            }
            
            String head = "Position\t#\tChange\tOccurs in ... samples\t";
            
            int q = 0;
            for (Measure m : parent.measures) {
                if (parent.isSingleMode()) {
                    head += m.name + "\t";
                } else {
                    head += parent.vcfAlias1 + ": " + m.name + "\t";
                    head += parent.vcfAlias2 + ": " + m.name + "\t";
                    if (parent.comparisons.toApply(q)) {
                        head += parent.comparisons.name(q) + "\t";
                    }
                }
                
                q++;
            }
            head += "Codon position\tLocus\tLocus tag\tProtein description";
            DefaultTableModel dtm = new DefaultTableModel(head.split("\t"), 0) {
                @Override
                public boolean isCellEditable(int x, int y) {
                    return false;
                }
                
                @Override
                public Class<?> getColumnClass(int colIndex) {
                    if (colIndex == 0) { //position
                        return String.class;
                    } else if (colIndex == 1) { //#
                        return Integer.class;
                    } else if (colIndex == 2) { //change
                        return String.class;
                    } else if (colIndex == 3) { //how often
                        return Integer.class;
                    } else if (colIndex > 3 && colIndex < this.getColumnCount() - 4) { //measures
                        return Double.class;
                    } else if (colIndex >= this.getColumnCount() - 4) { // protein table
                        return String.class;
                    } else { // should never happen
                        return Object.class;
                    }
                }
            };
            
            outTab.getColumnModel().setColumnMargin(4);
            
            data1 = new double[mySNPTable.entries.size()][parent.measures.size()];
            if (!parent.isSingleMode()) {
                data2 = new double[mySNPTable.entries.size()][parent.measures.size()];
                dataC = new double[mySNPTable.entries.size()][parent.measures.size()];
            }
            
            double[] mean1 = new double[parent.measures.size()];
            double[] mean2 = new double[parent.measures.size()];
            double[] sd1 = new double[parent.measures.size()];
            double[] sd2 = new double[parent.measures.size()];
            
            for (Measure m : parent.measures) {
                if (m.standardization == Measure.Standardization.SUBTRACT_MEAN) {
                    ArrayList<Double> allGenome1 = new ArrayList<>();
                    parent.vcfentries1.subList(1, parent.vcfentries1.size()).stream().map(e -> e == null ? 0 : m.sinM.apply(e)).forEachOrdered(allGenome1::add);
                    mean1[parent.measures.indexOf(m)] = SNPEvaluation.getWindowMeasure("mean").apply(allGenome1);
                    
                    if (!parent.isSingleMode()) {
                        ArrayList<Double> allGenome2 = new ArrayList<>();
                        parent.vcfentries2.subList(1, parent.vcfentries2.size()).stream().map(e -> e == null ? 0 : m.sinM.apply(e)).forEachOrdered(allGenome2::add);
                        mean2[parent.measures.indexOf(m)] = SNPEvaluation.getWindowMeasure("mean").apply(allGenome2);
                    }
                    
                } else if (m.standardization == Measure.Standardization.Z_STANDARDIZATION) {
                    ArrayList<Double> allGenome1 = new ArrayList<>();
                    parent.vcfentries1.subList(1, parent.vcfentries1.size()).stream().map(e -> e == null ? 0 : m.sinM.apply(e)).forEachOrdered(allGenome1::add);
                    mean1[parent.measures.indexOf(m)] = SNPEvaluation.getWindowMeasure("mean").apply(allGenome1);
                    sd1[parent.measures.indexOf(m)] = SNPEvaluation.getWindowMeasure("SD").apply(allGenome1);
                    
                    if (!parent.isSingleMode()) {
                        ArrayList<Double> allGenome2 = new ArrayList<>();
                        parent.vcfentries2.subList(1, parent.vcfentries2.size()).stream().map(e -> e == null ? 0 : m.sinM.apply(e)).forEachOrdered(allGenome2::add);
                        mean2[parent.measures.indexOf(m)] = SNPEvaluation.getWindowMeasure("mean").apply(allGenome2);
                        sd2[parent.measures.indexOf(m)] = SNPEvaluation.getWindowMeasure("SD").apply(allGenome2);
                    }
                }
            }
            
            
            
            int i = 0, j = 0;
            
            for (SNPTableEntry entry : mySNPTable.entries) {
                ArrayList<Object> row = new ArrayList<>();
                
                VCFEntry correspondingVCFEntry1 = entry.chrom.isEmpty() ? parent.vcfentries1.get(entry.pos) : parent.contigs1.get(entry.chrom).get(entry.pos);
                VCFEntry correspondingVCFEntry2 = null;
                if (!parent.isSingleMode()) {
                    correspondingVCFEntry2 = entry.chrom.isEmpty() ? parent.vcfentries2.get(entry.pos) : parent.contigs2.get(entry.chrom).get(entry.pos);
                }
                
                row.add(correspondingVCFEntry1.chrom + " " + correspondingVCFEntry1.pos);
                row.add(correspondingVCFEntry1.id);
                row.add(correspondingVCFEntry1.ref + " \u2192 " + correspondingVCFEntry1.alt);
                row.add(entry.getNumberOfOccurences());
                
                j = 0;
                for (Measure m : parent.measures) {
                    data1[i][j] = SNPEvaluation.evalSNP(correspondingVCFEntry1.pos, m.sinM, m.winM, m.windowsize, parent.contigs1.get(correspondingVCFEntry1.chrom));
                    if (m.standardization == Measure.Standardization.SUBTRACT_MEAN) {
                        data1[i][j] -= mean1[j];
                    } else if (m.standardization == Measure.Standardization.Z_STANDARDIZATION) {
                        data1[i][j] -= mean1[j];
                        data1[i][j] /= sd1[j];
                    }
                    
                    row.add(data1[i][j]);
                    
                    if (!parent.isSingleMode()) {
                        data2[i][j] = SNPEvaluation.evalSNP(correspondingVCFEntry2.pos, m.sinM, m.winM, m.windowsize, parent.contigs2.get(correspondingVCFEntry2.chrom));
                        if (m.standardization == Measure.Standardization.SUBTRACT_MEAN) {
                            data2[i][j] -= mean2[j];
                        } else if (m.standardization == Measure.Standardization.Z_STANDARDIZATION) {
                            data2[i][j] -= mean2[j];
                            data2[i][j] /= sd2[j];
                        }
                        
                        row.add(data2[i][j]);
                        
                        if (parent.comparisons.toApply(j)) {
                            dataC[i][j] = parent.comparisons.function(j).apply(data1[i][j], data2[i][j]);
                            row.add(dataC[i][j]);
                        } else {
                            dataC[i][j] = Double.NaN;
                        }
                    }
                    
                    j++;
                }
                ProtTableEntry pte = SNPEvaluation.getAnnotatedSection(entry.pos, parent.prottable);
                if (pte != null) {
                    row.add(String.valueOf(SNPEvaluation.getCodonPosition(entry.pos, pte)));
                    row.add(pte.locus);
                    row.add(pte.locusTag);
                    row.add(pte.proteinDescription);
                } else {
                    row.add("");
                    row.add("");
                    row.add("");
                    row.add("");
                }
                dtm.addRow(row.toArray());
                i++;
            }
            
            outTab.setModel(dtm);
            
            for (int col = 0; col < outTab.getColumnCount(); col++) {
                int width = 15;
                TableCellRenderer thr = outTab.getTableHeader().getDefaultRenderer();
                Component hComp = thr.getTableCellRendererComponent(outTab, outTab.getColumnModel().getColumn(col).getHeaderValue(), false, false, 0, 0);
                width = Math.max(width, hComp.getPreferredSize().width + 1);
                
                for (int row = 0; row < outTab.getRowCount(); row++) {
                    TableCellRenderer tcr = outTab.getCellRenderer(row, col);
                    Component comp = outTab.prepareRenderer(tcr, row, col);
                    width = Math.max(width, comp.getPreferredSize().width + 3);
                }
                width = Math.min(width, 300);
                outTab.getColumnModel().getColumn(col).setPreferredWidth(width + 5);
            }
            
            pp.paintContent();
            pph.updateSize();
            
            repaint();
            validate();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error.\n\n" + e.getClass().getName() + " " + e.getMessage() + "\n" + SNPEvaluation.stackTraceString(e), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void export(String filename) throws IOException {
        
        BufferedWriter write = new BufferedWriter(new FileWriter(filename + ".tsv", false));
        for (int col = 0; col < outTab.getColumnCount(); col++) {
            write.write(outTab.getColumnName(col));
            if (col < outTab.getColumnCount() - 1) {
                write.write("\t");
            }
        }
        write.newLine();
        
        for (int row = 0; row < outTab.getRowCount(); row++) {
            for (int col = 0; col < outTab.getColumnCount(); col++) {
                write.write(outTab.getValueAt(row, col).toString());
                if (col < outTab.getColumnCount() - 1) {
                    write.write("\t");
                }
            }
            write.newLine();
        }
        write.close();
        
        ImageIO.write(pp.content, "png", new File(filename + "_plot.png"));
    }
    
    public double[][] getData1() {
        return data1;
    }
    
    public double[][] getData2() {
        return data2;
    }
    
    public double[][] getDataC() {
        return dataC;
    }
    
    public void setRowFilter(RowFilter<DefaultTableModel, Integer> rf) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) outTab.getModel());
        sorter.setRowFilter(rf);
        outTab.setRowSorter(sorter);

        //mySNPTable.entries = myOrigSNPTable.filterEntriesAccordingTo(outTab);
        evaluate();
    }
    
    public String[] getColumnNames() {
        String[] names = new String[outTab.getColumnCount()];
        for (int i = 0; i < names.length; i++) {
            names[i] = outTab.getColumnName(i);
        }
        return names;
    }
    
    public String[] getNumericalColumnNames() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < outTab.getColumnCount(); i++) {
            if (outTab.getColumnClass(i) == Integer.class || outTab.getColumnClass(i) == Double.class) {
                names.add(outTab.getColumnName(i));
            }
        }
        return names.toArray(new String[0]);
    }
    
    public double[] getColumnData(int col) {
        if (outTab.getColumnClass(col) == Integer.class || outTab.getColumnClass(col) == Double.class) {
            double[] data = new double[outTab.getRowCount()];
            
            for (int row = 0; row < data.length; row++) {
                data[row] = ((Number) outTab.getValueAt(row, col)).doubleValue();
            }
            
            return data;
        } else {
            return new double[0];
        }
    }
    
    public double[] getColumnData(String colS) {
        for (int col = 0; col < outTab.getColumnCount(); col++) {
            if (outTab.getColumnName(col).equals(colS)) {
                return getColumnData(col);
            }
        }
        return new double[0];
    }
    
    public boolean positionIsShown(int pos) {
        int col;
        for (col = 0; col < outTab.getColumnCount(); col++) {
            if (outTab.getColumnName(col).equals("Position")) {
                break;
            }
        }
        
        for (int row = 0; row < outTab.getRowCount(); row++) {
            if (outTab.getValueAt(row, col).equals(pos)) {
                return true;
            }
        }
        return false;
    }
    
    public int[][] getDrawFields() {
        int[][] fields = new int[2][outTab.getColumnCount()];
        Arrays.fill(fields[0], -1);
        Arrays.fill(fields[1], -1);
        int fieldPos = 0;
        for (int col = 0; col < outTab.getColumnCount(); col++) {
            if (outTab.getColumnClass(col) == Integer.class
                    || outTab.getColumnClass(col) == Double.class) {
                
                boolean exists = false;
                for (int i = 0; i < fieldPos; i++) {
                    if (fields[0][i] >= 0 && sameBesidesVcf(outTab.getColumnName(fields[0][i]), outTab.getColumnName(col))) {
                        fields[1][i] = col;
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    fields[0][fieldPos] = col;
                    fieldPos++;
                }
            }
        }
        
        int col;
        for (col = fields[0].length - 1; col >= 0; col--) {
            if (fields[0][col] >= 0) {
                break;
            }
        }
        
        int[][] fieldsShort = new int[2][];
        fieldsShort[0] = Arrays.copyOf(fields[0], col + 1);
        fieldsShort[1] = Arrays.copyOf(fields[1], col + 1);
        
        return fieldsShort;
    }
    
    public boolean sameBesidesVcf(String s1, String s2) {
        if (s1.startsWith(parent.vcfAlias1)) {
            s1 = s1.substring(parent.vcfAlias1.length());
        } else if (s1.startsWith(parent.vcfAlias2)) {
            s1 = s1.substring(parent.vcfAlias2.length());
        }
        
        if (s2.startsWith(parent.vcfAlias1)) {
            s2 = s2.substring(parent.vcfAlias1.length());
        } else if (s2.startsWith(parent.vcfAlias2)) {
            s2 = s2.substring(parent.vcfAlias2.length());
        }
        
        return s1.equals(s2);
    }
    
    public JTable getOutTab() {
        return outTab;
    }
    
    public void setOrientation(int o) {
        jSplitPane1.setOrientation(o);
        jSplitPane1.setDividerLocation(-1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        tabScroll = new javax.swing.JScrollPane();
        outTab = new javax.swing.JTable();
        plotPanel = new javax.swing.JPanel();
        plotHeadScroll = new javax.swing.JScrollPane();
        plotScroll = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jSplitPane1.setResizeWeight(0.5);

        outTab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        outTab.setGridColor(new java.awt.Color(198, 198, 198));
        outTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                outTabMouseClicked(evt);
            }
        });
        tabScroll.setViewportView(outTab);

        jSplitPane1.setTopComponent(tabScroll);

        plotPanel.setLayout(new java.awt.BorderLayout());

        plotHeadScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        plotPanel.add(plotHeadScroll, java.awt.BorderLayout.PAGE_START);
        plotPanel.add(plotScroll, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(plotPanel);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void outTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_outTabMouseClicked
        if (evt.getClickCount() == 2) {
            int index = outTab.getSelectedRow();
            int y = 0 + index * pp.getPlotHeight();
            Rectangle rect = new Rectangle(0, y, pp.getWidth(), pp.getPlotHeight());
            pp.highlight(rect);
            pp.scrollRectToVisible(rect);
        }
    }//GEN-LAST:event_outTabMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable outTab;
    private javax.swing.JScrollPane plotHeadScroll;
    private javax.swing.JPanel plotPanel;
    private javax.swing.JScrollPane plotScroll;
    private javax.swing.JScrollPane tabScroll;
    // End of variables declaration//GEN-END:variables
}
