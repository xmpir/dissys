/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dslab.loadtest;

import dslab.analyticsserver.AnalyticsCallbackInterface;
import dslab.managementclient.EventListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public static boolean active = true;
    private static EventListener listener;
    private static Registry registry;
    private static AnalyticsCallbackInterface callback;
    private static String registryHost;
    private static String registryPort;

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
	
	try {
	    registry = LocateRegistry.getRegistry(registryHost, Integer.parseInt(registryPort));
	} catch (RemoteException ex) {
	    System.out.println("Registry could not be located");
	}
	try {
	    callback = (AnalyticsCallbackInterface) registry.lookup(analyticsName);
	} catch (RemoteException ex) {
	    System.out.println("Remote Exception looking up the AnalyticsServer");
	    return;
	} catch (NotBoundException ex) {
	    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
	}

	try {
	    listener = new EventListener();
	} catch (RemoteException e) {
	    System.err.println(e.getCause());
	}
	
	System.out.println(callback.subscribe(listener, ".*"));
	listener.setAuto(true);
	

	executor = Executors.newCachedThreadPool();

	//TODO mgmtclient;

	for (int i = 0; i < clients; ++i) {
	    TestClient client = new TestClient(port, auctionDuration, auctionsPerMin, bidsPerMin, updateIntervalSec, host, i);
	    executor.execute(client);
	}

	BufferedReader stdIn = new BufferedReader(
		new InputStreamReader(System.in));
	String fromUser;
	while ((fromUser = stdIn.readLine()) != null) {
	    if (fromUser.equals("!exit")) {
		break;
	    } else {
		System.out.println("you typed: " + fromUser);
	    }
	}
	stdIn.close();
	
	active=false;
	executor.shutdown();
	executor.shutdownNow();
	/*try {
	    registry.unbind(args[2]);
	} catch (RemoteException ex) {
	    System.out.println("RemoteException while unbinding the billing-server");
	} catch (NotBoundException ex) {
	    System.out.println("NotBoundException while unbinding the billing-server");
	}
	*/
	UnicastRemoteObject.unexportObject(listener, true);
	System.out.println("Good Bye");
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
	
	is = ClassLoader.getSystemResourceAsStream("registry.properties");
	if (is != null) {
	    java.util.Properties props = new java.util.Properties();
	    try {
		    props.load(is);
		
		registryHost = props.getProperty("registry.host");
		registryPort = props.getProperty("registry.port");
		//Data.getInstance().initUsers();
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
	    System.out.println("Properties file not found!");
	}
    }
}
