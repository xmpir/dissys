/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.loadtest;

import java.util.Date;

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
	Date now = new Date();
        client.log("client update");
	client.update();
	client.log("client update finished in "+((new Date()).getTime()-now.getTime()));
	
	    try {
		Thread.sleep(updateIntervalSec*1000);
	    } catch (InterruptedException ex) {
		break;
	    }
	
	}
    }
}
