/*
 * @author: Galabru-sama
 * @date: 13/01/2015
 *
 * This generic class aims at describing a full background worker.
 * By implementing both methods, we'll be able to specify the full worker behavior until it ends.
 */
package lucel_updater.threads;
import javax.swing.SwingWorker;

public abstract class LucelWorker extends SwingWorker<Integer, String>{

    // Thread operations (usually time-consuming). 
    @Override
    abstract protected Integer doInBackground();
    
    // This method can be executed when the process is done
    @Override
    abstract protected void done();
    
    public boolean isWorkerDone(){
        return this.isDone(); 
    }
    
    public boolean isWorkerCancelled(){
        return this.isCancelled();
    }
}
