/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater.controllers;

import java.awt.Dialog.ModalityType;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import lucel_updater.gui.actions.ExitAction;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

import com.alee.utils.swing.DefaultFileFilterListCellRenderer;

import lucel_updater.Lucel_Updater;
import lucel_updater.gui.actions.HelpAction;
import lucel_updater.gui.actions.SecretAction;
import lucel_updater.gui.frames.DownloadFrame;
import lucel_updater.gui.frames.ImagePanel;
import lucel_updater.gui.frames.RunDialog;
import lucel_updater.gui.frames.SecretDialog;
import lucel_updater.utils.PropertiesUtil;

/**
 *
 * @author NeNiuM
 */
@SuppressWarnings("unused")
public class MainFrame extends javax.swing.JFrame {

	/**
	 * Creates new form MainFrame
	 */
	private MainFrame() {
		try {
			initComponents();

			// also add the related frames here (cause initComponents is read
			// only and handled by Netbeans)
			this.downloadFrame = new DownloadFrame();
			this.downloadFrame.setName("Downloads");
			this.downloadFrame.setVisible(false);
		
			String configInfos = "Detected FTP Url: " + Lucel_Updater.getConfigValue("altisfr.ftp.host") + ":" + Lucel_Updater.getConfigValue("altisfr.ftp.port") + "\r\n";
			configInfos += "Checked game folder: " + Lucel_Updater.getConfigValue("altisfr.foldername") + "\r\n";
			// this.infoPane.setText(configInfos);
			this.getDebugZone().setText(configInfos);

			// secret key listener
			jPanel1.addKeyListener(new SecretAdapter());
			jPanel1.requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the jButton1
	 */
	public javax.swing.JButton getjButton1() {
		return jButton1;
	}

	/**
	 * @param jButton1
	 *            the jButton1 to set
	 */
	public void setjButton1(javax.swing.JButton jButton1) {
		this.jButton1 = jButton1;
	}

	/**
	 * @return the jButton2
	 */
	public javax.swing.JButton getjButton2() {
		return jButton2;
	}

	/**
	 * @param jButton2
	 *            the jButton2 to set
	 */
	public void setjButton2(javax.swing.JButton jButton2) {
		this.jButton2 = jButton2;
	}

	/**
	 * @return the jButton3
	 */
	public javax.swing.JButton getjButton3() {
		return jButton3;
	}

	/**
	 * @param jButton3
	 *            the jButton3 to set
	 */
	public void setjButton3(javax.swing.JButton jButton3) {
		this.jButton3 = jButton3;
	}

	/**
	 * @return the fcPath
	 */
	public String getFcPath() {
		return fcPath;
	}

	/**
	 * @param fcPath
	 *            the fcPath to set
	 */
	public void setFcPath(String fcPath) {
		this.fcPath = fcPath;
	}

	/**
	 * @return the jProgressBar1
	 */
	public javax.swing.JProgressBar getjProgressBar1() {
		return jProgressBar1;
	}

	/**
	 * @param jProgressBar1
	 *            the jProgressBar1 to set
	 */
	public void setjProgressBar1(javax.swing.JProgressBar jProgressBar1) {
		this.jProgressBar1 = jProgressBar1;
	}

	class SecretAdapter extends KeyAdapter {
		KeyEvent[] events = new KeyEvent[10];

		public void keyPressed(KeyEvent evt) {
			if (evt.getKeyCode() == KeyEvent.VK_UP) {
				if (events[0] == null)
					events[0] = evt;
				else
					events[1] = evt;
			} else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
				if (events[2] == null)
					events[2] = evt;
				else
					events[3] = evt;
			} else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
				if (events[4] == null)
					events[4] = evt;
				else
					events[6] = evt;
			} else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (events[5] == null)
					events[5] = evt;
				else
					events[7] = evt;
			} else if (evt.getKeyCode() == KeyEvent.VK_B) {
				events[8] = evt;
			} else if (evt.getKeyCode() == KeyEvent.VK_A) {
				events[9] = evt;
				SecretDialog s = new SecretDialog(MainFrame.getInstance(), true);
				MainFrame instance = MainFrame.getInstance();
				s.setLocation(instance.getX() + (instance.getWidth() / 2) - s.getWidth() / 2, instance.getY() + (instance.getHeight() / 2) - s.getHeight() / 2);
				s.setVisible(true);
			}
		}
	}

	private static MainFrame INSTANCE = new MainFrame();
	private DownloadFrame downloadFrame;
	private String fcPath;

	public static MainFrame getInstance() {
		return INSTANCE;
	}

	private JLabel altisRoot;
	protected boolean setUpdateRunning = false;

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel1 = new ImagePanel("/resources/images/back2.jpg");
		jButton1 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jScrollPane4 = new javax.swing.JScrollPane();
		jTextPane2 = new javax.swing.JTextPane();
		jProgressBar1 = new javax.swing.JProgressBar();
		jProgressBar1.setStringPainted(true);
		jProgressBar1.setValue(0);
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		jMenuItem3 = new javax.swing.JMenuItem();
		jMenu4 = new javax.swing.JMenu();
		jMenuItem6 = new javax.swing.JMenuItem();
		jMenu2 = new javax.swing.JMenu();
		jMenuItem5 = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("AltisLifeFR Updater v0.4");
		setBackground(new java.awt.Color(255, 255, 255));
		setResizable(false);
		getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jPanel1.setFocusTraversalPolicyProvider(true);

		jButton1.setText("Arma 3 folder");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jButton3.setText("Update !");
		jButton3.setEnabled(false);
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		jButton2.setText("Launch !");
		jButton2.setEnabled(false);
		jButton2.setOpaque(false);
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel1Layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jButton3)
						.addGap(18, 18, 18).addComponent(jButton2).addContainerGap(316, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				jPanel1Layout
						.createSequentialGroup()
						.addContainerGap(177, Short.MAX_VALUE)
						.addGroup(
								jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jButton1)
										.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton3).addComponent(jButton2))).addGap(25, 25, 25)));

		getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, 225));

		jTextPane2.setEditable(false);
		jTextPane2.setText("Debug: ============================================================================== ");
		jScrollPane4.setViewportView(jTextPane2);

		getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 650, 90));
		getContentPane().add(jProgressBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 650, 40));

		jMenuBar1.setBackground(new java.awt.Color(255, 255, 255));

		jMenu1.setText("Fichier");

		jMenuItem3.setText("Quitter");
		jMenuItem3.setIconTextGap(0);
		jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem3ActionPerformed(evt);
			}
		});
		jMenu1.add(jMenuItem3);

		jMenuBar1.add(jMenu1);

		jMenu4.setText("Affichage");

		jMenuItem6.setText("Afficher les téléchargements");
		jMenuItem6.setEnabled(true);
		jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem6ActionPerformed(evt);
			}
		});
		jMenu4.add(jMenuItem6);

		jMenuBar1.add(jMenu4);

		jMenu2.setText("A propos");

		jMenuItem5.setText("Aide");
		jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem5ActionPerformed(evt);
			}
		});
		jMenu2.add(jMenuItem5);

		jMenuBar1.add(jMenu2);

		setJMenuBar(jMenuBar1);

		//handle the arma 3 existing folder in configuration
		if(Lucel_Updater.config.containsKey("altisfr.armaFolder"))
		{
			getAltisRoot().setText(Lucel_Updater.config.getProperty("altisfr.armaFolder"));
			setFcPath(Lucel_Updater.config.getProperty("altisfr.armaFolder"));
			getjButton3().setEnabled(true);
		}
			
		pack();
		setLocationRelativeTo(null);
	}// </editor-fold>//GEN-END:initComponents

	private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem3ActionPerformed
		ExitAction e = new ExitAction();
		e.actionPerformed(evt);
	}// GEN-LAST:event_jMenuItem3ActionPerformed

	private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem4ActionPerformed
		final java.awt.event.ActionEvent e = evt;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SecretAction s = new SecretAction();
				s.actionPerformed(e);
			}
		});
	}// GEN-LAST:event_jMenuItem4ActionPerformed

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed

		BackgroundTask bt = new BackgroundTask();
		bt.execute();

		// TestThread t = new TestThread();
		// t.start();
	}// GEN-LAST:event_jButton1ActionPerformed

	class BackgroundTask extends SwingWorker<String, Object> {
		@Override
		public String doInBackground() {
			// Create a file chooser
			final JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setFileFilter(new FileFilter(){

				@Override
				public boolean accept(File f) {
					String fName = f.getName().toUpperCase();
	                if (f.isDirectory()) {
	                    return true;
	                } else {
	                    return false;   
	                }
				}

				@Override
				public String getDescription() {
					return "Arma 3 root folder";
				}
				
			});
			// In response to a button click:
			int returnVal = fc.showOpenDialog(MainFrame.getInstance());
			if (returnVal == 0) {
				if(Lucel_Updater.config.containsKey("altisfr.armaFolder")){
					Lucel_Updater.config.setProperty("altisfr.armaFolder", fc.getSelectedFile().getAbsolutePath());
					try {
						PropertiesUtil.store(Lucel_Updater.config, "config.properties" );
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					try {
						//store the arma folder
						Lucel_Updater.config.put("altisfr.armaFolder", fc.getSelectedFile().getAbsolutePath());
						PropertiesUtil.store(Lucel_Updater.config, "config.properties");
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				getAltisRoot().setText(fc.getSelectedFile().getAbsolutePath());
				setFcPath(fc.getSelectedFile().getAbsolutePath());
			}
			return "";
		}

		@Override
		protected void done() {
			MainFrame.getInstance().addDebugZoneRow("Ready to update.");
			MainFrame.getInstance().getjButton3().setEnabled(true);
		}
	}

	private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem5ActionPerformed
		final java.awt.event.ActionEvent e = evt;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				HelpAction s = new HelpAction();
				s.actionPerformed(e);
			}
		});
	}// GEN-LAST:event_jMenuItem5ActionPerformed

	private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem6ActionPerformed
		MainFrame.getInstance().getDownloadFrame().setVisible(true);
	}// GEN-LAST:event_jMenuItem6ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		// launch arma 3 exec

		try {
			RunDialog rd = new RunDialog(MainFrame.getInstance(), true);
			MainFrame instance = MainFrame.getInstance();
			rd.setLocation(instance.getX() + (instance.getWidth() / 2) - rd.getWidth() / 2, instance.getY() + (instance.getHeight() / 2) - rd.getHeight() / 2);
			rd.setVisible(true);
			if (rd.getReturnStatus() == RunDialog.getRET_OK()) {
				List<String> params = new ArrayList<String>();
				RunDialog r = RunDialog.getInstance();
				params.add(getFcPath() + "\\arma3.exe");

				if (r.getCb_connect().isSelected()){
					if(Lucel_Updater.config.containsKey("altisfr.ip")){
						//params.add("-connect=" + Lucel_Updater.config.getProperty("altisfr.ip"));
						Lucel_Updater.config.setProperty("altisfr.ip", r.getTxt_connect().getText());
						PropertiesUtil.store(Lucel_Updater.config, "config.properties" );
					}else{
						//params.add("-connect=" + r.getTxt_connect().getText());
						Lucel_Updater.config.put( "altisfr.ip", r.getTxt_connect().getText() );
						PropertiesUtil.store(Lucel_Updater.config, "config.properties" );
					}
					params.add("-connect=" + r.getTxt_connect().getText());
				}
				if (r.getCb_enableht().isSelected())
					params.add("-enableHT");
				if (r.getCb_high().isSelected())
					params.add("-high");
				if (r.getCb_mod().isSelected())
					params.add("-mod="+Lucel_Updater.getConfigValue("altisfr.foldername")+";");
				if (r.getCb_nologs().isSelected())
					params.add("-nologs");
				if (r.getCb_nopause().isSelected())
					params.add("-nopause");
				if (r.getCb_nosplash().isSelected())
					params.add("-nosplash");
				if (r.getCb_password().isSelected()){
					if(Lucel_Updater.config.containsKey("altisfr.password")){
						Lucel_Updater.config.setProperty("altisfr.password", r.getTxt_password().getText());
						PropertiesUtil.store(Lucel_Updater.config, "config.properties" );
					}else{
						Lucel_Updater.config.put( "altisfr.password", r.getTxt_password().getText() );
			            PropertiesUtil.store( Lucel_Updater.config,"config.properties" );
					}
					params.add("-password=" + r.getTxt_password().getText());
				}
				if (r.getCb_skipintro().isSelected())
					params.add("-skipintro");

				Process process = new ProcessBuilder(params).start();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "Arma exec not found. Please contact an admin.\r\nError:\r\n"+e.getMessage());
			e.printStackTrace();
		}

	}// GEN-LAST:event_jButton2ActionPerformed

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
		if(!this.setUpdateRunning){
			ScanBackgroundTask s = new ScanBackgroundTask();
			s.execute();
		}
		this.setUpdateRunning  = true;
	}// GEN-LAST:event_jButton3ActionPerformed

	class ScanBackgroundTask extends SwingWorker<String, Object> {
		@Override
		public String doInBackground() {
//			addDebugZoneRow(MainFrame.getInstance().getFcPath());
			FileCheckerController fcc = new FileCheckerController(MainFrame.getInstance().getFcPath());
			fcc.checkAltisFolder();
			return "";
		}

		@Override
		protected void done() {
			MainFrame.getInstance().addDebugZoneRow("Done.");
			MainFrame.getInstance().setUpdateRunning = false;
			jProgressBar1.setValue(100);
			MainFrame.getInstance().getjButton2().setEnabled(true);
		}
	}

	public JTextPane getDebugZone() {
		return this.jTextPane2;
	}

	public void addDebugZoneRow(String row) {
		this.jTextPane2.setText(this.jTextPane2.getText() + row + "\r\n");
		JScrollBar vertical = this.jScrollPane4.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				MainFrame m = new MainFrame().getInstance();
				m.setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenu jMenu2;
	private javax.swing.JMenu jMenu4;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem jMenuItem3;
	private javax.swing.JMenuItem jMenuItem5;
	private javax.swing.JMenuItem jMenuItem6;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JProgressBar jProgressBar1;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JTextPane jTextPane2;

	// End of variables declaration//GEN-END:variables

	/**
	 * @return the jTextPane2
	 */
	public javax.swing.JTextPane getjTextPane2() {
		return jTextPane2;
	}

	/**
	 * @param jTextPane2
	 *            the jTextPane2 to set
	 */
	public void setjTextPane2(javax.swing.JTextPane jTextPane2) {
		this.jTextPane2 = jTextPane2;
	}

	/**
	 * @return the altisRoot
	 */
	public javax.swing.JLabel getAltisRoot() {
		if (altisRoot == null)
			return new JLabel();
		return altisRoot;
	}

	/**
	 * @param altisRoot
	 *            the altisRoot to set
	 */
	public void setAltisRoot(javax.swing.JLabel altisRoot) {
		this.altisRoot = altisRoot;
	}

	/**
	 * @return the downloadFrame
	 */
	public DownloadFrame getDownloadFrame() {
		return downloadFrame;
	}

	/**
	 * @param downloadFrame
	 *            the downloadFrame to set
	 */
	public void setDownloadFrame(DownloadFrame downloadFrame) {
		this.downloadFrame = downloadFrame;
	}
}
