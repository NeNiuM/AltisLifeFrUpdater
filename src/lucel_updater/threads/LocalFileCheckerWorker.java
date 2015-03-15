/*
 * @author: Galabru-sama
 * @date: 13/01/2015
 *
 * This implementation aims at listing every remote file found on the FTP server
 * and producing a strong list to compare with remote files.
 */
package lucel_updater.threads;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author ZQTZ1943
 */
public class LocalFileCheckerWorker extends LucelWorker{

    private Hashtable<String, File> files;
    private String folder;
    private ArrayList<FTPFile> toDownload;
    private String currentBrowserPath;
    
    public LocalFileCheckerWorker(Hashtable<String, File> f, String fold){
        this.files = f;
        this.folder = fold;
        this.toDownload = new ArrayList<FTPFile>();
        this.currentBrowserPath = "/";
        
        Logger.getLogger(this.getClass().getSimpleName()).info("LocalFileCheckerWorker controller");
    }
    
    @Override
    protected Integer doInBackground() {
        Logger.getLogger(this.getClass().getName()).info("doInBackground");
        this.listAltisFiles(this.getFolder());
        return this.getFiles().size();
    }

    @Override
    protected void done() {
    }
    
    
    private Hashtable<String, File> listAltisFiles(String directoryName) {
        Logger.getLogger(this.getClass().getSimpleName()).info("Listing files for " + directoryName);
        File directory = new File(directoryName);
        Hashtable<String, File> resultList = new Hashtable<String, File>();

        // get all the files from a directory
        File[] fList = directory.listFiles();
       // resultList.addAll(Arrays.asList(fList));
        for (File file : fList) {
            if (file.isFile()) {
                this.files.put(file.getName(), file);
                
                //file here is a standard local file
                //check server side if the remote file exists
                Logger.getLogger(this.getClass().getSimpleName()).warning("Local file is "+file.getName());
                
            } else if (file.isDirectory()) {
                this.currentBrowserPath += file.getName();
                Logger.getLogger(this.getClass().getSimpleName()).info("File is a directory. Current buffer = " + this.currentBrowserPath);
                this.files.put(file.getName(), file);
                resultList.putAll(listAltisFiles(file.getAbsolutePath()));
            }
        }
        return resultList;
    } 

    /**
     * @return the files
     */
    public Hashtable<String, File> getFiles() {
        return files;
    }

    /**
     * @param files the files to set
     */
    public void setFiles(Hashtable<String, File> files) {
        this.files = files;
    }

    /**
     * @return the folder
     */
    public String getFolder() {
        return folder;
    }

    /**
     * @param folder the folder to set
     */
    public void setFolder(String folder) {
        this.folder = folder;
    }
    
}
