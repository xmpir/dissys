package dslab.auctionserver;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Auction{
	private static int counter = 1;
	private Date end;
	private String description;
	private int id;
	private User creator;
	private User highestBidder;
	private double highestBid;
	private boolean included = true;
	private User temp;
	private boolean bid = false;

	Auction(String description, long duration, Date now, User creator){
		end = new Date(now.getTime() + duration * 1000);
		this.description = description;
		id = incrementCount();

		this.creator = creator;
		highestBidder = new User("none");
		highestBid = 0.0;
	}

	Auction(int id){
		this.id = id;
	}

	public synchronized User getHighestBidder() {
		return highestBidder;
	}

	public synchronized void setHighestBidder(User highestBidder) {
		this.highestBidder = highestBidder;
	}

	public synchronized double getHighestBid() {
		return highestBid;
	}

	public synchronized void setHighestBid(double highestBid) {
		this.highestBid = highestBid;
	}

	public synchronized String bid(User user, double amount){
		Date now = new Date();
		if (now.after(end)){
			return "You can't bid any more!";
		}
		if (amount>highestBid){
			temp = highestBidder;
			highestBidder = user;
			highestBid = amount;
			/* UDP:
			if (bid){
			    String message = "!new-bid " + getDescription();
			    if (temp.isActive()){
				    ServerNotifier sn = new ServerNotifier(temp.getAddress(), temp.getUdpPort());
				    sn.send(message);
			    }
			    else {
				    temp.addMessage(message);
			    }
			}*/
			bid = true;
			return "true";
		}
		return "false";
	}

	public synchronized Date getEnd() {
		return end;
	}
	public synchronized void setEnd(Date end) {
		this.end = end;
	}

	public synchronized boolean isIncluded() {
		return included;
	}

	public synchronized void setIncluded(boolean included) {
		this.included = included;
	}

	@Override
	public synchronized boolean equals(Object auction){
		if (auction == null){
			return false;
		}
		if (getClass() != auction.getClass()){
			return false;
		}
		return (getId() == ((Auction)auction).getId());
	}

	public synchronized int getId() {
		return id;
	}

	public synchronized String getDescription() {
		return description;
	}

	public synchronized String getEndDate(){
		SimpleDateFormat displayFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm z");
		return displayFormat.format(end);
	}

	public synchronized User getCreator(){
		return creator;
	}

	public synchronized String toString(){
		String out = getId() + ". '" + getDescription() + "' ";
		out += getCreator().getUsername() + " " + getEndDate() + " ";
		if (bid){
			out += getHighestBid();
		}
		else {
			out += "0.00";
		}
		out += " " + getHighestBidder();
		return out;
	}
	public synchronized boolean isBid() {
		return bid;
	}
	public synchronized void setBid(boolean bid) {
		this.bid = bid;
	}

	public static synchronized int incrementCount(){
		return counter++;
	}
}
