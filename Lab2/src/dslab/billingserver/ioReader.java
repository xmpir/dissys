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

    public void start(){
	try {
	    BufferedReader stdIn = new BufferedReader(
	    new InputStreamReader(System.in));
	    String fromUser;
	    while ((fromUser = stdIn.readLine()) != null) {
		if (fromUser.length() > 7 && fromUser.substring(0, 6).equals("!login")){
			fromUser = fromUser;
		}
		System.out.println(fromUser);
		if (fromUser.equals("!end")){
		    try {
			BillingServerMain.shutdown();
		    } catch (AccessException ex) {
			Logger.getLogger(ioReader.class.getName()).log(Level.SEVERE, null, ex);
		    }  catch (RemoteException ex) {
			Logger.getLogger(ioReader.class.getName()).log(Level.SEVERE, null, ex);
		    }  catch (NotBoundException ex) {
			Logger.getLogger(ioReader.class.getName()).log(Level.SEVERE, null, ex);
		    }
		}
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException ex) {
		    Logger.getLogger(ioReader.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	    stdIn.close();
	} catch (IOException ex) {
	    Logger.getLogger(ioReader.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
}
