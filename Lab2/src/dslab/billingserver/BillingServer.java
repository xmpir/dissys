/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.billingserver;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert Rainer
 */
public class BillingServer implements BillingServerInterface, Serializable{

    private static MessageDigest md;
    private static String registryHost;
    private static String registryPort;
    private Registry registry;
    
    public void setMyProperties() throws IOException{
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
    
    
    public void start(String[] args) throws IOException {
	
	if(args.length!=1){
	    System.out.println("only one argument is allowed");
	    return;
	}
	
	try {
	    md = MessageDigest.getInstance("MD5");
	} catch (NoSuchAlgorithmException ex) {
	    System.out.println
		   ("md5 algorithm could not be found");
	}
	
	
	this.setMyProperties();
	
	//the following code is more or less taken from:
	//http://docs.oracle.com/javase/tutorial/rmi/implementing.html
	//TODO look for the registry that may have been created by the analysis server
	try {
            BillingServerInterface billingServer = new BillingServer();
            BillingServerInterface stub =
                (BillingServerInterface) UnicastRemoteObject.exportObject(billingServer, 0);
            
	    registry = LocateRegistry.createRegistry(Integer.parseInt(registryPort));
            registry.rebind(args[0], stub);
            System.out.println("BillingServer bound");
        } catch (Exception e) {
            System.err.println("BillingServer exception:");
            e.printStackTrace();
        }
    }
    
    @Override
    public BillingServerSecureInterface login(String name, String password) throws RemoteException{
        
	System.out.println(name +" is trying to login");
	
	String md5password=null;
	
	java.io.InputStream is = ClassLoader.getSystemResourceAsStream("user.properties");
	if (is != null) {
	java.util.Properties props = new java.util.Properties();
	try {
		try {
		    props.load(is);
		} catch (IOException ex) {
		    Logger.getLogger(BillingServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	md5password = props.getProperty(name);
	} finally {
		try {
		    is.close();
		} catch (IOException ex) {
		    Logger.getLogger(BillingServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	} else {
	System.err.println("User file not found!");
	throw new RemoteException("the Userdata of the billing-client seems not to be there...");
	}
	
	if(md5password != null){
	    if(this.getMd5(password).equals(md5password)){
		BillingServerSecureInterface clientInstance = new BillingServerSecure();
		return clientInstance;
	    }
	}
	
	throw new InvalidArgumentsException("unknown credentials");
    }

    
    private String getMd5(String input){
	    md.reset();
	    md.update(input.getBytes());
	    byte[] digest = md.digest();
	    BigInteger bigInt = new BigInteger(1,digest);
	    String hashtext = bigInt.toString(16);
	    // Now we need to zero pad it if you actually want the full 32 chars.
	    while(hashtext.length() < 32 ){
	      hashtext = "0"+hashtext;
	    }
	    return hashtext;
    }
    
    public void shutdown() throws RemoteException, AccessException, NotBoundException{
	registry.unbind(registryHost);
    }
    
}
