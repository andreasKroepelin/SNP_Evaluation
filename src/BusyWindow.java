/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import javax.swing.JFrame;

/**
 *
 * @author kroepelin
 */
public class BusyWindow extends javax.swing.JFrame implements Runnable {

    boolean running;
    int state;

    /**
     * Creates new form BusyWindow
     */
    public BusyWindow(JFrame parent, String message) {
        initComponents();

        setLocationRelativeTo(parent);

        taskL.setText(message);
        busyL.setText("BUSY...");

        running = false;
        state = 0;

        pack();
        start();
        setVisible(true);
    }

    @Override
    public void run() {
        String[] dots = {"", ".", "..", "..."};
        while (running) {
            System.out.println(state);
            busyL.setText("BUSY" + dots[state]);
            repaint();

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }

            state++;
            state = state % dots.length;
        }
    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
        dispose();
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

        busyL = new javax.swing.JLabel();
        taskL = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        busyL.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        busyL.setText("BUSY...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        getContentPane().add(busyL, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        getContentPane().add(taskL, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel busyL;
    private javax.swing.JLabel taskL;
    // End of variables declaration//GEN-END:variables
}
