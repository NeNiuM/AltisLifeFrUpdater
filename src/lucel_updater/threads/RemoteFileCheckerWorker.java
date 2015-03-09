/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater.threads;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lucel_updater.Lucel_Updater;
import lucel_updater.controllers.MainFrame;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author ZQTZ1943
 */
public class RemoteFileCheckerWorker extends LucelWorker {

	private ArrayList<FTPFile> remoteFiles;
	private FTPClient ftpClient;
	private String altisFolder;
	private HashMap<String, Integer> filesToDownload;
	private ArrayList<String> foldersToCreate;
	private File altisRootObject;
	private Hashtable<String, File> localFiles;

	public RemoteFileCheckerWorker(FTPClient ftp, String altisFold) {
		this.ftpClient = ftp;
		this.altisFolder = altisFold;

		// build the altis life root folder as a File object
		this.altisRootObject = new File(this.altisFolder);
	}

	public RemoteFileCheckerWorker(FTPClient ftpClient, String armaRoot, Hashtable<String, File> altisFiles) {
		this(ftpClient, armaRoot);
		this.localFiles = altisFiles;
	}

	public void process() {
		try {
			this.remoteFiles = new ArrayList<FTPFile>();
			this.setFilesToDownload(new HashMap<String, Integer>());
			this.setFoldersToCreate(new ArrayList<String>());

			listRemoteFiles(ftpClient);
		} catch (IOException ex) {
			Logger.getLogger(RemoteFileCheckerWorker.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	protected Integer doInBackground() {
		process();
		return 1;
	}

	@Override
	protected void done() {
		Logger.getLogger(this.getClass().getSimpleName()).info("done !");
	}

	/**
	 * List the main node of a ftp tree
	 * 
	 * @param ftpClient
	 * @return
	 * @throws IOException
	 */
	private List<FTPFile> listRemoteFiles(FTPClient ftpClient) throws IOException {
		List<FTPFile> resultList = new ArrayList<FTPFile>();

		// get all the files from a directory
		FTPFile[] fList = ftpClient.listFiles();
		resultList.addAll(Arrays.asList(fList));
		for (FTPFile file : fList) {
			System.out.println("TARGET="+Lucel_Updater.config.getProperty("altisfr.foldername"));
			
			if (file.isFile()) {
				String S = new SimpleDateFormat("dd/MM/yyyy hh:ss").format(file.getTimestamp().getTime());
				MainFrame.getInstance().addDebugZoneRow("Norm file: " + "/" + file.getName());// +
																								// " - "+S);
				this.getRemoteFiles().add(file);

				// check if the local file exists at the same path
				String fileObjectName = this.altisRootObject.getAbsolutePath() + "/" + file.getName();
				fileObjectName = fileObjectName.replace("\\", "/");
				File fileObject = new File(fileObjectName);
				if (fileObject.exists()) {
					MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : OK !");
				} else {
					File currentLocal = this.localFiles.get(file.getName());// fileObjectName);

					MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : NOK :( ! We'll add it to the downloads pool.");
					this.getFilesToDownload().put(ftpClient.printWorkingDirectory() + "/" + file.getName(), (int) file.getSize());
				}

				// do not take "." and ".." folders for parsing to avoid
				// infinite loops
			} else if (file.isDirectory() && file.getName().equals(Lucel_Updater.config.getProperty("altisfr.foldername")) &&
					!".".equals(file.getName()) && !"..".equals(file.getName())) {
				System.out.println("DIR="+file.getName());
				
				String S = new SimpleDateFormat("dd/MM/yyyy hh:ss").format(file.getTimestamp().getTime());
				MainFrame.getInstance().addDebugZoneRow("Directory: " + file.getName());// +
																						// " - "+S);

				// check if the local file exists at the same path
				String fileObjectName = this.altisRootObject.getAbsolutePath() + "/" + file.getName();
				fileObjectName = fileObjectName.replace("\\", "/");
				File fileObject = new File(fileObjectName);
				if (fileObject.exists()) {
					MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : OK !");
				} else {
					File currentLocal = this.localFiles.get(file.getName());// fileObjectName);

					MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : NOK :( ! We'll add it to the creation pool.");
					this.getFoldersToCreate().add(ftpClient.printWorkingDirectory() + "/" + file.getName());
				}

				MainFrame.getInstance().repaint();
				// MainFrame.getInstance().pack();
				// parse the children nodes
				listNodeFiles(ftpClient, file.getName());

				this.setProgress(100);
			}
		}
		return resultList;
	}

	private List<FTPFile> listNodeFiles(FTPClient ftpClient, String parent) throws IOException {
		List<FTPFile> resultList = new ArrayList<FTPFile>();
		// get all the files from a directory
		// MainFrame.getInstance().addDebugZoneRow("    -> digging into "+parent);
		ftpClient.changeWorkingDirectory(parent);

		FTPFile[] files = ftpClient.listFiles();
		resultList.addAll(Arrays.asList(files));

		for (FTPFile file : files) {
			if (file.isFile()) {
				String S = new SimpleDateFormat("dd/MM/yyyy hh:ss").format(file.getTimestamp().getTime());
				// MainFrame.getInstance().addDebugZoneRow("Norm file: "+parent+"/"+file.getName());//+
				// " - "+S + file.getRawListing());
				this.getRemoteFiles().add(file);

				String fileObjectName = this.altisRootObject.getAbsolutePath() + ftpClient.printWorkingDirectory() + "/" + file.getName();
				fileObjectName = fileObjectName.replace("\\", "/");
				// MainFrame.getInstance().addDebugZoneRow("The local filename should be "
				// + fileObjectName);
				File fileObject = new File(fileObjectName);
				if (fileObject.exists()) {
					File currentLocal = this.localFiles.get(file.getName());// fileObjectName);

					// compare the local and remote file dates to know if we
					// must re-download the file
					Long l = currentLocal.lastModified() / 1000;
					Long lo = file.getTimestamp().getTimeInMillis() / 1000;

					if (l < lo) {// local file is not up to date !
						MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : NOK (file is not up-to-date ! We'll add it to the downloads pool.");
						this.getFilesToDownload().put(ftpClient.printWorkingDirectory() + "/" + file.getName(), (int) file.getSize());
					} else {
						// local file up to date
						MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : OK !");
					}
				} else {

					MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : NOK (file doesn't exist :( ! We'll add it to the downloads pool.");
					this.getFilesToDownload().put(ftpClient.printWorkingDirectory() + "/" + file.getName(), (int) file.getSize());
				}

			} else if (file.isDirectory() && !".".equals(file.getName()) && !"..".equals(file.getName())) {
				String S = new SimpleDateFormat("dd/MM/yyyy hh:ss").format(file.getTimestamp().getTime());
				this.getRemoteFiles().add(file);

				// check if the local file exists at the same path
				String fileObjectName = this.altisRootObject.getAbsolutePath() + ftpClient.printWorkingDirectory() + "/" + file.getName();
				fileObjectName = fileObjectName.replace("\\", "/");
				// MainFrame.getInstance().addDebugZoneRow("2/ The local foldername should be "
				// + fileObjectName);
				File fileObject = new File(fileObjectName);
				if (fileObject.exists()) {
					MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : OK !");
				} else {
					/*
					 * File currentLocal =
					 * this.localFiles.get(file.getName());//fileObjectName);
					 * MainFrame
					 * .getInstance().addDebugZoneRow("CURRENT LOCAL IS " +
					 * currentLocal.getName());
					 * MainFrame.getInstance().addDebugZoneRow
					 * ("CURRENT REMOTE IS " + fileObjectName);
					 */

					MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : NOK :( ! We'll add it to the creation pool.");
					this.getFoldersToCreate().add(ftpClient.printWorkingDirectory() + "/" + file.getName());
				}

				// continue the children node parsing
				listNodeFiles(ftpClient, file.getName());
			}
		}
		ftpClient.changeWorkingDirectory("../");
		return resultList;
	}

	/**
	 * @return the remoteFiles
	 */
	public ArrayList<FTPFile> getRemoteFiles() {
		return remoteFiles;
	}

	/**
	 * @param remoteFiles
	 *            the remoteFiles to set
	 */
	public void setRemoteFiles(ArrayList<FTPFile> remoteFiles) {
		this.remoteFiles = remoteFiles;
	}

	/**
	 * @return the ftpClient
	 */
	public FTPClient getFtpClient() {
		return ftpClient;
	}

	/**
	 * @param ftpClient
	 *            the ftpClient to set
	 */
	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	/**
	 * @return the filesToDownload
	 */
	public HashMap<String, Integer> getFilesToDownload() {
		return filesToDownload;
	}

	/**
	 * @param filesToDownload
	 *            the filesToDownload to set
	 */
	public void setFilesToDownload(HashMap<String, Integer> filesToDownload) {
		this.filesToDownload = filesToDownload;
	}

	/**
	 * @return the foldersToCreate
	 */
	public ArrayList<String> getFoldersToCreate() {
		return foldersToCreate;
	}

	/**
	 * @param foldersToCreate
	 *            the foldersToCreate to set
	 */
	public void setFoldersToCreate(ArrayList<String> foldersToCreate) {
		this.foldersToCreate = foldersToCreate;
	}

}
