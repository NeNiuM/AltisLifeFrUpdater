/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater.gui.frames;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

import lucel_updater.gui.misc.LucelProgressRenderer;
import lucel_updater.gui.misc.LucelTableRow;
import lucel_updater.models.LucelTableModel;

/**
 *
 * @author ZQTZ1943
 */
public class DownloadFrame extends javax.swing.JFrame {

    /**
     * Creates new form DownloadFrame
     */
    public DownloadFrame() {
        initComponents();
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
        jTable1 = new javax.swing.JTable();

        setName("downloadFrame"); // NOI18N

        jTable1.setModel(new LucelTableModel(
        	new String [] {
                "Fichier local", "Fichier distant", "Taille", "Avancement"
            }
        ));
        
        jTable1.setColumnSelectionAllowed(false);
        LucelProgressRenderer rend = new LucelProgressRenderer();
        rend.prog.setStringPainted(true);
        rend.prog.setString("0 %");
        jTable1.getColumnModel().getColumn(3).setCellRenderer(rend);
        jTable1.setFillsViewportHeight(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
        );
       
        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DownloadFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DownloadFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DownloadFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DownloadFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               DownloadFrame d = new DownloadFrame();
               d.setVisible(true);
               d.setResizable(true);
               d.addTableRow("TO FILE", "FROM FILE", (long) 11111.11);//, progress);
//               d.setDownloadProgress(0, 234, 518);
               d.jTable1.getModel().setValueAt((long)75, 0, 3);
            }
        });
    }
    
    public int addTableRow(String to, String from, long size){
        LucelTableModel model = (LucelTableModel) this.jTable1.getModel();
        
        Double displaySizeK = (size / 1024.00);
		Double displaySizeM = (size / 1024.00 / 1024.00);
		Double displaySize;
		String unity;
		if (size < 9999) {
			displaySize = displaySizeK;
			unity = "Ko";
		} else {
			displaySize = displaySizeM;
			unity = "Mo";
		}
		
//		ArrayList<String> row = new ArrayList<String>();
//		row.addAll(Arrays.asList(to, from, String.format("%.2f", displaySize) + " " + unity));//, progress));
		LucelTableRow row = new LucelTableRow(from, to, String.format("%.2f", displaySize) + " " + unity);
        return model.addRow(row);
    }

    public javax.swing.JTable getjTable1() {
		return jTable1;
	}

	public void setjTable1(javax.swing.JTable jTable1) {
		this.jTable1 = jTable1;
	}

	public void setSpeed(long speed){
    	this.setTitle(speed + " KB/s");
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    
	
	public void setDownloadProgress(int rowNumber, long done, long total) {
		LucelTableModel model = (LucelTableModel) this.jTable1.getModel();
		
		long percent = (100 * done) / total; 
		
		model.setValueAt(percent, rowNumber, 3);
    	model.fireTableCellUpdated(rowNumber, 3);
	}
}
