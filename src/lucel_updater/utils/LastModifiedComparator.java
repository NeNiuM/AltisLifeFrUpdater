/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater.utils;

import java.util.Comparator;
import org.apache.commons.net.ftp.FTPFile;

/**
 *
 * @author NeNiuM
 */
public class LastModifiedComparator implements Comparator<FTPFile> {

    public int compare(FTPFile f1, FTPFile f2) {
        return f1.getTimestamp().compareTo(f2.getTimestamp());
    }
}