/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater.gui.frames;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import lucel_updater.controllers.MainFrame;


public class ImagePanel extends JPanel{

    private BufferedImage image;

    public ImagePanel(String path) {
       try {                
            InputStream is = getClass().getResourceAsStream(path);
            image = ImageIO.read(is);//new File(getClass().getResourceAsStream(path)));
           
            this.setVisible(true);
            this.repaint();
       } catch (IOException ex) {
            // handle exception...
       }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,0, 0,getWidth(),getHeight(), null); // see javadoc for more info on the parameters            
    }

}