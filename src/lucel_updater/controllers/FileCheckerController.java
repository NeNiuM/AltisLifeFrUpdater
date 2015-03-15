/**
 * @author NeNiuM
 * This class aims at control and synchronize local and remote file search to establish files listings.
 * The 2 lists will then be compared to make a "must download" file list (missing or not up-to-date files)
 */

package lucel_updater.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JProgressBar;

import org.apache.commons.net.ftp.FTP;

import cz.dhl.io.CoFile;
import lucel_updater.Lucel_Updater;
import lucel_updater.models.FTPClientManager;
import lucel_updater.models.FTPDownloadManager;
import lucel_updater.threads.LocalFileCheckerWorker;
import lucel_updater.threads.RemoteFileCheckerWorker;

public class FileCheckerController {

	private String armaRoot;
	private Hashtable<String, File> altisFiles;
	private HashMap<String, CoFile> toDownload;
	private ArrayList<String> toCreate;

	private FTPClientManager ftpManager;

	public FileCheckerController(String altisRoot) {
		this.armaRoot = altisRoot;
	}

	public void checkAltisFolder() {
		try {
			if (checkFolderExists(Paths.get(this.armaRoot))) {
				MainFrame.getInstance().addDebugZoneRow("Checking Arma3 folder: OK");
			} else {
				MainFrame.getInstance().addDebugZoneRow("Checking Arma3 folder: failed! (Does the directory really exist?)");
			}

			// check for the @AltisFr folder
			String altisFolder = this.armaRoot + "\\" + Lucel_Updater.getConfigValue("altisfr.foldername");
			// MainFrame.getInstance().addDebugZoneRow("r="+altisFolder);
			if (checkFolderExists(Paths.get(altisFolder))) {
				MainFrame.getInstance().addDebugZoneRow("Checking "+Lucel_Updater.getConfigValue("altisfr.foldername")+" folder: OK");
			} else {
				MainFrame.getInstance().addDebugZoneRow("Checking "+Lucel_Updater.getConfigValue("altisfr.foldername")+" folder: not found, let us create it for you.");
				File dir = new File(altisFolder);
				dir.mkdir();
			}

			// listing all the altis files with a local thread worker
			this.altisFiles = new Hashtable<String, File>();
			LocalFileCheckerWorker localChecker = new LocalFileCheckerWorker(this.altisFiles, altisFolder);
			localChecker.execute(); // start the background process
			localChecker.get(); // the current thread will wait for the thread
								// to finish

			if (localChecker.isWorkerDone()) {// ensure the thread has stopped
				this.altisFiles = localChecker.getFiles();
				
				if (this.altisFiles.isEmpty()) {
					MainFrame.getInstance().addDebugZoneRow("No files found in "+Lucel_Updater.getConfigValue("altisfr.foldername")+"! Going to download all the remote content, prepare to take down your connection!");
					listFtp();
				} else {
					// browse the remote ftp server to establish a file listing
					listFtp();
				}
			}
		} catch (InterruptedException ex) {
			Logger.getLogger(FileCheckerController.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} catch (ExecutionException ex) {
			Logger.getLogger(FileCheckerController.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		}
	}

	public void listFtp() {
		this.ftpManager = new FTPClientManager();
		MainFrame.getInstance().getjProgressBar1().setValue(0);

		if (this.ftpManager.connect())
			MainFrame.getInstance().addDebugZoneRow("Connection ok...");
		else
			MainFrame.getInstance().addDebugZoneRow("Connection failed...");
		
		// login on the ftp server
		boolean login = this.ftpManager.authenticate();
		if (login) {
			// try {
			// connection & authentication OK
			MainFrame.getInstance().addDebugZoneRow("Connection established...");
			MainFrame.getInstance().addDebugZoneRow("\r\nRemote files: ");
			
			//set the ftp connection to binary mode
			/*try {
				this.ftpManager.getFtpClient().setFileType(FTP.BINARY_FILE_TYPE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

			final RemoteFileCheckerWorker remoteLister = new RemoteFileCheckerWorker(ftpManager.getFtpClient(), this.armaRoot, this.altisFiles);
			final FTPDownloadManager downloadManager = new FTPDownloadManager(this.armaRoot);

			try {
				remoteLister.execute();
				// start the background process
				remoteLister.get(); // the current thread will wait for the
									// thread to finish
				if (remoteLister.isWorkerDone()) {// ensure the thread has
													// stopped
					toDownload = remoteLister.getFilesToDownload();
					toCreate = remoteLister.getFoldersToCreate();

					double step = 100 / toDownload.size();

					MainFrame.getInstance().addDebugZoneRow("\r\n");
					MainFrame.getInstance().addDebugZoneRow("#### Folders to create ####");
					if (toCreate.isEmpty())
						MainFrame.getInstance().addDebugZoneRow("None\r\n");
					for (String fi : toCreate) {
						File ff = new File(armaRoot + "/" + fi);
						boolean success = ff.mkdirs();
						if (!success) {
							// Directory creation failed
							MainFrame.getInstance().addDebugZoneRow("The folder " + ff.getAbsolutePath() + " cannot be created.");
						} else
							MainFrame.getInstance().addDebugZoneRow("The folder " + fi + " has been created !");
					}

					MainFrame.getInstance().addDebugZoneRow("\r\n");
					MainFrame.getInstance().addDebugZoneRow("#### Files to download ####");
					if (toDownload.isEmpty())
						MainFrame.getInstance().addDebugZoneRow("None\r\n");

					for (String key : toDownload.keySet()) {
						String filename = key;
						CoFile toDownloadFile = toDownload.get(key);
						
						long size = toDownloadFile.length();
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
						MainFrame.getInstance().addDebugZoneRow("Downloading " + filename + " (" + String.format("%.2f", displaySize) + " " + unity + ") ...");
						MainFrame.getInstance().getDownloadFrame().setVisible(true);
						
						if(downloadManager.addDownloadToPool(toDownloadFile, size)){
							MainFrame.getInstance().addDebugZoneRow("Download OK.");
							JProgressBar bar = MainFrame.getInstance().getjProgressBar1();
							double value = bar.getValue() + step;
							if (value > bar.getMaximum()) {
								value = bar.getMaximum();
							}
							bar.setValue((int) value);
						}
//						} else {
//							MainFrame.getInstance().addDebugZoneRow("Download failed... Retrying.");
//							if (downloadManager.addDownloadToPool(toDownloadFile, size)) {
//								MainFrame.getInstance().addDebugZoneRow("Download OK.");
//								JProgressBar bar = MainFrame.getInstance().getjProgressBar1();
//								double value = bar.getValue() + step;
//								if (value > bar.getMaximum()) {
//									value = bar.getMaximum();
//								}
//								bar.setValue((int) value);
//							} else
//								MainFrame.getInstance().addDebugZoneRow("Download failed...");
//						}
					}

				}
				MainFrame.getInstance().getjProgressBar1().setValue(100);
			} catch (InterruptedException ex) {
				Logger.getLogger(FileCheckerController.class.getName()).log(Level.SEVERE, null, ex);
				ex.printStackTrace();
			} catch (ExecutionException ex) {
				Logger.getLogger(FileCheckerController.class.getName()).log(Level.SEVERE, null, ex);
				ex.printStackTrace();
			}
		} else {
			MainFrame.getInstance().addDebugZoneRow("Authentication failed...");
		}
	}


	private boolean checkFolderExists(Path folderPath) {
		return Files.exists(folderPath);
	}
}
