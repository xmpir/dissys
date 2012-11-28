package dslab.auctionserver;

import java.rmi.RemoteException;

import dslab.analyticsserver.AnalyticsCallbackInterface;
import dslab.analyticsserver.Event;



public class AnalyticsServerProtocol {
	private static AnalyticsServerProtocol instance;
	private AnalyticsCallbackInterface callback;


	public AnalyticsCallbackInterface getCallback() {
		return callback;
	}

	public void setCallback(AnalyticsCallbackInterface callback) {
		this.callback = callback;
	}

	public static AnalyticsServerProtocol getInstance(){
		if(instance==null){
			instance=new AnalyticsServerProtocol();
		}
		return instance;
	}

	private AnalyticsServerProtocol(){
	}
	
	public void processEvent(Event e){
		try {
		    callback.processEvent(e);
		    System.out.println("Event processed");
		} catch (RemoteException ex) {
		    System.out.println("unable to process Event");
		}
		
	}


}
