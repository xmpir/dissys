/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dslab.bidclient;

import dslab.channels.Channel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert Rainer
 */
public class ClientWriter extends Thread {

    private final Channel channel;

    public ClientWriter(Channel channel) {
	this.channel = channel;
    }

    public void run() {
	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	String fromUser;

	try {
	    while ((fromUser = stdIn.readLine()) != null) {
		if (fromUser.length() > 7 && fromUser.substring(0, 6).equals("!login")) {
		    //something to add when logging in?
		}
		channel.send(fromUser);
		if (fromUser.equals("!end")) {
		    break;
		}
		
	    }
	} catch (IOException ex) {
	    Logger.getLogger(ClientWriter.class.getName()).log(Level.SEVERE, null, ex);
	}
	try {
	    stdIn.close();
	} catch (IOException ex) {
	    Logger.getLogger(ClientWriter.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    Client.shutdown();
	}
    }
}
