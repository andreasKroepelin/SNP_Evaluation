/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snpevaluation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.Arrays;
import javax.swing.JTable;

/**
 *
 * @author kroepelin
 */
public class PlotPanel extends javax.swing.JPanel {
    
    static final Color[] COLORS = new Color[]{
        new Color(82, 36, 82),
        new Color(171, 47, 82),
        new Color(229, 93, 87),
        new Color(232, 133, 84),
        new Color(225, 175, 83)
    };
    static Font fontHeader = new Font("Tahoma", Font.BOLD, 13);
    static Font fontValues = new Font("Tahoma", Font.PLAIN, 12);
    BufferedImage content;
    DataViewPanel parent;
    Rectangle hlRect;

    /**
     * Creates new form PlotPanel2
     */
    public PlotPanel(DataViewPanel parent) {
        initComponents();
        
        this.parent = parent;
        this.content = new BufferedImage(1000, 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = content.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, content.getWidth(), content.getHeight());
        
        setVisible(true);
    }
    
    public void paintContent() {
        int[][] fields = parent.getDrawFields();
        JTable tab = parent.getOutTab();
        Graphics2D gr = content.createGraphics();
        
        gr.setFont(fontHeader);
        int maxHeaderLength = Arrays.stream(parent.getColumnNames()).mapToInt(s -> gr.getFontMetrics().stringWidth(s)).max().getAsInt();
        
        Dimension dim = new Dimension(10 + (fields[0].length + 1) * (maxHeaderLength + getXSpace()), getTopSpace() + tab.getRowCount() * getPlotHeight());
        content = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(dim);
        setSize(dim);
        Graphics2D g = content.createGraphics();
        g.setFont(fontHeader);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, content.getWidth(), content.getHeight());
        g.setColor(Color.BLACK);

        //draw background
        boolean even = true;
        for (int h = getTopSpace(); h < content.getHeight(); h += getPlotHeight()) {
            g.setColor(!even ? Color.WHITE : new Color(240, 240, 240));
            g.fillRect(0, h, content.getWidth(), getPlotHeight());
            
            even = !even;
        }

        //Draw Headers
        for (int i = 0; i < fields[0].length; i++) {
            g.setColor(COLORS[i % COLORS.length]);
            if (fields[1][i] == -1) {
                String h1 = tab.getColumnName(fields[0][i]);
                g.drawString(h1, 10 + i * (maxHeaderLength + getXSpace()), 20);
            } else {
                String h1 = tab.getColumnName(fields[0][i]);
                String h2 = tab.getColumnName(fields[1][i]);
                g.drawString(h1, 10 + i * (maxHeaderLength + getXSpace()), 20);
                g.drawString(h2, 10 + i * (maxHeaderLength + getXSpace()), 25 + g.getFontMetrics().getHeight());
            }
        }

        //get max value for each column
        double maxima[] = new double[fields[0].length];
        double minima[] = new double[fields[0].length];
        Arrays.fill(maxima, Double.NEGATIVE_INFINITY);
        Arrays.fill(minima, Double.POSITIVE_INFINITY);
        for (int i = 0; i < fields[0].length; i++) {
            for (int row = 0; row < tab.getRowCount(); row++) {
                double val0 = ((Number) tab.getValueAt(row, fields[0][i])).doubleValue();
                double val1 = fields[1][i] == -1 ? Double.NaN : ((Number) tab.getValueAt(row, fields[1][i])).doubleValue();
                double valMax = Double.isNaN(val1) ? val0 : Math.max(val0, val1);
                double valMin = Double.isNaN(val1) ? val0 : Math.min(val0, val1);
                
                if (maxima[i] < valMax) {
                    maxima[i] = valMax;
                }
                if (minima[i] > valMin) {
                    minima[i] = valMin;
                }
            }
        }

        //draw bars
        for (int i = 0; i < fields[0].length; i++) {
            
            double min = minima[i];
            double max = maxima[i];
            int zero = (int) (-min * maxHeaderLength / (max - min));
            zero = Math.max(zero, 0);
            zero = Math.min(zero, maxHeaderLength);
            
            g = content.createGraphics();
            g.setColor(COLORS[i % COLORS.length]);
            g.translate(10 + i * (maxHeaderLength + getXSpace()), getTopSpace());
            if (zero > 0 && zero < maxHeaderLength) {
                Stroke oldStroke = g.getStroke();
                g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
                g.draw(new Line2D.Double(zero, 0, zero, content.getHeight()));
                g.setStroke(oldStroke);
            }
            
            for (int row = 0; row < tab.getRowCount(); row++) {
                double val0 = ((Number) tab.getValueAt(row, fields[0][i])).doubleValue();
                double val1 = fields[1][i] == -1 ? Double.NaN : ((Number) tab.getValueAt(row, fields[1][i])).doubleValue();
                
                int val0Norm = (int) ((val0 - minima[i]) * maxHeaderLength / (maxima[i] - minima[i]));
                int val1Norm = (int) ((val1 - minima[i]) * maxHeaderLength / (maxima[i] - minima[i]));
                
                g = content.createGraphics();
                g.setFont(fontValues);
                g.setColor(COLORS[i % COLORS.length]);
                g.translate(10 + i * (maxHeaderLength + getXSpace()), getTopSpace() + row * getPlotHeight());
                
                if (!Double.isNaN(val0)) {
                    drawBar(g, val0, val0Norm, zero, 0);
                    
                }
                if (!Double.isNaN(val1)) {
                    drawBar(g, val1, val1Norm, zero, g.getFontMetrics().getHeight() + getBarHeight() + 5);
                }
            }
        }
        
        hlRect = null;
    }
    
    private void drawBar(Graphics2D g, double val, int valNorm, int zero, int yOff) {
        String valS = String.format("%.2f", val);
        if (valS.endsWith(".00") || valS.endsWith(",00")) {
            valS = valS.substring(0, valS.length() - 3);
        }
        g.drawString(valS, 0, g.getFontMetrics().getHeight() + yOff);
        if (valNorm < zero) {
            g.fillRect(valNorm, g.getFontMetrics().getHeight() + 1 + yOff, zero - valNorm, getBarHeight());
        } else {
            g.fillRect(zero, g.getFontMetrics().getHeight() + 1 + yOff, valNorm - zero, getBarHeight());
        }
    }
    
    public BufferedImage getHeaderImage() {
        return content.getSubimage(0, 0, content.getWidth(), getTopSpace());
    }
    
    public int getTopSpace() {
        return 50;
    }
    
    private int getXSpace() {
        return 20;
    }
    
    private int getBarHeight() {
        return 10;
    }
    
    public int getPlotHeight() {
        Graphics2D g = content.createGraphics();
        g.setFont(fontValues);
        return 2 * (g.getFontMetrics().getHeight() + getBarHeight()) + 18;
    }
    
    public void highlight(Rectangle rect) {
        this.hlRect = rect;
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            g.drawImage(content.getSubimage(0, getTopSpace(), content.getWidth(), Math.max(content.getHeight() - getTopSpace(), 1)), 0, 0, null);
        } catch (RasterFormatException rfe) {
        }
        if (hlRect != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(3));
            g2d.draw(hlRect);
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

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if (evt.getClickCount() == 2) {
            int y = evt.getY();
            int index = (y - 0) / getPlotHeight();
            index = Math.max(index, 0);
            index = Math.min(index, parent.getOutTab().getRowCount() - 1);
            
            Rectangle rect = new Rectangle(0, 0 + index * getPlotHeight(), getWidth(), getPlotHeight());
            highlight(rect);
            
            parent.getOutTab().getSelectionModel().setSelectionInterval(index, index);
            Rectangle tabRect = parent.getOutTab().getCellRect(index, 0, true);
            parent.getOutTab().scrollRectToVisible(tabRect);
        }
    }//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
