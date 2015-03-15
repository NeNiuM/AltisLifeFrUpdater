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
import cz.dhl.ftp.Ftp;
import cz.dhl.ftp.FtpFile;
import cz.dhl.io.CoFile;

/**
 *
 * @author ZQTZ1943
 */
public class RemoteFileCheckerWorker extends LucelWorker {

	private ArrayList<CoFile> remoteFiles;
	private Ftp ftpClient;
	private String altisFolder;
	private HashMap<String, CoFile> filesToDownload;
	private ArrayList<String> foldersToCreate;
	private File altisRootObject;
	private Hashtable<String, File> localFiles;

	public RemoteFileCheckerWorker(Ftp ftp, String altisFold) {
		this.ftpClient = ftp;
		this.altisFolder = altisFold;

		// build the altis life root folder as a File object
		this.altisRootObject = new File(this.altisFolder);
	}

	public RemoteFileCheckerWorker(Ftp ftpClient, String armaRoot, Hashtable<String, File> altisFiles) {
		this(ftpClient, armaRoot);
		this.localFiles = altisFiles;
	}

	public void process() {
		try {
			this.remoteFiles = new ArrayList<CoFile>();
			this.setFilesToDownload(new HashMap<String, CoFile>());
			this.setFoldersToCreate(new ArrayList<String>());

			listRemoteFiles(new FtpFile("/",ftpClient));
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
	 * @param root
	 * @return
	 * @throws IOException
	 */
	private List<CoFile> listRemoteFiles(FtpFile root) throws IOException {
		List<CoFile> resultList = new ArrayList<CoFile>();
		
		Logger.getLogger(this.getClass().getSimpleName()).warning("listRemoteFiles");
		
		// get all the files from a directory
		CoFile[] fList = root.listCoFiles();//listFiles();
		resultList.addAll(Arrays.asList(fList));
		for (CoFile file : fList) {
			Logger.getLogger(this.getClass().getSimpleName()).warning("AA="+file.getAbsolutePath());
			if (file.isFile()) {
				String S = new SimpleDateFormat("dd/MM/yyyy hh:ss").format(file.lastModified());//getTimestamp().getTime());
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
					this.getFilesToDownload().put(root.getAbsolutePath() + file.getName(), file);//getSize());
				}

				// do not take "." and ".." folders for parsing to avoid
				// infinite loops
			} else if (file.isDirectory() && file.getName().equals(Lucel_Updater.config.getProperty("altisfr.foldername")) &&
					!".".equals(file.getName()) && !"..".equals(file.getName())) {
				System.out.println("DIR="+file.getName());
				
				String S = new SimpleDateFormat("dd/MM/yyyy hh:ss").format(file.lastModified());//getTimestamp().getTime());
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
					this.getFoldersToCreate().add(root.getAbsolutePath() + file.getName());
				}

				MainFrame.getInstance().repaint();
				// MainFrame.getInstance().pack();
				// parse the children nodes
				listNodeFiles(file, file.getAbsolutePath());

				this.setProgress(100);
			}
		}
		return resultList;
	}

	private List<CoFile> listNodeFiles(CoFile file2, String parent) throws IOException {
		List<CoFile> resultList = new ArrayList<CoFile>();
		// get all the files from a directory
		
		Logger.getLogger(this.getClass().getSimpleName()).warning("Diging into " + file2.getAbsolutePath()+" /// " + parent);
		
		ftpClient.cd(parent);//ftpFile.changeWorkingDirectory(parent);
		Logger.getLogger(this.getClass().getSimpleName()).warning(ftpClient.pwd());
		
		CoFile p = new FtpFile(parent, ftpClient);

		CoFile[] files = file2.listCoFiles();//listFiles();
		resultList.addAll(Arrays.asList(files));

		for (CoFile file : files) {
			if (file.isFile()) {
				String S = new SimpleDateFormat("dd/MM/yyyy hh:ss").format(file.lastModified());//getTimestamp().getTime());
				// MainFrame.getInstance().addDebugZoneRow("Norm file: "+parent+"/"+file.getName());//+
				// " - "+S + file.getRawListing());
				this.getRemoteFiles().add(file);

				String fileObjectName = this.altisRootObject.getAbsolutePath() + file2.getAbsolutePath() + "/" + file.getName();
				fileObjectName = fileObjectName.replace("\\", "/");
				// MainFrame.getInstance().addDebugZoneRow("The local filename should be "
				// + fileObjectName);
				File fileObject = new File(fileObjectName);
				if (fileObject.exists()) {
					File currentLocal = this.localFiles.get(file.getName());

					// compare the local and remote file dates to know if we
					// must re-download the file
					Long l = currentLocal.lastModified() / 10000;
					Long lo = file.lastModified() / 10000;
					
					Logger.getLogger(this.getClass().getSimpleName()).warning("testing " +fileObjectName);
							
					Long remoteSize = file.length();
					Long localSize = currentLocal.length();
					MainFrame.getInstance().addDebugZoneRow("LocalSize:"+localSize.longValue()+" remoteSize:"+remoteSize.longValue());
					Logger.getLogger(this.getClass().getSimpleName()).warning("localdate = " + l + " remotedate=" + lo);
					if (l < lo) {// local file is not up to date !
						MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : NOK (file is not up-to-date ! We'll add it to the downloads pool.");
						this.getFilesToDownload().put(file2.getAbsolutePath() + "/" + file.getName(), file);
					} else if(l == lo) {
						// local file up to date
						MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : File date is the same as remote, check the size now:");
						Logger.getLogger(this.getClass().getSimpleName()).warning(fileObjectName+" remoteSize"+remoteSize+" remoteSizeLV="+remoteSize.longValue()+" locSize="+localSize+" locSizeLV="+localSize.longValue());
						if(remoteSize.longValue() != localSize.longValue()){
							MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : File date is the same as remote, but the size is different, we'll add it to the downloads pool.");
							this.getFilesToDownload().put(file2.getAbsolutePath() + "/" + file.getName(), file);
						}
						else
						{
							MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : OK !");
						}
					}
//					else
//					{
//						//localTime > remoteTime ?
//						//just in case, download
//						MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : This local file date seems strange. Just in case, download the file.");
//						this.getFilesToDownload().put(file2.getAbsolutePath() + "/" + file.getName(), file);
//					}
				} else {

					MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : NOK (file doesn't exist :( ! We'll add it to the downloads pool.");
					this.getFilesToDownload().put(file2.getAbsolutePath() + "/" + file.getName(), file);
				}

			} else if (file.isDirectory() && !".".equals(file.getName()) && !"..".equals(file.getName())) {
				String S = new SimpleDateFormat("dd/MM/yyyy hh:ss").format(file.lastModified());
				this.getRemoteFiles().add(file);

				// check if the local file exists at the same path
				String fileObjectName = this.altisRootObject.getAbsolutePath() + file2.getAbsolutePath() + "/" + file.getName();
				fileObjectName = fileObjectName.replace("\\", "/");
				File fileObject = new File(fileObjectName);
				if (fileObject.exists()) {
					MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : OK !");
				} else {
				
					MainFrame.getInstance().addDebugZoneRow("Testing " + fileObjectName + " : NOK :( ! We'll add it to the creation pool.");
					this.getFoldersToCreate().add(file2.getAbsolutePath() + "/" + file.getName());
				}

				// continue the children node parsing
				listNodeFiles(file, file.getName());
			}
		}
		ftpClient.cd("../");//.changeWorkingDirectory("../");
		return resultList;
	}

	/**
	 * @return the remoteFiles
	 */
	public ArrayList<CoFile> getRemoteFiles() {
		return remoteFiles;
	}

	/**
	 * @param remoteFiles
	 *            the remoteFiles to set
	 */
	public void setRemoteFiles(ArrayList<CoFile> remoteFiles) {
		this.remoteFiles = remoteFiles;
	}

	/**
	 * @return the ftpClient
	 */
	public Ftp getFtpClient() {
		return ftpClient;
	}

	/**
	 * @param ftpClient
	 *            the ftpClient to set
	 */
	public void setFtpClient(Ftp ftpClient) {
		this.ftpClient = ftpClient;
	}

	/**
	 * @return the filesToDownload
	 */
	public HashMap<String, CoFile> getFilesToDownload() {
		return filesToDownload;
	}

	/**
	 * @param filesToDownload
	 *            the filesToDownload to set
	 */
	public void setFilesToDownload(HashMap<String, CoFile> filesToDownload) {
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
