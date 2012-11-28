/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.loadtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert Rainer
 */
public class TestClient extends Thread{

    private int tcpPort;
    private int auctionDuration;
    private int auctionsPerMin;
    private int bidsPerMin;
    private int updateIntervalSec;
    private int id;
    private String host;
    private Socket socket = null;
    private PrintWriter out = null;
    private final ArrayList<Integer> activeAuctions;
    private ExecutorService executor;
    public static Random zufall;
    //private ArrayList<Auction> activeAuctions;
    
    
    public TestClient(int tcpPort, int auctionDuration, int auctionsPerMin, int bidsPerMin, int updateIntervalSec, String host, int id) {
	this.tcpPort = tcpPort;
	this.auctionDuration = auctionDuration;
	this.auctionsPerMin = auctionsPerMin;
	this.bidsPerMin = bidsPerMin;
	this.updateIntervalSec = updateIntervalSec;
	this.host = host;
	this.id=id;
	executor = Executors.newFixedThreadPool(3);
	activeAuctions = new ArrayList<Integer>();
    }
    
    
    
    @Override
    public void run(){
	zufall = new Random();
	try {
	    socket = new Socket(host, tcpPort);
	    out = new PrintWriter(socket.getOutputStream(), true);
	} catch (UnknownHostException ex) {
	    Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
	}
	out.println("!login test"+id);
	out.flush();
	
	CreateThread creater = new CreateThread(this, auctionsPerMin, auctionDuration);
	BidThread bidder = new BidThread(this, bidsPerMin);
	UpdateThread updater = new UpdateThread(this, updateIntervalSec);
	
	executor.execute(creater);
	
	executor.execute(updater);
	
	executor.execute(bidder);
    }	
    
    public void bid(){
	if(activeAuctions.size()>0){
	Date now = new Date();
	double price = now.getTime()-Test.start.getTime();
	out.println("!bid "+zufall.nextInt(activeAuctions.size())+" "+price+"\n");
	out.flush();
	}
    }
    
   
    
    public void update(){
	BufferedReader in = null;
	try {
	    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    
	    while(!in.ready()){
		try {
		    Thread.sleep(11);
		} catch (InterruptedException ex) {
		    break;
		}
	    }
	    out.println("!list\n");
	    out.flush();
	    synchronized(activeAuctions){
	    activeAuctions.clear();
	    String line="  ";
		while(in.ready()){
		    line = in.readLine();
		    int index = line.indexOf(".");
		    if(index>0 && !line.startsWith("An auction") && !line.startsWith("You")){
		    activeAuctions.add(Integer.parseInt(line.substring(0, index)));
		    }
		}
	    }
	    System.out.println(id + "auctions synchronized:" + activeAuctions.size());
	} catch (IOException ex) {
	    System.out.println("error reading list from server");
	}
    }
    
    @Override
    public void interrupt(){
	out.close();
	executor.shutdown();
	executor.shutdownNow();
	super.interrupt();
    }

    void create(int auctionDuration) {
	out.println("!create "+auctionDuration+" productFromClient"+id);
	out.flush();
    }

    
}
