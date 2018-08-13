/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author guestmpi
 */
public class MeasuresConfigurationDialog extends javax.swing.JDialog {

    ArrayList<Measure> measures;
    DefaultListModel<Measure> dlm;
    Surface parent;

    /**
     * Creates new form MeasuresConfigurationDialog
     */
    public MeasuresConfigurationDialog(Surface parent, boolean modal, ArrayList<Measure> measures) {
        super(parent, modal);
        initComponents();
        this.parent = parent;
        this.measures = measures;

        dlm = new DefaultListModel<>();
        measLi.setModel(dlm);

        this.measures.forEach(m -> dlm.addElement(m));

        String[] sinMs = SNPEvaluation.getSingleMeasureNames();
        sinMCoB.setModel(new DefaultComboBoxModel(sinMs));

        String[] winMs = SNPEvaluation.getWindowMeasureNames();
        winMCoB.setModel(new DefaultComboBoxModel(winMs));

        setVisible(true);
    }

    private boolean isUsed(String name) {
        return !measures.isEmpty() && measures.stream().map(m -> m.name).anyMatch((n) -> (name.equals(n)));
    }

    private void updateNameTf() {
        if (isUsed(nameTf.getText())) {
            nameTf.setBackground(Color.RED);
            nameTf.setToolTipText("already used");
            addB.setEnabled(false);
        } else {
            nameTf.setBackground(Color.WHITE);
            nameTf.setToolTipText("");
            addB.setEnabled(true);
        }
    }

    public String getNameSuggestion() {
        return "";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        measLi = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        addB = new javax.swing.JButton();
        delB = new javax.swing.JButton();
        currMeasL = new javax.swing.JLabel();
        okB = new javax.swing.JButton();
        loadFileB = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        winSizeL = new javax.swing.JLabel();
        winMCoB = new javax.swing.JComboBox();
        winSizeS = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        sinMCoB = new javax.swing.JComboBox();
        nameTf = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        standCB = new javax.swing.JComboBox<>();
        suggB = new javax.swing.JButton();
        saveFileB = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configure Measures...");

        measLi.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        measLi.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                measLiValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(measLi);

        jLabel1.setText("Used Measures:");

        addB.setText("Add Measure");
        addB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBActionPerformed(evt);
            }
        });

        delB.setText("Delete");
        delB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delBActionPerformed(evt);
            }
        });

        okB.setText("OK");
        okB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBActionPerformed(evt);
            }
        });

        loadFileB.setText("Load from File...");
        loadFileB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadFileBActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Create new measure"));

        winSizeL.setText("Window Size: 100");

        winSizeS.setMajorTickSpacing(100);
        winSizeS.setMaximum(500);
        winSizeS.setMinorTickSpacing(10);
        winSizeS.setPaintLabels(true);
        winSizeS.setPaintTicks(true);
        winSizeS.setSnapToTicks(true);
        winSizeS.setValue(100);
        winSizeS.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                winSizeSStateChanged(evt);
            }
        });

        jLabel3.setText("Single Measure");

        jLabel2.setText("Name");

        jLabel4.setText("Window Measure");

        nameTf.setColumns(10);
        nameTf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameTfKeyReleased(evt);
            }
        });

        jLabel7.setText("Standardization:");

        standCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "None", "Subtract mean", "Z-standardize" }));

        suggB.setText("Use name suggestion");
        suggB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suggBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(nameTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(suggB))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(standCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(sinMCoB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(winMCoB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(winSizeS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(winSizeL))))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(winSizeL))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(winMCoB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(winSizeS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sinMCoB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(standCB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(nameTf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(suggB)))
                .addContainerGap(108, Short.MAX_VALUE))
        );

        saveFileB.setText("Save Measures");
        saveFileB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(currMeasL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(delB)
                            .addComponent(addB))
                        .addGap(0, 95, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(loadFileB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveFileB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(okB)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(delB)
                        .addGap(45, 45, 45))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(2, 2, 2)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(currMeasL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(okB)
                            .addComponent(loadFileB)
                            .addComponent(saveFileB))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void winSizeSStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_winSizeSStateChanged
        winSizeL.setText("Window Size: " + winSizeS.getValue());
    }//GEN-LAST:event_winSizeSStateChanged

    private void measLiValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_measLiValueChanged
        if (measLi.getSelectedValue() != null) {
            Measure selM = (Measure) measLi.getSelectedValue();
            measLi.setToolTipText(selM.getDescription());
            //currMeasL.setText(((Measure) measLi.getSelectedValue()).getDescription());

            nameTf.setText(selM.name);
            sinMCoB.setSelectedItem(selM.getSinMName());
            winMCoB.setSelectedItem(selM.getWinMName());
            winSizeS.setValue(selM.windowsize);
            if (selM.standardization == Measure.Standardization.RAW) {
                standCB.setSelectedIndex(0);
            } else if (selM.standardization == Measure.Standardization.SUBTRACT_MEAN) {
                standCB.setSelectedIndex(1);
            } else {
                standCB.setSelectedIndex(2);
            }
            updateNameTf();
        }
    }//GEN-LAST:event_measLiValueChanged

    private void addBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBActionPerformed
        String name = nameTf.getText();
        if (!isUsed(name)) {
            SNPEvaluation.SingleMeasure sinM = SNPEvaluation.getSingleMeasure(sinMCoB.getSelectedItem().toString());
            SNPEvaluation.WindowMeasure winM = SNPEvaluation.getWindowMeasure(winMCoB.getSelectedItem().toString());
            int windowsize = winSizeS.getValue();
            Measure.Standardization standardization;
            if (standCB.getSelectedIndex() == 0) {
                standardization = Measure.Standardization.RAW;
            } else if (standCB.getSelectedIndex() == 1) {
                standardization = Measure.Standardization.SUBTRACT_MEAN;
            } else {
                standardization = Measure.Standardization.Z_STANDARDIZATION;
            }

            Measure m = new Measure(sinM, winM, windowsize, name, standardization);
            measures.add(m);
            dlm.addElement(m);
        }

        updateNameTf();
    }//GEN-LAST:event_addBActionPerformed

    private void delBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delBActionPerformed
        if (measLi.getSelectedIndex() >= 0) {
            measures.remove((Measure) measLi.getSelectedValue());
            dlm.remove(measLi.getSelectedIndex());
            updateNameTf();
        }
    }//GEN-LAST:event_delBActionPerformed

    private void okBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBActionPerformed
        parent.measures = this.measures;
        this.dispose();
    }//GEN-LAST:event_okBActionPerformed

    private void loadFileBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadFileBActionPerformed
        JFileChooser fc = new JFileChooser(SNPEvaluation.getDefaultPath());
        fc.setDialogTitle("Load Measures");
        fc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".measures");
            }

            @Override
            public String getDescription() {
                return "Measure files";
            }
        });
        fc.setAcceptAllFileFilterUsed(false);

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            SNPEvaluation.setDefaultPath(fc.getSelectedFile().getParent());

            try {
                ArrayList<Measure> newMs = new ArrayList<>();
                dlm.clear();

                BufferedReader read = new BufferedReader(new FileReader(file));
                String line = read.readLine();
                while (line != null && !line.isEmpty()) {
                    String[] separated = line.split("\t");
                    String name = separated[0];
                    SNPEvaluation.SingleMeasure sinM = SNPEvaluation.getSingleMeasure(separated[1]);
                    SNPEvaluation.WindowMeasure winM = SNPEvaluation.getWindowMeasure(separated[2]);
                    int windowsize = Integer.parseInt(separated[3]);
                    String standS = separated[4];
                    Measure.Standardization standardization;
                    if (standS.equals("Z_STANDARDIZATION")) {
                        standardization = Measure.Standardization.Z_STANDARDIZATION;
                    } else if (standS.equals("SUBTRACT_MEAN")) {
                        standardization = Measure.Standardization.SUBTRACT_MEAN;
                    } else {
                        standardization = Measure.Standardization.RAW;
                    }

                    Measure m = new Measure(sinM, winM, windowsize, name, standardization);
                    newMs.add(m);
                    dlm.addElement(m);

                    line = read.readLine();
                }
                this.measures = newMs;

                read.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error.\n\n" + e.getClass().getName() + " " + e.getMessage() + "\n" + SNPEvaluation.stackTraceString(e), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_loadFileBActionPerformed

    private void saveFileBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFileBActionPerformed
        JFileChooser fc = new JFileChooser(SNPEvaluation.getDefaultPath());
        fc.setDialogTitle("Save Measures");
        fc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".measures");
            }

            @Override
            public String getDescription() {
                return "Measure files";
            }
        });
        fc.setAcceptAllFileFilterUsed(false);
        fc.setSelectedFile(new File(".measures"));

        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            SNPEvaluation.setDefaultPath(fc.getSelectedFile().getParent());

            try {
                BufferedWriter write = new BufferedWriter(new FileWriter(file));

                for (Measure m : measures) {
                    //SNPEvaluation.singleMeasures.
                    write.write(m.name + "\t" + m.getSinMName() + "\t" + m.getWinMName() + "\t" + m.windowsize + "\t" + m.standardization);
                    write.newLine();
                }

                write.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error.\n\n" + e.getClass().getName() + " " + e.getMessage() + "\n" + SNPEvaluation.stackTraceString(e), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_saveFileBActionPerformed

    private void nameTfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameTfKeyReleased
        updateNameTf();
    }//GEN-LAST:event_nameTfKeyReleased

    private void suggBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suggBActionPerformed
        SNPEvaluation.SingleMeasure sinM = SNPEvaluation.getSingleMeasure(sinMCoB.getSelectedItem().toString());
        SNPEvaluation.WindowMeasure winM = SNPEvaluation.getWindowMeasure(winMCoB.getSelectedItem().toString());
        int windowsize = winSizeS.getValue();
        Measure.Standardization standardization;
        if (standCB.getSelectedIndex() == 0) {
            standardization = Measure.Standardization.RAW;
        } else if (standCB.getSelectedIndex() == 1) {
            standardization = Measure.Standardization.SUBTRACT_MEAN;
        } else {
            standardization = Measure.Standardization.Z_STANDARDIZATION;
        }

        String nameSugg = "";

        if (windowsize > 1) {
            if (winM == SNPEvaluation.getWindowMeasure("max")) {
                nameSugg += "max_";
            } else if (winM == SNPEvaluation.getWindowMeasure("min")) {
                nameSugg += "min_";
            } else if (winM == SNPEvaluation.getWindowMeasure("sum")) {
                nameSugg += "sum_";
            } else if (winM == SNPEvaluation.getWindowMeasure("mean")) {
                nameSugg += "mean_";
            } else if (winM == SNPEvaluation.getWindowMeasure("variance")) {
                nameSugg += "var_";
            } else if (winM == SNPEvaluation.getWindowMeasure("SD")) {
                nameSugg += "SD_";
            }
        }

        if (sinM == SNPEvaluation.getSingleMeasure("coverage")) {
            nameSugg += "cov_";
        } else if (sinM == SNPEvaluation.getSingleMeasure("quality")) {
            nameSugg += "qual_";
        } else if (sinM == SNPEvaluation.getSingleMeasure("not covered")) {
            nameSugg += "notCov_";
        } else if (sinM == SNPEvaluation.getSingleMeasure("homoz. SNP")) {
            nameSugg += "homz_";
        } else if (sinM == SNPEvaluation.getSingleMeasure("heteroz. SNP")) {
            nameSugg += "hetz_";
        } else if (sinM == SNPEvaluation.getSingleMeasure("hetero- or homoz. SNP")) {
            nameSugg += "hetHomz_";
        } else if (sinM == SNPEvaluation.getSingleMeasure("abs. ref. support")) {
            nameSugg += "absRefSupp_";
        } else if (sinM == SNPEvaluation.getSingleMeasure("rel. ref. support")) {
            nameSugg += "relRefSupp_";
        }

        nameSugg += windowsize;

        if (standardization == Measure.Standardization.SUBTRACT_MEAN) {
            nameSugg += "_-M";
        } else if (standardization == Measure.Standardization.Z_STANDARDIZATION) {
            nameSugg += "_Z";
        }

        nameTf.setText(nameSugg);
        updateNameTf();
    }//GEN-LAST:event_suggBActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addB;
    private javax.swing.JLabel currMeasL;
    private javax.swing.JButton delB;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadFileB;
    private javax.swing.JList measLi;
    private javax.swing.JTextField nameTf;
    private javax.swing.JButton okB;
    private javax.swing.JButton saveFileB;
    private javax.swing.JComboBox sinMCoB;
    private javax.swing.JComboBox<String> standCB;
    private javax.swing.JButton suggB;
    private javax.swing.JComboBox winMCoB;
    private javax.swing.JLabel winSizeL;
    private javax.swing.JSlider winSizeS;
    // End of variables declaration//GEN-END:variables
}
