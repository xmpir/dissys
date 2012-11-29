package dslab.analyticsserver;

import java.util.regex.Pattern;


public class Subscription{
	private static int counter = 0;
	private final int id;
	Pattern pattern;
	EventListenerInterface eventI;

	public Subscription(Pattern pattern, EventListenerInterface eventI) {
		this.pattern = pattern;
		this.eventI = eventI;
		id = incrementCount();
	}
	
	public int getID(){
		return id;
	}
	
	public Pattern getPattern() {
		return pattern;
	}

	public static synchronized int incrementCount(){
		return counter++;
	}

	public EventListenerInterface getEventI() {
		return eventI;
	}
	
	@Override
	public boolean equals(Object subscription){
		if (subscription == null){
			return false;
		}
		if (getClass() != subscription.getClass()){
			return false;
		}
		return (getID() == ((Subscription)subscription).getID());
	}

}
