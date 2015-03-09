/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater.threads;

import java.util.logging.Level;
import java.util.logging.Logger;
import lucel_updater.controllers.MainFrame;

public class TestThread extends LucelRunnable{
    
    public void start() {
        MainFrame.getInstance().addDebugZoneRow("Start !");
        if (currentThread == null) {
            currentThread = new Thread(this);
            currentThread.start();
        }
    }

    public void stop() {
        currentThread = null;
        MainFrame.getInstance().addDebugZoneRow("Stop !");
    }

    @Override
    public void run() {
        while (currentThread != null) {
            for (int i = 0; i < 50; i++) {
                MainFrame.getInstance().addDebugZoneRow("Test "+"\r\n");
//                MainFrame.getInstance().repaint();
            }
        }
        this.stop();
    }

    
}
