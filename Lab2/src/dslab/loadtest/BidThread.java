/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.loadtest;

import java.util.Random;

/**
 *
 * @author Robert Rainer
 */
public class BidThread extends Thread{

    private TestClient client;
    private int bidsPerMin;
    
    public BidThread(TestClient client, int bidsPerMin){
	this.client=client;
	this.bidsPerMin=bidsPerMin;
    }
    
    @Override
    public void run(){
	while(Test.active){
	    try {
		
		client.bid();
		Thread.sleep(60000/bidsPerMin);
	    } catch (InterruptedException ex) {
		break;
	    }
	}
    }
    
}
