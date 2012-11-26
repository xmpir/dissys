package dslab.auctionserver;

import java.util.ArrayList;
import java.util.Date;

public class Lists {
	ArrayList<User> user = new ArrayList<User>();
	ArrayList<Auction> auctions = new ArrayList<Auction>();
	
	Lists(){
	}

	synchronized public void addUser(User u){
		user.add(u);
	}
	
	synchronized public int getUserIndex(User u){
		return user.indexOf(u);
	}
	
	synchronized public User getUser(int i){
		return user.get(i);
	}
	
	synchronized public int getAuctionIndex(Auction a){
		return auctions.indexOf(a);
	}
	
	synchronized public Auction getAuction(int i){
		return auctions.get(i);
	}
	
	
	synchronized public void addAuction(Auction a){
		auctions.add(a);
	}
	
	synchronized public String listAuctions(){
		String out = "";
		int i = 0;
		for (Auction a: auctions){
			if (a.isIncluded() == true){
				i++;
				out += a.toString() + System.getProperty("line.separator");
			}
		}
		if (i == 0){
			out = "Currently there are no auctions!";
		}
		return out;
	}
	
	synchronized public void logoutUsers(){
		for (User u : user){
			u.logout();
		}
	}
	
	synchronized public void updateAuctions(){
		for (Auction a : auctions){
			Date now = new Date();
			if (a.isIncluded() && now.after(a.getEnd())){
				a.setIncluded(false);
				String message = "!auction-ended " + a.getHighestBidder()  + " " + a.getHighestBid() + " " + a.getDescription();
				if (a.getCreator().isActive()){
					ServerNotifier creator = new ServerNotifier(a.getCreator().getAddress(), a.getCreator().getUdpPort());
					creator.send(message);
				}
				else {
					a.getCreator().addMessage(message);
				}
				if (a.isBid()){
					if (a.getHighestBidder().isActive()){
						ServerNotifier highestBidder = new ServerNotifier(a.getHighestBidder().getAddress(), a.getHighestBidder().getUdpPort());
						highestBidder.send(message);
					}
					else {
						a.getHighestBidder().addMessage(message);
					}
				}
			}
		}
	}
	
}
