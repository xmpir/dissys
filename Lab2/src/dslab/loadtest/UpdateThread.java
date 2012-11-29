/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.loadtest;

/**
 *
 * @author Robert Rainer
 */
public class UpdateThread extends Thread {

    private TestClient client;
    private int updateIntervalSec;
    
    public UpdateThread(TestClient client, int updateIntervalSec){
	this.client=client;
	this.updateIntervalSec = updateIntervalSec;
    }
    
    @Override
    public void run(){
	while(Test.active){
	client.update();
	    try {
		Thread.sleep(updateIntervalSec*1000);
	    } catch (InterruptedException ex) {
		break;
	    }
	
	}
    }
}
