/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.auctionserver;

import dslab.billingserver.BillingServerInterface;
import dslab.billingserver.BillingServerSecureInterface;
import java.rmi.RemoteException;


/**
 *
 * @author Robert Rainer
 */
public class BillingServerProtocol {

    private static BillingServerProtocol instance;
    private BillingServerInterface billingServer;
    private BillingServerSecureInterface billingServerSecure;
    
    
    public static BillingServerProtocol getInstance(){
	if(instance==null){
	    instance=new BillingServerProtocol();
	}
	return instance;
    }
    
    private BillingServerProtocol(){
    }
    
    public void login(){
	try {
	    billingServerSecure = (BillingServerSecureInterface) billingServer.login("auctionServer", "3141592653");
	} catch (RemoteException ex) {
	    System.out.println("could not login on the billingserver (remote exception)");
	}
    }
    
    
    public BillingServerInterface getBillingServer() {
	return billingServer;
    }

    public void setBillingServer(BillingServerInterface billingServer) {
	this.billingServer = billingServer;
    }
    
    public void sendBill(String user, long auctionID, double price){
	try {
	    billingServerSecure.billAuction(user, auctionID, price);
	    System.out.println("bill sent to billingServer");
	} catch (RemoteException ex) {
	    System.out.println("unable to send the bill");
	}
    }
    
    
    
}
