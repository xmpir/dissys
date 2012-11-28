package dslab.analyticsserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EventListenerInterface extends Remote{

	public void processEvent(Event event) throws RemoteException;
}
