package dslab.analyticsserver;

public class BidEvent extends Event{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8278966241550211016L;
	private String userName;
	private long auctionID;
	private double price;
	public static final String placed = "BID_PLACED";
	public static final String overbid = "BID_OVERBID";
	public static final String won = "BID_WON";

	public BidEvent(String type, long timestamp, String userName, long auctionID, double price) throws EventNotFoundException{
		if (!type.equals(placed) && !type.equals(overbid) && !type.equals(won)){
			throw new EventNotFoundException();
		}
		this.type = type;
		this.timestamp = timestamp;
		this.userName = userName;
		this.auctionID = auctionID;
		this.price = price;
		
	}


	public long getAuctionID() {
		return auctionID;
	}


	public double getPrice() {
		return price;
	}


	public String getUserName() {
		return userName;
	}

}
