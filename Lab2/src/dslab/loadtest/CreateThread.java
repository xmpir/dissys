/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.loadtest;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert Rainer
 */
public class CreateThread extends Thread{

    private TestClient client;
    private int auctionsPerMin;
    private int auctionDuration;
    
    public CreateThread(TestClient client){
	this.client=client;
    }

    public CreateThread(TestClient client, int auctionsPerMin, int auctionDuration) {
	this.client = client;
	this.auctionsPerMin = auctionsPerMin;
	this.auctionDuration = auctionDuration;
    }

    @Override
    public void run(){
	while(true){
	client.create(auctionDuration);
	    try {
		Thread.sleep(-TestClient.zufall.nextInt(15)+60000/auctionsPerMin);
	    } catch (InterruptedException ex) {
		break;
	    }
	}
    }
}
