/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dslab.loadtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Robert Rainer
 */
public class Test {

    private static String host;
    private static int port;
    private static String analyticsName;
    private static int auctionDuration;
    private static int auctionsPerMin;
    private static int bidsPerMin;
    private static int clients;
    private static int updateIntervalSec;
    private static ExecutorService executor;
    public static Date start;

    public static void main(String args[]) throws IOException, InterruptedException {
	
	start = new Date();
	
	if (args.length != 3) {
	    System.out.print("3 arguments required!");
	    return;
	}

	host = args[0];
	port = Integer.parseInt(args[1]);
	analyticsName = args[2];

	setMyProperties();
	
	executor = Executors.newCachedThreadPool();
	
	//TODO mgmtclient;
	
	for(int i=0; i<clients; ++i){
	    TestClient client = new TestClient(port, auctionDuration, auctionsPerMin, bidsPerMin, updateIntervalSec, host, i);
	    
	    executor.execute(client);
	    Thread.sleep((new Random()).nextInt(10));
	}
	
	
	BufferedReader stdIn = new BufferedReader(
	    new InputStreamReader(System.in));
	    String fromUser;
	while ((fromUser = stdIn.readLine()) != null) {
		if (fromUser.equals("!exit")){
		    break;
		} else{
		    System.out.println("you typed: "+fromUser);
		}
	}
	
	executor.shutdown();
    }

    /**
     * Reads the registry properties and stores the host and port values
     */
    private static void setMyProperties() {
	java.io.InputStream is = ClassLoader.getSystemResourceAsStream("loadtest.properties");
	if (is != null) {
	    java.util.Properties props = new java.util.Properties();
	    try {
		props.load(is);
		clients = Integer.parseInt(props.getProperty("clients"));
		auctionsPerMin = Integer.parseInt(props.getProperty("auctionsPerMin"));
		auctionDuration = Integer.parseInt(props.getProperty("auctionDuration"));
		updateIntervalSec = Integer.parseInt(props.getProperty("updateIntervalSec"));
		bidsPerMin = Integer.parseInt(props.getProperty("bidsPerMin"));
	    } catch (IOException e) {
		System.out.println("properties loading failed");
	    } finally {
		try {
		    
		    is.close();
		} catch (IOException e) {
		    System.out.println("properties stream not closed");
		}
	    }
	} else {
	    System.out.println("Properties for load test not found");
	}
    }
}
