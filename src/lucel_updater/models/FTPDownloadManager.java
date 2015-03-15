/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater.models;

import cz.dhl.ftp.Ftp;
import cz.dhl.ftp.FtpFile;
import cz.dhl.ftp.FtpInputStream;
import cz.dhl.io.CoFile;
import cz.dhl.io.CoLoad;
import cz.dhl.io.LocalFile;
import cz.dhl.ui.CoProgress;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import lucel_updater.Lucel_Updater;
import lucel_updater.controllers.MainFrame;

/**
 *
 * @author Shiva
 */
public class FTPDownloadManager {

	// TODO implement a sql connection pool

	Ftp connection;
	String armaR;
	CoFile to;
	CoFile from;
	Ftp[] connectionPool;

	public FTPDownloadManager(String armaRoot) {
		initDownloadManager(armaRoot);
	}

	/**
	 * Start the download manager by instanciating a new FTP connection and
	 * instanciating the pool
	 */
	public void initDownloadManager(String armaRoot) {
		try {
			this.armaR = armaRoot;
			this.connectionPool = new Ftp[5];

			this.connection = new Ftp();
			this.connection.connect(Lucel_Updater.getConfigValue("altisfr.ftp.host"), Integer.valueOf(Lucel_Updater.getConfigValue("altisfr.ftp.port")));
			this.connection.login(Lucel_Updater.getConfigValue("altisfr.ftp.username"), Lucel_Updater.getConfigValue("altisfr.ftp.pass"));

		} catch (IOException ex) {
			Logger.getLogger(FTPDownloadManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public boolean addDownloadToPool(CoFile file, long size) {
		try {

			// Ftp conn = new Ftp();
			// conn.connect(Lucel_Updater
			// .getConfigValue("altisfr.ftp.host"), Integer
			// .valueOf(Lucel_Updater.getConfigValue("altisfr.ftp.port")));
			// conn.login(
			// Lucel_Updater.getConfigValue("altisfr.ftp.username"),
			// Lucel_Updater.getConfigValue("altisfr.ftp.pass"));

			Logger log = Logger.getLogger("MyLog");
			FileHandler fh;
			try {
				URL loc = CoFile.class.getProtectionDomain().getCodeSource().getLocation();
				log.info("Class: " + CoFile.class.getSimpleName() + " loaded from " + loc);

				FtpInputStream is = null;

				CoFile from = new FtpFile(file.getAbsolutePath(), this.connection);
				CoFile to = new LocalFile(this.armaR, file.getAbsolutePath());
				// this.setFile(to, from);

				final int rownumber = MainFrame.getInstance().getDownloadFrame().addTableRow(to.getAbsolutePath(), from.getAbsolutePath(), size);// ,
																																					// "0");

				// MainFrame.getInstance().addPropertyChangeListener(new
				// PropertyChangeListener() {
				//
				// @Override
				// public void propertyChange(PropertyChangeEvent evt) {
				// if (evt.getPropertyName().equals("progress")) {
				// ((LucelTableModel)
				// MainFrame.getInstance().getDownloadFrame().getjTable1().getModel()).updateStatus(rownumber,
				// (int) evt.getNewValue());
				// }
				// }
				// });

				LucelProgress prog = new LucelProgress(rownumber);
				prog.setFile(to, from);
				prog.total = size;
				// prog.setProgress(0);

				boolean ret = CoLoad.copy(to, from, prog);
				if (ret) {
					MainFrame.getInstance().getDownloadFrame().setDownloadProgress(rownumber, 100, 100);
					
					return ret;
				}
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}

	String tolabel;
	String filelabel;
	String progress;
	long done;
	long delay;
	long total;
	boolean abort;

	public void setFile(CoFile newto, CoFile newfile) {
		this.to = newto;
		this.from = newfile;
		done = 0;
		progress = "";
		// total = this.from.length();
		// MainFrame.getInstance().addDebugZoneRow("LENGTH = "+ total);
		delay = 0;
		/*
		 * SINCE 1.3 synchronized (this) { if (repeat != null) repeat.cancel();
		 * repeat=new Timer(); repeat.schedule(new TimerTask() { public void
		 * run() { System.out.println("Timer tick"); } } ,500,500); }
		 */
		// (new Thread(copy)).start();
	}

	Runnable copy = new Runnable() {
		public void run() {
			tolabel = "To: " + to.toString();
			filelabel = "File: " + from.toString();
			if (progress != null)
				if (!from.isDirectory())
					progress = "? of " + total;
				else
					progress = "";
		}

	};

	class LucelProgress implements CoProgress {
		String tolabel;
		String filelabel;
		String progress;
		long done;
		long delay;
		long total;
		boolean abort;
		CoFile to;
		CoFile from;
		int rowNumber;
		long start;

		public LucelProgress(int rownumber) {
			rowNumber = rownumber;
		}

		@Override
		public void setFile(CoFile arg0) {
			this.from = arg0;
			/*
			 * SINCE 1.3 synchronized (this) { if (repeat != null)
			 * repeat.cancel(); repeat = null; }
			 */
			(new Thread(remove)).start();
		}

		Runnable remove = new Runnable() {
			public void run() {

			}
		};

		@Override
		public void setFile(CoFile newto, CoFile newfile) {
			this.to = newto;
			this.from = newfile;
			done = 0;
			total = from.length();
			delay = 0;
			int tick = 0;
			try {
				start = System.currentTimeMillis();// System.nanoTime();
			} catch (Exception e) {
				e.printStackTrace();
			}

			(new Thread(this.copy)).start();
		}

		Runnable copy = new Runnable() {
			public void run() {
				int delay = 1000; // delay for 5 sec.
				int period = 1000; // repeat every sec.

				// Timer timer = new Timer();
				// timer.scheduleAtFixedRate(new TimerTask() {
				// public void run() {
				// Logger.getLogger(this.getClass().getSimpleName()).warning("TOCK");
				// }
				// }, delay, period);
			}
		};

		/*
		 * SINCE 1.3 Timer repeat = null;
		 */
		Runnable status = new Runnable() {
			public void run() {
				if (progress != null)
					Logger.getLogger(this.getClass().getSimpleName()).warning(done + " of " + total + (delay > 0 ? " " + delay + "sec no answer" : ""));
			}
		};
		private int tick = 0;
		private int tickSize = 0;

		/** Thread safe. */
		public void setProgress(int increment) {
			done += increment;
			String prog = done + " of " + total + (delay > 0 ? " " + delay + "sec no answer" : "");
			MainFrame.getInstance().getDownloadFrame().setDownloadProgress(rowNumber, done, total);
			try {
				if (tick == 5) {
					long end = System.currentTimeMillis();// System.nanoTime();
					long estimatedTime = (end - start) / 1000;
					// long sec = TimeUnit.SECONDS.convert((long)(end-start),
					// TimeUnit.NANOSECONDS);
					long sec = estimatedTime;
					long speed = tickSize / sec;
					// speed = speed * 1000 / 1024;
					Logger.getLogger(this.getClass().getSimpleName()).warning("start=" + start + " end=" + end + " estim = " + estimatedTime + " sec = " + sec + " speed = " + speed + " kb/s");

					MainFrame.getInstance().getDownloadFrame().setSpeed(speed);// estimatedTime);
					start = System.currentTimeMillis();// System.nanoTime();
					tick = 0;
					tickSize = 0;
				}
				tick++;
				tickSize += increment;
			} catch (Exception e) {
				// e.printStackTrace();
			}
			if (increment > 0)
				delay = 0;
		}

		public void setDelay(long increment) {
			delay += (int) (increment / 1000);
		}

		public boolean isAborted() {
			return abort;
		}

		public void abort() {
			/*
			 * SINCE 1.3 synchronized (this) { if (repeat != null)
			 * repeat.cancel(); repeat = null; }
			 */
			abort = true;
		}
	}
}
