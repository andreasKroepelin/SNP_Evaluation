/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author guestmpi
 */
public class CorrelationsDialog extends javax.swing.JDialog {

    Surface parent;
    String[] mNames;
    double[][] dataUni;
    double[][] dataSha;
    DotplotPanel dpp;
    int m1;
    int m2;

    /**
     * Creates new form CorrelationsDialog
     */
    public CorrelationsDialog(Surface parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.parent = parent;
        mNames = parent.getTab(0).getNumericalColumnNames();

        m1CoB.setModel(new DefaultComboBoxModel(mNames));
        m2CoB.setModel(new DefaultComboBoxModel(mNames));
        usCoB.setModel(new DefaultComboBoxModel(parent.getTabNames()));
        contL.setListData(parent.getTabNames());

        dpp = new DotplotPanel(this);
        jScrollPane1.setViewportView(dpp);
        setVisible(true);
    }

    public void writeDotPlotPosition(double m1Val, double m2Val) {
        try {
            m1PosL.setText(mNames[m1] + ": " + m1Val);
            m2PosL.setText(mNames[m2] + ": " + m2Val);
        } catch (Exception e) {
        }
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

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        m1CoB = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        m2CoB = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        usCoB = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        coeffCoB = new javax.swing.JComboBox();
        goB = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        corrTf = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        contL = new javax.swing.JList<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        m1PosL = new javax.swing.JLabel();
        m2PosL = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Calculate Correlations");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Calculate correlation between");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(m1CoB, gridBagConstraints);

        jLabel2.setText("and");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(m2CoB, gridBagConstraints);

        jLabel3.setText("in");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(usCoB, gridBagConstraints);

        jLabel4.setText("using");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(jLabel4, gridBagConstraints);

        coeffCoB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Correlation Coefficient (Pearson)" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(coeffCoB, gridBagConstraints);

        goB.setText("Go!");
        goB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(goB, gridBagConstraints);

        jLabel5.setText("Correlation:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(jLabel5, gridBagConstraints);

        corrTf.setEditable(false);
        corrTf.setColumns(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(corrTf, gridBagConstraints);

        jLabel6.setText("Dotplot contains:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(jLabel6, gridBagConstraints);

        jScrollPane2.setViewportView(contL);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel2.add(jScrollPane2, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);
        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        m1PosL.setText("Measure 1");

        m2PosL.setText("Measure 2");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(m1PosL)
                    .addComponent(m2PosL))
                .addContainerGap(1121, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(m1PosL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m2PosL)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goBActionPerformed
        try {
            int coeff = coeffCoB.getSelectedIndex();
            m1 = m1CoB.getSelectedIndex();
            m2 = m2CoB.getSelectedIndex();
            double[] dataM1;
            double[] dataM2;

            dataM1 = parent.getTab(usCoB.getSelectedIndex()).getColumnData(m1CoB.getSelectedItem().toString());
            dataM2 = parent.getTab(usCoB.getSelectedIndex()).getColumnData(m2CoB.getSelectedItem().toString());

            if (coeff == 0) {
                double m1Mean = 0;
                for (int i = 0; i < dataM1.length; i++) {
                    m1Mean += dataM1[i];
                }
                m1Mean /= (double) dataM1.length;

                double m2Mean = 0;
                for (int i = 0; i < dataM2.length; i++) {
                    m2Mean += dataM2[i];
                }
                m2Mean /= (double) dataM2.length;

                double cov = 0;
                for (int i = 0; i < dataM1.length; i++) {
                    cov += (dataM1[i] - m1Mean) * (dataM2[i] - m2Mean);
                }

                double m1Var = 0;
                for (int i = 0; i < dataM1.length; i++) {
                    m1Var += (dataM1[i] - m1Mean) * (dataM1[i] - m1Mean);
                }
                double m2Var = 0;
                for (int i = 0; i < dataM2.length; i++) {
                    m2Var += (dataM2[i] - m2Mean) * (dataM2[i] - m2Mean);
                }

                double corr = cov / Math.sqrt(m1Var * m2Var);

                corrTf.setText(corr + "");
                corrTf.setCaretPosition(0);

            }

            ArrayList<Integer> ind = new ArrayList<>();
            for (int i : contL.getSelectedIndices()) {
                ind.add(i);
            }

            double[][] datasM1 = new double[contL.getModel().getSize()][];
            double[][] datasM2 = new double[contL.getModel().getSize()][];
            for (int k = 0; k < datasM1.length; k++) {
                if (ind.contains(k)) {
                    datasM1[k] = parent.getTab(k).getColumnData(m1CoB.getSelectedItem().toString());
                    datasM2[k] = parent.getTab(k).getColumnData(m2CoB.getSelectedItem().toString());
                } else {
                    datasM1[k] = null;
                    datasM2[k] = null;
                }
            }

            dpp.drawDotplot(datasM1, datasM2, m1, m2, mNames);
            revalidate();
            repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error.\n\n" + e.getClass().getName() + " " + e.getMessage() + "\n" + SNPEvaluation.stackTraceString(e), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_goBActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox coeffCoB;
    private javax.swing.JList<String> contL;
    private javax.swing.JTextField corrTf;
    private javax.swing.JButton goB;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox m1CoB;
    private javax.swing.JLabel m1PosL;
    private javax.swing.JComboBox m2CoB;
    private javax.swing.JLabel m2PosL;
    private javax.swing.JComboBox usCoB;
    // End of variables declaration//GEN-END:variables
}
