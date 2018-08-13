/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author guestmpi
 */
public class Surface extends JFrame {

    private boolean singleMode;
    private boolean vertical;
    String vcfFile1;
    String vcfFile2;
    ArrayList<VCFEntry> vcfentries1;
    ArrayList<VCFEntry> vcfentries2;
    HashMap<String, ArrayList<VCFEntry>> contigs1;
    HashMap<String, ArrayList<VCFEntry>> contigs2;
    String vcfAlias1;
    String vcfAlias2;
    String snptableFile;
    String prottableFile;
    SNPTable snptable;
    ArrayList<ProtTableEntry> prottable;
    ArrayList<Measure> measures;
    ComparisonList comparisons;
    ArrayList<String> filters;

    /**
     * Creates new form Surface
     */
    public Surface() {
        initComponents();

        setSingleMode(true);
        vertical = false;
        snptableFile = "";
        vcfFile1 = "";
        vcfFile2 = "";
        vcfAlias1 = "1st vcf file";
        vcfAlias2 = "2nd vcf file";
        snptable = new SNPTable();
        vcfentries1 = new ArrayList<>();
        vcfentries2 = new ArrayList<>();
        contigs1 = new HashMap<>();
        contigs2 = new HashMap<>();
        prottable = new ArrayList<>();
        measures = new ArrayList<>();
        comparisons = new ComparisonList();
        for (Measure m : measures) {
            comparisons.add(false, null, "");
        }

        ToolTipManager.sharedInstance().setDismissDelay(10000);
        ToolTipManager.sharedInstance().setInitialDelay(50);

        //tabsP.setComponentPopupMenu(tabsPM);
        setIconImage(getIconBig().getImage());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        if (SNPEvaluation.isFirstTime()) {
            int option = JOptionPane.showConfirmDialog(this, "Welcome to the SNP Evaluation tool!\nThis seems to be your first time here.\nWould you like to read a short introduction?", "Welcome", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, getIconSmall());
            if (option == JOptionPane.YES_OPTION) {
                new HelpDialog(this, false);
            }
        }

    }

    private void addTab() {
        TabContentDialog tcd = new TabContentDialog(this, true);
        if (tcd.getNewSNPTable() != null) {
            DataViewPanel dvp = new DataViewPanel(this, tcd.getNewSNPTable());
            tabsP.addTab(tcd.getTabName(), dvp);
        }
    }

    public void setSingleMode(boolean singleMode) {
        this.singleMode = singleMode;
        comparisonsMI.setEnabled(!singleMode);
        selVCF2Tf.setEnabled(!singleMode);
        if (singleMode) {
            selVCFL.setText("VCF File:");
            selVCF2Tf.setText("");
            vcfAlias1 = "1st vcf file";
            vcfAlias2 = "2nd vcf file";
        } else {
            selVCFL.setText("VCF Files:");
        }
    }

    public boolean isSingleMode() {
        return singleMode;
    }

    public String getTabName(int i) {
        return tabsP.getTitleAt(i);
    }

    public DataViewPanel getTab(int i) {
        return (DataViewPanel) tabsP.getComponentAt(i);
    }

    public String[] getTabNames() {
        String[] n = new String[tabsP.getTabCount()];
        for (int i = 0; i < n.length; i++) {
            n[i] = tabsP.getTitleAt(i);
        }
        return n;
    }

    public boolean hasTabs() {
        return tabsP.getTabCount() > 0;
    }

    public String[] getMeasureNames() {
        ArrayList<String> n = new ArrayList<>();
        measures.stream().map(m -> m.name).forEachOrdered(n::add);
        return n.toArray(new String[0]);
    }

    public void setRowFilter(RowFilter<DefaultTableModel, Integer> rf) {
        for (int i = 0; i < tabsP.getTabCount(); i++) {
            getTab(i).setRowFilter(rf);
        }
    }

    public ImageIcon getIconBig() {
        return new ImageIcon(getClass().getResource("/snpevaluation/iconBig.png"));
    }

    public ImageIcon getIconSmall() {
        return new ImageIcon(getClass().getResource("/snpevaluation/iconSmall.png"));
    }

    public void evaluate() {
        contigs1 = VCFEntry.decomposeIntoContigs(vcfentries1);
        if (!isSingleMode()) {
            contigs2 = VCFEntry.decomposeIntoContigs(vcfentries2);
        }

        for (int i = 0; i < tabsP.getTabCount(); i++) {
            getTab(i).evaluate();
        }
    }

    public void highlightVCFName(JTextField tf) {
        String path = tf.getText();
        String[] sep = path.split(File.separator);
        String file = sep[sep.length - 1];
        String sample = file.split("_")[0];
        int i0 = path.indexOf(sample);
        int i1 = i0 + sample.length();

        System.out.println(sample + " " + i0 + " " + i1);

        tf.setCaret(new DefaultCaret() {
            private Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.LIGHT_GRAY);
            private boolean isFocused = false;

            @Override
            protected Highlighter.HighlightPainter getSelectionPainter() {
                return painter;
            }

            @Override
            public void setSelectionVisible(boolean hasFocus) {
                if (hasFocus != isFocused) {
                    isFocused = hasFocus;
                    super.setSelectionVisible(false);
                    super.setSelectionVisible(true);
                }
            }
        });

        tf.requestFocusInWindow();
        tf.setSelectionStart(i0);
        tf.setSelectionEnd(i1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tabsPM = new javax.swing.JPopupMenu();
        addTabMI = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        selVCFL = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        selSNPTf = new javax.swing.JTextField();
        selVCF1Tf = new javax.swing.JTextField();
        selProteinTf = new javax.swing.JTextField();
        selVCF2Tf = new javax.swing.JTextField();
        addTabB = new javax.swing.JButton();
        orientationB = new javax.swing.JButton();
        evalB = new javax.swing.JButton();
        tabsP = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        snpTableMI = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        vcfFile1MI = new javax.swing.JMenuItem();
        vcfFile2MI = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        protTabMI = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        confMeasMI = new javax.swing.JMenuItem();
        filtersMI = new javax.swing.JMenuItem();
        comparisonsMI = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        evalMI = new javax.swing.JMenuItem();
        corrMI = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exportMI = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        helpMI = new javax.swing.JMenuItem();
        aboutMI = new javax.swing.JMenuItem();

        addTabMI.setText("Add Tab");
        addTabMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTabMIActionPerformed(evt);
            }
        });
        tabsPM.add(addTabMI);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SNP Evaluation");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected Files"));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("SNP Table:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel1.add(jLabel1, gridBagConstraints);

        selVCFL.setText("VCF Files:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel1.add(selVCFL, gridBagConstraints);

        jLabel3.setText("Protein Table:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

        selSNPTf.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel1.add(selSNPTf, gridBagConstraints);

        selVCF1Tf.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel1.add(selVCF1Tf, gridBagConstraints);

        selProteinTf.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel1.add(selProteinTf, gridBagConstraints);

        selVCF2Tf.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel1.add(selVCF2Tf, gridBagConstraints);

        addTabB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/snpevaluation/plus.png"))); // NOI18N
        addTabB.setText("Add Tab");
        addTabB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTabBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        jPanel1.add(addTabB, gridBagConstraints);

        orientationB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/snpevaluation/leftright.png"))); // NOI18N
        orientationB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orientationBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(orientationB, gridBagConstraints);

        evalB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/snpevaluation/go.png"))); // NOI18N
        evalB.setText("Evaluate!");
        evalB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                evalBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(evalB, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);
        getContentPane().add(tabsP, java.awt.BorderLayout.CENTER);

        jMenu1.setText("Data");

        snpTableMI.setText("Load SNP Table");
        snpTableMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                snpTableMIActionPerformed(evt);
            }
        });
        jMenu1.add(snpTableMI);
        jMenu1.add(jSeparator4);

        vcfFile1MI.setText("Load 1 VCF File");
        vcfFile1MI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vcfFile1MIActionPerformed(evt);
            }
        });
        jMenu1.add(vcfFile1MI);

        vcfFile2MI.setText("Load 2 VCF Files");
        vcfFile2MI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vcfFile2MIActionPerformed(evt);
            }
        });
        jMenu1.add(vcfFile2MI);
        jMenu1.add(jSeparator3);

        protTabMI.setText("Load Protein Table");
        protTabMI.setToolTipText("");
        protTabMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                protTabMIActionPerformed(evt);
            }
        });
        jMenu1.add(protTabMI);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Measures");

        confMeasMI.setText("Configure Measures");
        confMeasMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confMeasMIActionPerformed(evt);
            }
        });
        jMenu2.add(confMeasMI);

        filtersMI.setText("Set Filters");
        filtersMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtersMIActionPerformed(evt);
            }
        });
        jMenu2.add(filtersMI);

        comparisonsMI.setText("Set Comparisons");
        comparisonsMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comparisonsMIActionPerformed(evt);
            }
        });
        jMenu2.add(comparisonsMI);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Evaluation");

        evalMI.setText("Evaluate SNPs");
        evalMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                evalMIActionPerformed(evt);
            }
        });
        jMenu4.add(evalMI);

        corrMI.setText("View Correlations");
        corrMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                corrMIActionPerformed(evt);
            }
        });
        jMenu4.add(corrMI);
        jMenu4.add(jSeparator1);

        exportMI.setText("Export evaluation");
        exportMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMIActionPerformed(evt);
            }
        });
        jMenu4.add(exportMI);

        jMenuBar1.add(jMenu4);

        jMenu3.setText("?");

        helpMI.setText("Help");
        helpMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMIActionPerformed(evt);
            }
        });
        jMenu3.add(helpMI);

        aboutMI.setText("About");
        aboutMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMIActionPerformed(evt);
            }
        });
        jMenu3.add(aboutMI);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void snpTableMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_snpTableMIActionPerformed
        JFileChooser fc = new JFileChooser(SNPEvaluation.getDefaultPath());
        fc.setDialogTitle("Load SNP Table");
        fc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".tsv") || f.getName().toLowerCase().endsWith(".tsv.gz");
            }

            @Override
            public String getDescription() {
                return "(gzipped) tab separated files";
            }
        });
        fc.setAcceptAllFileFilterUsed(false);

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                snptableFile = fc.getSelectedFile().getAbsolutePath();
                snptable = SNPTableParser.parse(snptableFile);
                selSNPTf.setText(fc.getSelectedFile().getAbsolutePath());
                SNPEvaluation.setDefaultPath(fc.getSelectedFile().getParent());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error.\n\n" + e.getClass().getName() + " " + e.getMessage() + "\n" + SNPEvaluation.stackTraceString(e), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_snpTableMIActionPerformed

    private void vcfFile2MIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vcfFile2MIActionPerformed
        setSingleMode(false);
        JFileChooser fc = new JFileChooser(SNPEvaluation.getDefaultPath());

        fc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".vcf") || f.getName().toLowerCase().endsWith(".vcf.gz");
            }

            @Override
            public String getDescription() {
                return "(gziped) VCF files";
            }
        });
        fc.setAcceptAllFileFilterUsed(false);

        fc.setDialogTitle("Load 1st VCF File");
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                vcfFile1 = fc.getSelectedFile().getAbsolutePath();

                vcfentries1 = VCFParser.parse(vcfFile1);
                selVCF1Tf.setText(fc.getSelectedFile().getAbsolutePath());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error.\n\n" + e.getClass().getName() + " " + e.getMessage() + "\n" + SNPEvaluation.stackTraceString(e), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        vcfAlias1 = JOptionPane.showInputDialog(this, "Choose an alias for the 1st vcf file:", "1st vcf file");
        selVCF1Tf.setText("[" + vcfAlias1 + "] " + selVCF1Tf.getText());
        highlightVCFName(selVCF1Tf);

        fc.setDialogTitle("Load 2nd VCF File");
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                vcfFile2 = fc.getSelectedFile().getAbsolutePath();

                vcfentries2 = VCFParser.parse(vcfFile2);
                selVCF2Tf.setText(fc.getSelectedFile().getAbsolutePath());
                SNPEvaluation.setDefaultPath(fc.getSelectedFile().getParent());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error.\n\n" + e.getClass().getName() + " " + e.getMessage() + "\n" + SNPEvaluation.stackTraceString(e), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        vcfAlias2 = JOptionPane.showInputDialog(this, "Choose an alias for the 2nd vcf file:", "2nd vcf file");
        selVCF2Tf.setText("[" + vcfAlias2 + "] " + selVCF2Tf.getText());
        highlightVCFName(selVCF2Tf);
    }//GEN-LAST:event_vcfFile2MIActionPerformed

    private void confMeasMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confMeasMIActionPerformed
        MeasuresConfigurationDialog mcd = new MeasuresConfigurationDialog(this, true, measures);
        //filters = new FilterList(filters.selected());
        comparisons = new ComparisonList();
        for (Measure m : measures) {
            comparisons.add(false, null, null);
        }
    }//GEN-LAST:event_confMeasMIActionPerformed

    private void exportMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMIActionPerformed
        JFileChooser fc = new JFileChooser(SNPEvaluation.getDefaultPath());
        fc.setDialogTitle("Export Evaluation");
        //fc.setSelectedFile(new File(""));

        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            String filename = fc.getSelectedFile().getAbsolutePath();
            if (filename != null) {
                new File(filename).mkdir();
                try {
                    for (int i = 0; i < tabsP.getTabCount(); i++) {
                        getTab(i).export(filename + "/" + tabsP.getTitleAt(i));
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error.\n\n" + e.getClass().getName() + " " + e.getMessage() + "\n" + SNPEvaluation.stackTraceString(e), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            SNPEvaluation.setDefaultPath(fc.getSelectedFile().getParent());
        }
    }//GEN-LAST:event_exportMIActionPerformed

    private void evalMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_evalMIActionPerformed
        evaluate();
    }//GEN-LAST:event_evalMIActionPerformed

    private void filtersMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filtersMIActionPerformed
        //new FilterDialog(this, true);
        FilterDialog fd = new FilterDialog(this, true, filters);
        filters = fd.getDescriptions();
    }//GEN-LAST:event_filtersMIActionPerformed

    private void protTabMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_protTabMIActionPerformed
        JFileChooser fc = new JFileChooser(SNPEvaluation.getDefaultPath());
        fc.setDialogTitle("Load Protein Table");
        fc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt") || f.getName().toLowerCase().endsWith(".tsv");
            }

            @Override
            public String getDescription() {
                return "text files or tsv files";
            }
        });
        fc.setAcceptAllFileFilterUsed(false);

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                prottableFile = fc.getSelectedFile().getAbsolutePath();

                prottable = ProtTableParser.parse(prottableFile);
                selProteinTf.setText(fc.getSelectedFile().getAbsolutePath());
                SNPEvaluation.setDefaultPath(fc.getSelectedFile().getParent());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error.\n\n" + e.getClass().getName() + " " + e.getMessage() + "\n" + SNPEvaluation.stackTraceString(e), "Error", JOptionPane.ERROR_MESSAGE);
            }

        }


    }//GEN-LAST:event_protTabMIActionPerformed

    private void comparisonsMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comparisonsMIActionPerformed
        new ComparisonDialog(this, true);
    }//GEN-LAST:event_comparisonsMIActionPerformed

    private void addTabMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTabMIActionPerformed
        addTab();
    }//GEN-LAST:event_addTabMIActionPerformed

    private void addTabBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTabBActionPerformed
        addTab();
    }//GEN-LAST:event_addTabBActionPerformed

    private void vcfFile1MIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vcfFile1MIActionPerformed
        setSingleMode(true);

        JFileChooser fc = new JFileChooser(SNPEvaluation.getDefaultPath());

        fc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".vcf") || f.getName().toLowerCase().endsWith(".vcf.gz");
            }

            @Override
            public String getDescription() {
                return "(gziped) VCF files";
            }
        });
        fc.setAcceptAllFileFilterUsed(false);

        fc.setDialogTitle("Load VCF File");
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                vcfFile1 = fc.getSelectedFile().getAbsolutePath();

                vcfentries1 = VCFParser.parse(vcfFile1);
                selVCF1Tf.setText(fc.getSelectedFile().getAbsolutePath());
                highlightVCFName(selVCF1Tf);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error.\n\n" + e.getClass().getName() + " " + e.getMessage() + "\n" + SNPEvaluation.stackTraceString(e), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_vcfFile1MIActionPerformed

    private void corrMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_corrMIActionPerformed
        if (hasTabs()) {
            new CorrelationsDialog(this, true);
        }
    }//GEN-LAST:event_corrMIActionPerformed

    private void helpMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMIActionPerformed
        new HelpDialog(this, false);
    }//GEN-LAST:event_helpMIActionPerformed

    private void orientationBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orientationBActionPerformed
        int o = JSplitPane.HORIZONTAL_SPLIT;
        if (vertical) {
            vertical = false;
            orientationB.setIcon(new ImageIcon(getClass().getResource("/snpevaluation/leftright.png")));
            o = JSplitPane.HORIZONTAL_SPLIT;
        } else {
            vertical = true;
            orientationB.setIcon(new ImageIcon(getClass().getResource("/snpevaluation/topbottom.png")));
            o = JSplitPane.VERTICAL_SPLIT;
        }
        for (int i = 0; i < tabsP.getTabCount(); i++) {
            getTab(i).setOrientation(o);
        }
    }//GEN-LAST:event_orientationBActionPerformed

    private void evalBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_evalBActionPerformed
        evaluate();
    }//GEN-LAST:event_evalBActionPerformed

    private void aboutMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMIActionPerformed
        JOptionPane.showMessageDialog(this, "<html><center><b>SNP Evaluation</b><br><hr><br>Andreas Kröpelin, 2017-2018<br><tt>kroepelin@shh.mpg.de</tt><br><br>Max-Planck-Institute<br>for the Science of Human History,<br>Jena<hr></center></html>", "About", JOptionPane.INFORMATION_MESSAGE, getIconBig());
    }//GEN-LAST:event_aboutMIActionPerformed

    private static double printE(VCFEntry e) {
        System.out.println("Is e null? " + (e == null));
        return 0;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMI;
    private javax.swing.JButton addTabB;
    private javax.swing.JMenuItem addTabMI;
    private javax.swing.JMenuItem comparisonsMI;
    private javax.swing.JMenuItem confMeasMI;
    private javax.swing.JMenuItem corrMI;
    private javax.swing.JButton evalB;
    private javax.swing.JMenuItem evalMI;
    private javax.swing.JMenuItem exportMI;
    private javax.swing.JMenuItem filtersMI;
    private javax.swing.JMenuItem helpMI;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JButton orientationB;
    private javax.swing.JMenuItem protTabMI;
    private javax.swing.JTextField selProteinTf;
    private javax.swing.JTextField selSNPTf;
    private javax.swing.JTextField selVCF1Tf;
    private javax.swing.JTextField selVCF2Tf;
    private javax.swing.JLabel selVCFL;
    private javax.swing.JMenuItem snpTableMI;
    private javax.swing.JTabbedPane tabsP;
    private javax.swing.JPopupMenu tabsPM;
    private javax.swing.JMenuItem vcfFile1MI;
    private javax.swing.JMenuItem vcfFile2MI;
    // End of variables declaration//GEN-END:variables
}
