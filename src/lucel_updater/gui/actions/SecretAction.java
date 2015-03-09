/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater.gui.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import lucel_updater.controllers.MainFrame;
import lucel_updater.gui.frames.SecretDialog;


/**
 *
 * @author NeNiuM
 */
public class SecretAction implements javax.swing.Action{

    private SecretDialog s;
    
    @Override
    public Object getValue(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void putValue(String key, Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setEnabled(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            SecretDialog s = new SecretDialog(MainFrame.getInstance(),true);
            s.setAlwaysOnTop(true);
            MainFrame instance = MainFrame.getInstance();
            s.setLocation(instance.getX()+(instance.getWidth()/2)-s.getWidth()/2, instance.getY() + (instance.getHeight()/2)-s.getHeight()/2);
            s.setVisible(true);
        }
        catch(Exception ee)
        {
            ee.printStackTrace();
            System.exit(0);
        }
    }
    
}
