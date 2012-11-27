package dslab.auctionserver;

import dslab.billingserver.BillingServerInterface;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.io.*;
import java.net.ServerSocket;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static Registry registry;
    private static String registryHost;
    private static String registryPort;
    private static BillingServerInterface billingServerStub;

    public static void main(String[] args) {
	int tcpPort = 0;
	Lists lists = Lists.getInstance();
	BufferedReader stdIn = new BufferedReader(
		new InputStreamReader(System.in));
	ServerSocket serverSocket;
	ScheduledExecutorService scheduler;
	Updater updater;

	if (args.length == 3) {
	    try {
		tcpPort = Integer.parseInt(args[0]);
		serverSocket = new ServerSocket(tcpPort);
		ServerListener s = new ServerListener(serverSocket, lists);
		s.start();
		scheduler = Executors.newScheduledThreadPool(2);
		updater = new Updater(lists);
		scheduler.scheduleAtFixedRate(updater, 1000, 1000, MILLISECONDS);
		if ((stdIn.readLine()) != null) {
		    s.interrupt();
		    lists.logoutUsers();
		    serverSocket.close();
		    stdIn.close();
		    scheduler.shutdown();
		}
	    } catch (NumberFormatException e) {
		System.err.println("tcpPort must be an integer!");
	    } catch (IOException e2) {
		System.err.println("IOException");
	    }
	    try {
		setMyProperties();
	    } catch (IOException ex) {
		System.out.println("Properties file not found!");
		return;
	    }
	    initializeRmi(args[1], args[2]);
	} else {
	    System.err.println("3 arguments required: tcpPort, billingserver, managementserver");
	}
    }

    private static void initializeRmi(String analyticsName, String billingName) {


	try {
	    registry = LocateRegistry.getRegistry(registryHost, Integer.parseInt(registryPort));
	} catch (RemoteException ex) {
	    System.out.println("Registry could not be located");
	}
	try {
	    billingServerStub = (BillingServerInterface) registry.lookup(billingName);
	} catch (RemoteException ex) {
	    System.out.println("Connection to BillingServer not established");
	} catch (NotBoundException ex) {
	    System.out.println("BillingServer not bound");
	}

	BillingServerProtocol.getInstance().setBillingServer(billingServerStub);
	BillingServerProtocol.getInstance().login();
	
	System.out.println("BillingServer bound");
	
    }

    public static void setMyProperties() throws IOException {
	java.io.InputStream is = ClassLoader.getSystemResourceAsStream("registry.properties");
	if (is != null) {
	    java.util.Properties props = new java.util.Properties();
	    try {
		props.load(is);
		registryHost = props.getProperty("registry.host");
		registryPort = props.getProperty("registry.port");
		//Data.getInstance().initUsers();
	    } finally {
		is.close();
	    }
	} else {
	    System.out.println("Properties file not found!");
	}
    }
}
