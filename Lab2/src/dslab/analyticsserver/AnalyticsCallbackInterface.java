/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dslab.analyticsserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AnalyticsCallbackInterface extends Remote{

    public String subscribe(EventListenerInterface eventI, String regEx) throws RemoteException;
    
    public void processEvent(Event event) throws RemoteException;
    
    public String unsubscribe(int id) throws RemoteException;
    
}
