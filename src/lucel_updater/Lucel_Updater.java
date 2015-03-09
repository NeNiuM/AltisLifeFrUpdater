/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import com.alee.laf.WebLookAndFeel;
import lucel_updater.controllers.*;
import lucel_updater.gui.frames.HelpDialog;
import lucel_updater.utils.PropertiesUtil;

/**
 *
 * @author NeNiuM
 */
public class Lucel_Updater {

    public static Properties config;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            
            SwingUtilities.invokeLater(new Runnable(){
                public void run(){
                    try {
                        File f = new File("config.properties");
                        if(f.exists())
                            Lucel_Updater.config = PropertiesUtil.load( new Properties(), "config.properties" );
                        else
                        {
                            Properties p = new Properties();
                            p.put("altisfr.ftp.host","localhost");
                            p.put("altisfr.ftp.username","kaf");
                            p.put("altisfr.ftp.pass","altis");
                            p.put("altisfr.ftp.port","21");
                            p.put("altisfr.foldername","@Altisfr");
                            PropertiesUtil.store(p, "config.properties");
                            
                            Lucel_Updater.config = PropertiesUtil.load(p, "config.properties");
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Lucel_Updater.class.getName()).log(Level.SEVERE, null, ex);
                    }
                               
                    try {
//                        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                            /*if ("Nimbus".equals(info.getName())) {
//                                UIManager.setLookAndFeel(info.getClassName());
//                                break;
//                            }
//                            else{*/
//                                UIManager.setLookAndFeel  ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//                            //}
//                        }
                    	 WebLookAndFeel.install ();
                    } catch (Exception e) {e.printStackTrace();}
                    
                    
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            MainFrame f = MainFrame.getInstance();
                            f.setVisible(true);
                            f.setLocationRelativeTo(null);
                            HelpDialog hd = new HelpDialog(f, true);
                            hd.setVisible(true);
                        }
                    }); 
                }
            });
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * @return the config
     */
    public Properties getConfig() {
        return config;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(Properties config) {
        this.config = config;
    }
    
    public static String getConfigValue(String key)
    {
        return PropertiesUtil.getByKey( config, key );
    }
}
