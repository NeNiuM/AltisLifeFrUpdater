/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lucel_updater.threads;

/**
 *
 * @author ZQTZ1943
 */
public abstract class LucelRunnable implements Runnable{
    Thread currentThread;
    
    public LucelRunnable(){
        
    }
    
    public abstract void run() ;

    /**
     * @return the currentThread
     */
    public Thread getCurrentThread() {
        return currentThread;
    }

    /**
     * @param currentThread the currentThread to set
     */
    public void setCurrentThread(Thread currentThread) {
        this.currentThread = currentThread;
    }
}
