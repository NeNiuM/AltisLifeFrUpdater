/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater.gui.actions;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import lucel_updater.controllers.MainFrame;
import lucel_updater.gui.frames.ExitDialog;
/**
 *
 * @author NeNiuM
 */
public class ExitAction implements javax.swing.Action{

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
        ExitDialog d = new ExitDialog(MainFrame.getInstance(), true);
        d.setAlwaysOnTop(true);
        MainFrame instance = MainFrame.getInstance();
        d.setLocation(instance.getX()+(instance.getWidth()/2)-d.getWidth()/2, instance.getY() + (instance.getHeight()/2)-d.getHeight()/2);
        d.setVisible(true);
    }
    
}
