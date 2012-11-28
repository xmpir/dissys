package dslab.analyticsserver;

import java.util.ArrayList;

public class StatisticsEvent extends Event{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6387323532810407969L;
	private double value;
	public static final String userMin = "USER_SESSIONTIME_MIN";
	public static final String userMax = "USER_SESSIONTIME_MAX";
	public static final String userAvg = "USER_SESSIONTIME_AVG";
	public static final String bidMax = "BID_PRICE_MAX";
	public static final String bidCount = "BID_COUNT_PER_MINUTE";
	public static final String auctionAvg = "AUCTION_TIME_AVG";
	public static final String auctionRatio = "AUCTION_SUCCESS_RATIO";
	

	public StatisticsEvent(String type, long timestamp, double value) throws EventNotFoundException{
		if (!type.equals(userMin) && !type.equals(userMax) && !type.equals(userAvg) &&
				!type.equals(bidMax) && !type.equals(bidCount) &&
				!type.equals(auctionAvg) && !type.equals(auctionRatio)){
			throw new EventNotFoundException();
		}
		this.type = type;
		this.timestamp = timestamp;
		this.value = value;
		
	}


	public double getValue() {
		return value;
	}
	
	
}
