package dslab.auctionserver;

public class UnknownParameterException extends Exception {
	private static final long serialVersionUID = 3606836519289795113L;
	
	UnknownParameterException(){
	}
	
	UnknownParameterException(String message){
		super(message);
	}
}
