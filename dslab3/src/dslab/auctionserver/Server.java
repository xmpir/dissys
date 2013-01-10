package dslab.auctionserver;

import dslab.analyticsserver.AnalyticsCallbackInterface;
import dslab.billingserver.BillingServerInterface;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.io.*;
import java.net.ServerSocket;
import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.Security;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

	private static Registry registry;
	private static String registryHost;
	private static String registryPort;
	private static BillingServerInterface billingServerStub;
	private static AnalyticsCallbackInterface callbackStub;

	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		int tcpPort = 0;
		BufferedReader stdIn = new BufferedReader(
				new InputStreamReader(System.in));
		ServerSocket serverSocket;
		ScheduledExecutorService scheduler;
		Updater updater;

		try {
			setMyProperties();


		} catch (IOException ex) {
			System.out.println("Properties file not found!");
			return;
		}
		initializeRmi(args[1], args[2]);

		//stuff for channel communication and key initiation
		Data.getInstance().setKeydirpath(args[4]);
		Data.getInstance().setPathToServerPrivKey(args[3]);
		Data.getInstance().initKeys();


		try {
			tcpPort = Integer.parseInt(args[0]);
			serverSocket = new ServerSocket(tcpPort);
			ServerListener s = new ServerListener(serverSocket);
			s.start();
			scheduler = Executors.newScheduledThreadPool(2);
			updater = new Updater();
			scheduler.scheduleAtFixedRate(updater, 1000, 1000, MILLISECONDS);

			String command = "";
			while ((command = stdIn.readLine()) != null) {
				if (command.equals("!close")){
					s.interrupt();
					Data.getInstance().logoutUsers();
					serverSocket.close();
				}
				else if (command.equals("!reconnect")){
					serverSocket = new ServerSocket(tcpPort);
					s = new ServerListener(serverSocket);
					s.start();
					System.out.println("Reconnected");
					}
				else if (command.equals("!exit")){
					//shut down
					s.interrupt();
					Data.getInstance().logoutUsers();
					serverSocket.close();
					stdIn.close();
					scheduler.shutdown();
					break;
				}

				//unbind and unexport RMI stuff
				/*try {
		 registry.unbind(args[2]);
		 } catch (RemoteException ex) {
		 System.out.println("RemoteException while unbinding the billing-server");
		 } catch (NotBoundException ex) {
		 System.out.println("NotBoundException while unbinding the billing-server");
		 } 
		 try {
		 registry.unbind(args[1]);
		 } catch (RemoteException ex) {
		 System.out.println("RemoteException while unbinding the analytics-server");
		 } catch (NotBoundException ex) {
		 System.out.println("NotBoundException while unbinding the analytics-server");
		 }

		 try {
		 UnicastRemoteObject.unexportObject(billingServerStub, true);
		 } catch (NoSuchObjectException ex) {
		 System.out.println("no billingServerStub object to unexport");	    
		 }
		 try {
		 UnicastRemoteObject.unexportObject(BillingServerProtocol.getInstance().getSecure(), true);
		 } catch (NoSuchObjectException ex) {
		 System.out.println("no BillingServerSecure object to unexport");	    
		 }
		 try {
		 UnicastRemoteObject.unexportObject(callbackStub, true);
		 } catch (NoSuchObjectException ex) {
		 System.out.println("no callbackStub object to unexport");	    
		 }*/
			}
		} catch (NumberFormatException e) {
			System.err.println("tcpPort must be an integer!");
		} catch (IOException e2) {
			//e2.printStackTrace();
			System.err.println("IOException1");
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

		try {
			callbackStub = (AnalyticsCallbackInterface) registry.lookup(analyticsName);
		} catch (RemoteException ex) {
			System.out.println("Connection to AnalyticsServer not established");
		} catch (NotBoundException ex) {
			System.out.println("AnalyticsServer not bound");
		}

		AnalyticsServerProtocol.getInstance().setCallback(callbackStub);
		BillingServerProtocol.getInstance().setBillingServer(billingServerStub);
		BillingServerProtocol.getInstance().login();
		//System.out.println("logged in on the billingServer");

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
