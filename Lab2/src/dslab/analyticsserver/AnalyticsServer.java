/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dslab.analyticsserver;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnalyticsServer {

    private static MessageDigest md;
    private static String registryHost;
    private static String registryPort;
    private static AnalyticsCallbackInterface analyticsCallback;
    private Registry registry;

    public void setMyProperties() throws IOException {
	java.io.InputStream is = ClassLoader.getSystemResourceAsStream("registry.properties");
	if (is != null) {
	    java.util.Properties props = new java.util.Properties();
	    try {
		props.load(is);
		registryHost = props.getProperty("registry.host");
		registryPort = props.getProperty("registry.port");
	    } finally {
		is.close();
	    }
	} else {
	    System.out.println("Properties file not found!");
	}
    }

    public void start(String[] args) throws IOException {

	if (args.length != 1) {
	    System.out.println("only one argument is allowed");
	    return;
	}

	try {
	    md = MessageDigest.getInstance("MD5");
	} catch (NoSuchAlgorithmException ex) {
	    System.out.println("md5 algorithm could not be found");
	}


	this.setMyProperties();

	//the following code is more or less taken from:
	//http://docs.oracle.com/javase/tutorial/rmi/implementing.html
	//TODO look for the registry that may have been created by the analysis server
	try {

	    analyticsCallback = new AnalyticsCallback();
	    AnalyticsCallbackInterface stub =
		    (AnalyticsCallbackInterface) UnicastRemoteObject.exportObject(analyticsCallback, 0);

	    try {
		registry = LocateRegistry.getRegistry(Integer.parseInt(registryPort));
		registry = LocateRegistry.createRegistry(Integer.parseInt(registryPort));
	    } catch (RemoteException e) {
		//do nothing, error means registry already exists
		System.out.println("java RMI registry already exists.");
	    }
	    registry.rebind(args[0], stub);
	    System.out.println("AnalyticsServer bound");
	} catch (Exception e) {
	    System.err.println("AnalyticsServer exception:");
	}
    }

    public void shutdown() {
	
	    try {
		registry.unbind(registryHost);
	    } catch (RemoteException re) {
		System.out.println("remote exception while unbinding");
	    } catch (NotBoundException nbe) {
		System.out.println("analyticsserver already unbound");
	    }
	    try {
	    UnicastRemoteObject.unexportObject(analyticsCallback, true);
	    } catch (NoSuchObjectException ex) {
		System.out.println("no object to unexport");	    
	    }


    }
}
