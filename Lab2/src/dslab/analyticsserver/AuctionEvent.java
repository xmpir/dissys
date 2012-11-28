package dslab.analyticsserver;

public class AuctionEvent extends Event{
	/**
	 * 
	 */
	private static final long serialVersionUID = -118352168943910355L;
	private long auctionID;
	public static final String started = "AUCTION_STARTED";
	public static final String ended = "AUCTION_ENDED";

	public AuctionEvent(String type, long timestamp, long auctionID) throws EventNotFoundException{
		if (!type.equals(started) && !type.equals(ended)){
			throw new EventNotFoundException();
		}
		this.type = type;
		this.timestamp = timestamp;
		this.auctionID = auctionID;
		
	}


	public long getAuctionID() {
		return auctionID;
	}

}
