/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dslab.billingserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author rr
 */
public interface BillingServerInterface extends Remote{
    
    /**
     * 
     * @param name Username
     * @param password password
     * @return a BillingServerSecure-Object that offers the functionality
     */
    public BillingServerSecureInterface login(String name, String password) throws RemoteException;
    
}
