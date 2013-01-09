/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.billingserver;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.Charset;
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

/**
 *
 * @author Robert Rainer
 */
public class BillingServer implements BillingServerInterface, Serializable{

    private static MessageDigest md;
    private static String registryHost;
    private static String registryPort;
    private static String billingServerName;
    
    private BillingServerSecure clientInstance;
    private BillingServerInterface stub;
    private BillingServerSecureInterface secureStub;
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
	
	billingServerName = args[0];
	
	try {
	    md = MessageDigest.getInstance("MD5");
	} catch (NoSuchAlgorithmException ex) {
	    System.out.println
		   ("md5 algorithm could not be found");
	}
	
	
	this.setMyProperties();
	
	//the following code is more or less taken from:
	//http://docs.oracle.com/javase/tutorial/rmi/implementing.html
	   try{
               registry = LocateRegistry.getRegistry(Integer.parseInt(registryPort));
               registry = LocateRegistry.createRegistry(Integer.parseInt(registryPort));
           }
           catch (RemoteException e) {
               //do nothing, error means registry already exists
               System.out.println("java RMI registry already exists.");
           } 
           stub = (BillingServerInterface) UnicastRemoteObject.exportObject(this, 0);
           registry.rebind(billingServerName, stub);
           System.out.println("BillingServer bound");
    }
    
    @Override
    public BillingServerSecureInterface login(String name, String password) throws RemoteException{
        
	//System.out.println(name +" is trying to login");
	
	String md5password=null;
	java.io.InputStream is = ClassLoader.getSystemResourceAsStream("user.properties");
	if (is != null) {
	java.util.Properties props = new java.util.Properties();
	try {
		try {
		    props.load(is);
		} catch (IOException ex) {
		    System.out.println("IOException while reading properties");
		    throw new RemoteException("problems with the credentials");
		}
	md5password = props.getProperty(name);
	} finally {
		try {
		    is.close();
		} catch (IOException ex) {
		    //should not happen
		    System.out.println("Properties-stream could not be closed");
		}
	}
	} else {
	System.out.println("User.properties file not found!");
	throw new RemoteException("the Userdata of the billing-client seems not to be there...");
	}
	if(md5password != null){
	    if(this.getMd5(password).equals(md5password)){
		clientInstance = new BillingServerSecure();
		
		secureStub=  (BillingServerSecureInterface) UnicastRemoteObject.exportObject(clientInstance, 0);
		
		//System.out.println(name +" successfully logged in");
		return secureStub;
	    }
	}
	
	throw new RemoteException("bad credentials");
    }

    //a little helper for doing the md5-hashing
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
    
    
    //shutting down both the billingserver-stub and ...secure-stub
    public void shutdown(){
		
	try {
            registry.unbind(billingServerName);
        } catch (RemoteException re) {
            System.out.println("remote exception while unbinding");
        } catch (NotBoundException nbe) {
            System.out.println("billingserver already unbound");
        }
	try {
	    UnicastRemoteObject.unexportObject(clientInstance, true);
	} catch (NoSuchObjectException ex) {
            System.out.println("no secure billingstub to unexport");
	}
	try {
	    UnicastRemoteObject.unexportObject(this, true);
	} catch (NoSuchObjectException ex) {
            System.out.println("no billingstub to unexport");
	}
	
	
        
    }
    
}
