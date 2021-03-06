/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import javax.swing.event.HyperlinkEvent;

/**
 *
 * @author guestmpi
 */
public class HelpDialog extends javax.swing.JDialog {

    /**
     * Creates new form HelpDialog
     */
    public HelpDialog(Surface parent, boolean modal) {
        super(parent, modal);
        initComponents();

        try {
            String filename = "/snpevaluation/snpeval_quickguide.html";
            BufferedReader read = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));
            String content = "";
            String line;
            while ((line = read.readLine()) != null) {
                content += line;
            }
            read.close();

            String iconsrc = getClass().getResource("/snpevaluation/iconBig.png").toString();
            String plussrc = getClass().getResource("/snpevaluation/plus.png").toString();
            String gosrc = getClass().getResource("/snpevaluation/go.png").toString();
            content = content.replaceAll("ICONSRC", iconsrc);
            content = content.replaceAll("PLUSSRC", plussrc);
            content = content.replaceAll("GOSRC", gosrc);

            contentEp.setContentType("text/html;charset=UTF-8");
            contentEp.setText(content);
            contentEp.setCaretPosition(0);
        } catch (Exception e) {
            System.out.println(SNPEvaluation.stackTraceString(e));
        }

        setVisible(true);
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
        contentEp = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Help");
        setPreferredSize(new java.awt.Dimension(600, 700));
        getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 500));

        contentEp.setEditable(false);
        contentEp.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentEp.setMinimumSize(new java.awt.Dimension(100, 200));
        contentEp.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                contentEpHyperlinkUpdate(evt);
            }
        });
        jScrollPane1.setViewportView(contentEp);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void contentEpHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_contentEpHyperlinkUpdate
        if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(evt.getURL().toURI());
                } catch (IOException | URISyntaxException | NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_contentEpHyperlinkUpdate


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane contentEp;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
