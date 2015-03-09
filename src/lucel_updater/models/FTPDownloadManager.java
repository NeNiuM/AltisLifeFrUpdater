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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import lucel_updater.Lucel_Updater;

/**
 *
 * @author Shiva
 */
public class FTPDownloadManager {

	ArrayList<CoFile> inPool;
	Ftp connection;
	String armaR;
	CoFile to;
	CoFile from;

	public FTPDownloadManager(String armaRoot) {
		initDownloadManager(armaRoot);
	}

	/**
	 * Start the download manager by instanciating a new FTP connection and
	 * instanciating the pool
	 */
	public void initDownloadManager(String armaRoot) {
		try {
			this.inPool = new ArrayList<CoFile>();
			this.armaR = armaRoot;

			this.connection = new Ftp();
			this.connection.connect(Lucel_Updater
					.getConfigValue("altisfr.ftp.host"), Integer
					.valueOf(Lucel_Updater.getConfigValue("altisfr.ftp.port")));
			this.connection.login(
					Lucel_Updater.getConfigValue("altisfr.ftp.username"),
					Lucel_Updater.getConfigValue("altisfr.ftp.pass"));

		} catch (IOException ex) {
			Logger.getLogger(FTPDownloadManager.class.getName()).log(
					Level.SEVERE, null, ex);
		}
	}

	public boolean addDownloadToPool(String path, String target) {

		Logger log = Logger.getLogger("MyLog");
		FileHandler fh;
		try {
			// fh = new FileHandler("C:/lucel.log");
			// log.addHandler(fh);
			// SimpleFormatter formatter = new SimpleFormatter();
			// fh.setFormatter(formatter);

			URL loc = CoFile.class.getProtectionDomain().getCodeSource()
					.getLocation();
			log.info("Class: " + CoFile.class.getSimpleName() + " loaded from "
					+ loc);

			FtpInputStream is = null;

			CoFile from = new FtpFile(path, this.connection);
			CoFile to = new LocalFile(this.armaR, path);
			this.setFile(to, from);
			return CoLoad.copy(to, from);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

		/* FtpInputStream is = null; */
		/* source FtpFile remote file */
		/* FtpFile file = new FtpFile(path,this.connection); */

		/* open ftp input stream */
		/*
		 * is = new FtpInputStream(file); BufferedReader br = new
		 * BufferedReader(new InputStreamReader(is));
		 */

		// Object line = "";
		/* read ftp input stream */
		/*
		 * while ((line = br.readLine()) != null) System.out.println(line);
		 */

		/* close reader */
		/*
		 * br.close(); return true; } catch (IOException ex) {
		 * Logger.getLogger(FTPDownloadManager
		 * .class.getName()).log(Level.SEVERE, null, ex); return false; }
		 */

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
		total = this.from.length();
		// MainFrame.getInstance().addDebugZoneRow("LENGTH = "+ total);
		delay = 0;
		/*
		 * SINCE 1.3 synchronized (this) { if (repeat != null) repeat.cancel();
		 * repeat=new Timer(); repeat.schedule(new TimerTask() { public void
		 * run() { System.out.println("Timer tick"); } } ,500,500); }
		 */
		(new Thread(copy)).start();
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

	Runnable status = new Runnable() {
		public void run() {
			if (progress != null) {
				progress = "" + done + " of " + total
						+ (delay > 0 ? " " + delay + "sec no answer" : "");
			}
		}
	};

	/** Thread safe. */
	public void setProgress(int increment) {
		done += increment;
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
		 * SINCE 1.3 synchronized (this) { if (repeat != null) repeat.cancel();
		 * repeat = null; }
		 */
		abort = true;
	}
}
