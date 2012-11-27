package dslab.auctionserver;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Updater implements Runnable{
	Lists lists = Lists.getInstance();


	public Updater(Lists lists){
		this.lists = lists;
	}

	public void run(){
	    while(true){
		lists.updateAuctions();
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException ex) {
		    break;
		}
		
	    }
	}
	
	
	
}
