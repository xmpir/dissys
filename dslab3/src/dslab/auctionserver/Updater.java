package dslab.auctionserver;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Updater implements Runnable{

	public Updater(){
	}

    @Override
	public void run(){
	    while(true){
		Data.getInstance().updateAuctions();
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException ex) {
		    break;
		}
		
	    }
	}
	
	
	
}
