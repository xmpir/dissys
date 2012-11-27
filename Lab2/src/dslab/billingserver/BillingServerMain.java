/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.billingserver;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Robert Rainer
 */
public class BillingServerMain {
    
    private static BillingServer bs;
    
public static void main(String[] args) throws IOException {
	
    ioReader ioReader = new ioReader();
    ioReader.start();
    
    bs = new BillingServer();
    bs.start(args);
    }
    
    public static void shutdown() throws RemoteException, AccessException, NotBoundException{
	bs.shutdown();
    }

}
