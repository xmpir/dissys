/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.bidclient;

import dslab.billingserver.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;

/**
 * "Datenbank" des Bid-Client
 * @author Robert Rainer
 */
public class Data {

    private static Data instance = null;
    private PrivateKey privateKeyClient = null;
    private PublicKey publicKeyClient = null;
    private PublicKey publicKeyServer = null;
    private String keydirpath = null;
    private String pathToServerKey = null;
    private String user = "alice";
    /**
     * Default-Konstruktor, der nicht außerhalb dieser Klasse
     * aufgerufen werden kann
     */
    private Data() {
	//hier sollten alle Objekt-Datentypen instanziert werden
	
    }
 
    /**
     * Statische Methode, liefert die einzige Instanz dieser
     * Klasse zurück
     */
    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    
    
    public void initKeys(){
	//first get the private key:
	PEMReader privIn = null, publIn = null;
	try {
	    //private key       
		    
	    privIn = new PEMReader(new FileReader(keydirpath+user+".pem"), new PasswordFinder() {
		@Override
		public char[] getPassword() {
		// reads the password from standard input for decrypting the private key
		System.out.println("Enter pass phrase:");
		    try {
			return new BufferedReader(new InputStreamReader(System.in)).readLine().toCharArray();
		    } catch (IOException ex) {
			return "".toCharArray();
		    }
		}
	    });
	} catch (FileNotFoundException ex) {
	   System.out.println(user+": cannot find his/her private key file...");
	}
	KeyPair keyPair = null; 
	try {
	    keyPair = (KeyPair) privIn.readObject();
	} catch (IOException ex) {
	    System.out.println("Wrong password");
	    initKeys();
	    return;
	}
	
	privateKeyClient = keyPair.getPrivate();
	//publicKeyClient = keyPair.getPublic();
	try {
	    publIn = new PEMReader(new FileReader(keydirpath+user+".pub.pem"));
	} catch (FileNotFoundException ex) {
	    System.out.println(user+": cannot find his/her public key...");
	}
	try {
	    publicKeyClient = (PublicKey) publIn.readObject();
	} catch (IOException ex) {
	    Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
	}
	try {
	    publIn = new PEMReader(new FileReader(this.pathToServerKey));
	} catch (FileNotFoundException ex) {
	    System.out.println(user+": cannot find the servers public key...");
	}
	try {
	    publicKeyServer = (PublicKey) publIn.readObject();
	} catch (IOException ex) {
	    Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
	}
	try {
	    privIn.close();
	    publIn.close();
	} catch (IOException ex) {
	    Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
	}
	
	
    }
    
    
   
    public PrivateKey getPrivateKeyClient() {
	return privateKeyClient;
    }

    public void setPrivateKeyClient(PrivateKey privateKeyClient) {
	this.privateKeyClient = privateKeyClient;
    }

    public PublicKey getPublicKeyClient() {
	return publicKeyClient;
    }

    public void setPublicKeyClient(PublicKey publicKeyClient) {
	this.publicKeyClient = publicKeyClient;
    }

    public PublicKey getPublicKeyServer() {
	return publicKeyServer;
    }

    public void setPublicKeyServer(PublicKey publicKeyServer) {
	this.publicKeyServer = publicKeyServer;
    }

    public String getKeydirpath() {
	return keydirpath;
    }

    public void setKeydirpath(String keydirpath) {
	this.keydirpath = keydirpath;
    }

    public String getPathToServerKey() {
	return pathToServerKey;
    }

    public void setPathToServerKey(String pathToServerKey) {
	this.pathToServerKey = pathToServerKey;
    }

    public String getUserName() {
	return user;
    }

    public void setUserName(String user) {
	this.user = user;
    }
    
    
    
}
