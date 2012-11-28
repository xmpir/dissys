/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.loadtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	    while (
		    
		    (fromUser = stdIn.readLine()
		    
		    ) != null) {
		
		//exit command shuts down the billing-server
		if (fromUser.equals("!exit")){
		    
		}
		try {Thread.sleep(1000);} 
		catch (InterruptedException ex) {
		    System.out.println("IO Interrupted");
		    break;
		}
	    }
	} catch (IOException ex) {
	    System.out.println("Problems with reading from the IO-Stream");	}
	try{
	stdIn.close();
	System.out.println("IO Stream closed");
	} catch (IOException ex) {
	    System.out.println("Problems with closing the IO-Stream");
	}
    }
    
}
