package dslab.managementclient;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dslab.analyticsserver.AuctionEvent;
import dslab.analyticsserver.BidEvent;
import dslab.analyticsserver.Event;
import dslab.analyticsserver.EventListenerInterface;
import dslab.analyticsserver.StatisticsEvent;
import dslab.analyticsserver.Subscription;
import dslab.analyticsserver.UserEvent;

public class EventListener extends UnicastRemoteObject implements EventListenerInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4599723981272900983L;
	private boolean auto = false;
	private List<String> buffer = Collections.synchronizedList(new ArrayList<String>());
	
	public EventListener() throws RemoteException{
		super();

	}
	public synchronized boolean isAuto() {
		return auto;
	}
	

	public synchronized void setAuto(boolean auto) {
		this.auto = auto;
	}
	
	private synchronized void addToBuffer(String s){
		buffer.add(s);
	}
	
	public synchronized void printBuffer(){
		String printBuffer = "";
		for (String s : buffer){
			printBuffer += s + System.getProperty("line.separator");
		}
		System.out.println(printBuffer);
		buffer = Collections.synchronizedList(new ArrayList<String>());
	}
	
	private void process(String s){
		if (isAuto()){
			System.out.println(s);
		}
		else {
			addToBuffer(s);
		}
		
	}

	

	public void processEvent(Event event) throws RemoteException{
		if (event instanceof AuctionEvent){
			process(processEvent((AuctionEvent)event));
		}
		else if (event instanceof UserEvent){
			process(processEvent((UserEvent)event));
		}
		else if (event instanceof BidEvent){
			process(processEvent((BidEvent)event));
		}
		else if (event instanceof StatisticsEvent){
			process(processEvent((StatisticsEvent)event));
		}
	}

	private String processEvent(AuctionEvent event) throws RemoteException{
		SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm z");
		String timestamp = displayFormat.format(event.getTimestamp());
		if (event.getType().equals(AuctionEvent.started)){
			return event.getType() + ": " + timestamp + " - auction " + event.getAuctionID() + " started";
			
		}
		else if (event.getType().equals(AuctionEvent.ended)){
			return event.getType() + ": " + timestamp + " - auction " + event.getAuctionID() + " ended";
			
		}
		return "Should not happen!";
	}
	
	private String processEvent(UserEvent event) throws RemoteException{
		SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm z");
		String timestamp = displayFormat.format(event.getTimestamp());
		if (event.getType().equals(UserEvent.login)){
			return event.getType() + ": " + timestamp + " - user " + event.getUserName() + " logged in";
			
		}
		else if (event.getType().equals(UserEvent.logout)){
			return event.getType() + ": " + timestamp + " - user " + event.getUserName() + " logged out";
			
		}
		else if (event.getType().equals(UserEvent.disconnect)){
			return event.getType() + ": " + timestamp + " - user " + event.getUserName() + " disconnected";
			
		}
		
		return "Should not happen!";
	}
	
	private String processEvent(BidEvent event) throws RemoteException{
		SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm z");
		String timestamp = displayFormat.format(event.getTimestamp());
		if (event.getType().equals(BidEvent.placed)){
			return event.getType() + ": " + timestamp + " - user " + event.getUserName() + " placed bid " + event.getPrice() + " on auction " + event.getAuctionID();
			
		}
		else if (event.getType().equals(BidEvent.overbid)){
			return event.getType() + ": " + timestamp + " - user " + event.getUserName() + " has been overbid with " + event.getPrice() + " on auction " + event.getAuctionID();
			
		}
		else if (event.getType().equals(BidEvent.won)){
			return event.getType() + ": " + timestamp + " - user " + event.getUserName() + " has has won auction " + event.getAuctionID() + " with " + event.getPrice();
			
		}

		return "Should not happen!";
	
	}
	
	private String processEvent(StatisticsEvent event) throws RemoteException{
		SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm z");
		String timestamp = displayFormat.format(event.getTimestamp());
		if (event.getType().equals(StatisticsEvent.userMin)){
			return event.getType() + ": " + timestamp + " minimum session time is " + event.getValue()+ " seconds ";
			
		}
		else if (event.getType().equals(StatisticsEvent.userMax)){
			return event.getType() + ": " + timestamp + " maximum session time is " + event.getValue()+ " seconds ";
			
		}
		else if (event.getType().equals(StatisticsEvent.userAvg)){
			return event.getType() + ": " + timestamp + " average session time is " + event.getValue()+ " seconds ";
			
		}
		else if (event.getType().equals(StatisticsEvent.bidMax)){
			return event.getType() + ": " + timestamp + " maximum bid price seen so far is " + event.getValue();
			
		}
		else if (event.getType().equals(StatisticsEvent.bidCount)){
			return event.getType() + ": " + timestamp + " current bids per minute is " + event.getValue();
		}
		else if (event.getType().equals(StatisticsEvent.auctionAvg)){
			return event.getType() + ": " + timestamp + " average auction time is " + event.getValue()+ " seconds ";
			
		}
		else if (event.getType().equals(StatisticsEvent.auctionRatio)){
			return event.getType() + ": " + timestamp + " auction success ratio is " + event.getValue();
			
		}
		
		return "Should not happen!";
	
	}

}
