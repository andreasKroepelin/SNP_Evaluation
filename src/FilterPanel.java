/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import java.awt.Color;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kroepelin
 */
public class FilterPanel extends javax.swing.JPanel {

    Pattern pattern;

    /**
     * Creates new form FilterPanel
     */
    public FilterPanel(String[] colNames) {
        initComponents();

        pattern = Pattern.compile(".*");
        colCoB.setModel(new DefaultComboBoxModel<>(colNames));
        contTf.setComponentPopupMenu(contTfPM);
    }

    private static boolean isValidRegex(String regex) {
        try {
            Pattern.compile(regex);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSelected() {
        return useChB.isSelected();
    }

    public String getColumn() {
        return colCoB.getSelectedItem().toString();
    }

    public boolean isNegated() {
        return notChB.isSelected();
    }

    public String getOperator() {
        return opCoB.getSelectedItem().toString();
    }

    public String getContent() {
        return contTf.getText();
    }

    public String getDescription() {
        return (isSelected() ? "use\t" : "ignore\t")
                + getColumn() + "\t"
                + (isNegated() ? "not\t" : "\t")
                + getOperator() + "\t"
                + getContent();
    }

    public void initFromDescription(String description) {
        String[] parts = description.split("\t");
        useChB.setSelected(parts[0].equals("use"));
        colCoB.setSelectedItem(parts[1]);
        notChB.setSelected(parts[2].equals("not"));
        opCoB.setSelectedItem(parts[3]);
        contTf.setText(parts[4]);
    }

    public Predicate<RowFilter.Entry<DefaultTableModel, Integer>> getTester() {
        return e -> {
            try {
                if (!isSelected()) {
                    return true;
                }

                int col;
                for (col = 0; col < e.getValueCount(); col++) {
                    if (e.getModel().getColumnName(col).equals(getColumn())) {
                        break;
                    }
                }

                String val = e.getStringValue(col);
                boolean ok = true;

                if (opCoB.getSelectedItem().equals("=")) {
                    try {
                        double valD = Double.parseDouble(val);
                        double contD = Double.parseDouble(getContent());
                        ok = valD == contD;
                    } catch (NumberFormatException nfe) {
                        ok = val.equals(getContent());
                    }

                } else if (opCoB.getSelectedItem().equals("!=")) {
                    try {
                        double valD = Double.parseDouble(val);
                        double contD = Double.parseDouble(getContent());
                        ok = valD != contD;
                    } catch (NumberFormatException nfe) {
                        ok = !val.equals(getContent());
                    }

                } else if (opCoB.getSelectedItem().equals("<")) {
                    double valD = Double.parseDouble(val);
                    double contD = Double.parseDouble(getContent());
                    ok = valD < contD;

                } else if (opCoB.getSelectedItem().equals(">")) {
                    double valD = Double.parseDouble(val);
                    double contD = Double.parseDouble(getContent());
                    ok = valD > contD;

                } else if (opCoB.getSelectedItem().equals("<=")) {
                    double valD = Double.parseDouble(val);
                    double contD = Double.parseDouble(getContent());
                    ok = valD <= contD;

                } else if (opCoB.getSelectedItem().equals(">=")) {
                    double valD = Double.parseDouble(val);
                    double contD = Double.parseDouble(getContent());
                    ok = valD >= contD;

                } else if (opCoB.getSelectedItem().equals("BETWEEN")) {
                    Pattern num = Pattern.compile("[+-]?\\d+(\\.\\d+)?");
                    Matcher mat = num.matcher(getContent());
                    double val1D = Double.NEGATIVE_INFINITY;
                    double val2D = Double.POSITIVE_INFINITY;
                    if (mat.find()) {
                        String val1 = mat.group();
                        val1D = Double.parseDouble(val1);
                    }
                    if (mat.find(mat.end() + 1)) {
                        String val2 = mat.group();
                        val2D = Double.parseDouble(val2);
                    }
                    double valD = Double.parseDouble(val);
                    ok = val1D <= valD && valD <= val2D;

                } else if (opCoB.getSelectedItem().equals("LIKE")) {
                    ok = Pattern.compile(getContent()).matcher(val).matches();

                } else if (opCoB.getSelectedItem().equals("IN")) {
                    String[] elements = getContent().split(",");
                    ok = false;
                    for (String el : elements) {
                        if (el.equals(val)) {
                            ok = true;
                        }
                    }

                }

                return isNegated() ^ ok;
            } catch (Exception exc) {
                exc.printStackTrace();
                return true;
            }
        };
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

        contTfPM = new javax.swing.JPopupMenu();
        arrowMI = new javax.swing.JMenuItem();
        useChB = new javax.swing.JCheckBox();
        colCoB = new javax.swing.JComboBox<>();
        opCoB = new javax.swing.JComboBox<>();
        contTf = new javax.swing.JTextField();
        notChB = new javax.swing.JCheckBox();

        arrowMI.setText("Insert \u2192");
        arrowMI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrowMIActionPerformed(evt);
            }
        });
        contTfPM.add(arrowMI);

        setLayout(new java.awt.GridBagLayout());

        useChB.setSelected(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(useChB, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(colCoB, gridBagConstraints);

        opCoB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=", "!=", ">", "<", ">=", "<=", "BETWEEN", "LIKE", "IN" }));
        opCoB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opCoBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(opCoB, gridBagConstraints);

        contTf.setColumns(10);
        contTf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                contTfKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        add(contTf, gridBagConstraints);

        notChB.setText("not");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        add(notChB, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void opCoBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opCoBActionPerformed
        try {
            if (opCoB.getSelectedItem().equals("=") || opCoB.getSelectedItem().equals("!=")) {
                pattern = Pattern.compile(".*");
                contTf.setToolTipText("anything");
            } else if (opCoB.getSelectedItem().equals("<") || opCoB.getSelectedItem().equals(">")
                    || opCoB.getSelectedItem().equals(">=") || opCoB.getSelectedItem().equals(">=")) {
                pattern = Pattern.compile("[+-]?\\d+(\\.\\d+)?");
                contTf.setToolTipText("number");
            } else if (opCoB.getSelectedItem().equals("BETWEEN")) {
                pattern = Pattern.compile("[+-]?\\d+(\\.\\d+)?-[+-]?\\d+(\\.\\d+)?");
                contTf.setToolTipText("number-number");
            } else if (opCoB.getSelectedItem().equals("LIKE")) {
                pattern = Pattern.compile(".*");
                contTf.setToolTipText("regex");
            } else if (opCoB.getSelectedItem().equals("IN")) {
                pattern = Pattern.compile("([^,]+,)*[^,]+");
                contTf.setToolTipText("a,b,c,d");
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_opCoBActionPerformed

    private void contTfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contTfKeyReleased
        boolean ok;
        if (opCoB.getSelectedItem().equals("LIKE")) {
            ok = isValidRegex(contTf.getText());
        } else {
            ok = pattern.matcher(contTf.getText()).matches();
        }

        if (ok) {
            contTf.setBackground(Color.WHITE);
        } else {
            contTf.setBackground(Color.RED);
        }
    }//GEN-LAST:event_contTfKeyReleased

    private void arrowMIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowMIActionPerformed
        int cp = contTf.getCaretPosition();
        String cont = contTf.getText();
        contTf.setText(cont.substring(0, cp) + "\u2192" + cont.substring(cp));
    }//GEN-LAST:event_arrowMIActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem arrowMI;
    private javax.swing.JComboBox<String> colCoB;
    private javax.swing.JTextField contTf;
    private javax.swing.JPopupMenu contTfPM;
    private javax.swing.JCheckBox notChB;
    private javax.swing.JComboBox<String> opCoB;
    private javax.swing.JCheckBox useChB;
    // End of variables declaration//GEN-END:variables
}
