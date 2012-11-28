package dslab.analyticsserver;

import java.io.Serializable;

import dslab.auctionserver.Auction;

public abstract class Event implements Serializable{
	private static final long serialVersionUID = -5505577835400467268L;
	private static int counter = 0;
	private final String id = "" + incrementCount();
	protected String type;
	protected long timestamp;
	
	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public long getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}


	public String getId() {
		return id;
	}


	public static synchronized int incrementCount(){
		return counter++;
	}
	
	@Override
	public  boolean equals(Object event){
		if (event == null){
			return false;
		}
		if (getClass() != event.getClass()){
			return false;
		}
		return (getId().equals(((Event)event).getId()));
	}

}
