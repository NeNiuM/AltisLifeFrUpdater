/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater.models;

import java.io.IOException;
import java.net.URL;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.net.ftp.FTPClient;

import lucel_updater.Lucel_Updater;
import lucel_updater.controllers.MainFrame;

/**
 *
 * @author ZQTZ1943
 */
public class FTPClientManager {

	private FTPClient ftpClient;

	public FTPClientManager() {

		Logger logger = Logger.getLogger("MyLog");
		FileHandler fh;

		try {
			// This block configure the logger with handler and formatter
			/*fh = new FileHandler("C:/lucel.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);*/

			URL loc = FTPClient.class.getProtectionDomain().getCodeSource()
					.getLocation();
			logger.info("Class: " + FTPClient.class.getSimpleName()
					+ " loaded from " + loc);

			this.ftpClient = new FTPClient();
			logger.info("OK FTP !");

		} catch (Exception e) {
			e.printStackTrace();
			Logger.getLogger(this.getClass().getName()).warning(
					"e=" + e.getMessage());
			System.out.println(e.getMessage());
			logger.info(e.getMessage());
		}

	}

	// Custom connect method, which will avoid specifying parameters : it'll
	// automatically read the configuration file
	public boolean connect() {
		try {
			// pass directory path on server to connect
			this.getFtpClient().connect(
					Lucel_Updater.getConfigValue("altisfr.ftp.host"));
			this.getFtpClient().setDefaultPort(
					Integer.valueOf(Lucel_Updater
							.getConfigValue("altisfr.ftp.port")));
			this.getFtpClient().enterRemotePassiveMode();
			this.getFtpClient().setConnectTimeout(600000);
			this.getFtpClient().setDefaultTimeout(600000);
			this.getFtpClient().setSoTimeout(600000);
			this.getFtpClient().setControlKeepAliveTimeout(300);
		} catch (Exception e) {
			MainFrame
					.getInstance()
					.addDebugZoneRow(
							"Exception occured: error during the ftp remote connection (is the configuration file readable?). "
									+ e.getMessage());
		}
		return true;
	}

	// This method will authenticate the user over the remote ftp server,
	// using the credentials provided into the configuration file
	public boolean authenticate() {
		try {
			return this.getFtpClient().login(
					Lucel_Updater.getConfigValue("altisfr.ftp.username"),
					Lucel_Updater.getConfigValue("altisfr.ftp.pass"));
		} catch (Exception e) {
			MainFrame
					.getInstance()
					.addDebugZoneRow(
							"Exception occured: error during the ftp remote authentication (is the configuration file readable?). "
									+ e.getMessage());
			return false;
		}
	}

	public void disconnect() {
		try {
			this.getFtpClient().disconnect();
		} catch (IOException ex) {
			Logger.getLogger(FTPClientManager.class.getName()).log(
					Level.SEVERE, null, ex);
		}
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

}
