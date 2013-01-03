package dslab.analyticsserver;

public class EventNotFoundException extends Exception {

	EventNotFoundException(){
	}
	
	EventNotFoundException(String message){
		super(message);
	}
}
