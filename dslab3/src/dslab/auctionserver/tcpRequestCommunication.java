package dslab.auctionserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.Date;

import dslab.analyticsserver.BidEvent;
import dslab.analyticsserver.EventNotFoundException;
import dslab.analyticsserver.UserEvent;
import dslab.channels.Channel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class tcpRequestCommunication extends Thread {

    private User currentUser;
    private Lists lists;
    private final Channel channel;

    tcpRequestCommunication(Lists lists, Channel channel, User currentUser) {
	this.lists = lists;
	this.currentUser = currentUser;
	this.channel = channel;

    }

    public void run() {
	    Protocol p = new Protocol(currentUser, lists);
	    String inputLine;
	    String outputWhole="";
	    while (true) {
		inputLine = channel.receive();
		if(inputLine!=null){
		try {
		    outputWhole = p.processInput(inputLine);
		} catch (UnknownParameterException ex) {
		    Logger.getLogger(tcpRequestCommunication.class.getName()).log(Level.SEVERE, null, ex);
		}
		channel.send(outputWhole);
		}
		try {
		    Thread.sleep(40);
		} catch (InterruptedException ex) {
		    break;
		}
	    }
	try {
	    AnalyticsServerProtocol.getInstance().processEvent(new UserEvent(UserEvent.disconnect, new Date().getTime(), currentUser.getUsername()));
	} catch (EventNotFoundException enf) {
	    // TODO Auto-generated catch block
	    System.out.println(enf.getMessage());
	} catch (NullPointerException ex) {
	    try {
		AnalyticsServerProtocol.getInstance().processEvent(new UserEvent(UserEvent.disconnect, new Date().getTime(), "Unknown User"));
	    } catch (EventNotFoundException ex1) {
		Logger.getLogger(tcpRequestCommunication.class.getName()).log(Level.SEVERE, null, ex1);
	    }
	}
	System.out.println("Exiting tcpRequestCommunication");
    }
}
