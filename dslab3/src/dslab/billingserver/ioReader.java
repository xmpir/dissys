/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.billingserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert Rainer
 */
public class ioReader extends Thread{

    @Override
    public void run(){
	
	    BufferedReader stdIn = new BufferedReader(
	    new InputStreamReader(System.in));
	    String fromUser;
	try {
	    while ((fromUser = stdIn.readLine()) != null) {
		
		//exit command shuts down the billing-server
		if (fromUser.equals("!exit")){
		    BillingServerMain.shutdown();
		    break;
		}
		//other commands are for testing reasons only
		else if (fromUser.equals("!auctions")){
		    for(Auction a:Data.getInstance().getAuctions()){
			System.out.println(a.getLineForBill());
		    }
		    System.out.println(Data.getInstance().getAuctions().size());
		}
		else if (fromUser.equals("!pricesteps")){
		    System.out.println(PriceSteps.getInstance().getRepresentation());
		}
		else if (fromUser.equals("!billuser")){
		    for(Auction a:Data.getInstance().getAuctionsByUser("alice")){
			System.out.println(a.getLineForBill());
		    }
		} else{
		    System.out.println(fromUser);
		}
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException ex) {
		    System.out.println("IO Interrupted");
		    break;
		}
	    }
	} catch (IOException ex) {
	    System.out.println("Problems with reading from the IO-Stream");	
	}
	try{
	stdIn.close();
	System.out.println("IO Stream closed");
	} catch (IOException ex) {
	    System.out.println("Problems with closing the IO-Stream");
	}
    }
    
}
