package dslab.auctionserver;

public class TentativeBid {
	User initiator = null;
	Auction auction = null;
	double amount = 0;
	int confirms = 0;
	boolean confirmed = false;

	public TentativeBid(User initiator, Auction auction, double amount){
		this.initiator = initiator;
		this.auction = auction;
		this.amount = amount;
	}
	public User getInitiator() {
		return initiator;
	}

	public void setInitiator(User initiator) {
		this.initiator = initiator;
	}

	public Auction getAuction() {
		return auction;
	}

	public void setAuction(Auction auction) {
		this.auction = auction;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	synchronized public void confirm(){
		confirms++;
		if (confirms >= 2){
			confirmed = true;
		}
	}
}
