/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater.gui.misc;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import lucel_updater.controllers.MainFrame;

/**
 *
 * @author NeNiuM
 */
public class TeuilCursor extends java.awt.Cursor{
    
    private Cursor t;
    
    
    public TeuilCursor(int type) {
        super(type);
        
        try{
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Image image = toolkit.getImage(getClass().getResource("/lucel_updater/gui/icons/tetedeteuil.gif"));  
            ImageIcon i = new ImageIcon(getClass().getResource("/lucel_updater/gui/icons/tetedeteuil.gif"));
            Point point = new Point(0, 0);
            t = toolkit.createCustomCursor(image , point, "img");
        }
        catch(Exception e){
            e.printStackTrace();
            
        }
    }

    /**
     * @return the t
     */
    public Cursor getT() {
        return t;
    }

    /**
     * @param t the t to set
     */
    public void setT(TeuilCursor t) {
        this.t = t;
    }
    
    
}
