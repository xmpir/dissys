package dslab.auctionserver;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Date;

import dslab.analyticsserver.AuctionEvent;
import dslab.analyticsserver.BidEvent;
import dslab.analyticsserver.EventNotFoundException;
import dslab.analyticsserver.UserEvent;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Protocol {

    private User currentUser;

    Protocol(User currentUser) {
	this.currentUser = currentUser;
    }

    public String processInput(String inputWhole, tcpRequestCommunication request) throws UnknownParameterException {
	if (inputWhole != null) {
	    String[] input = inputWhole.split(" ");
	    if (input.length > 0) {
		if (input[0].equals("!list")) {
		    return this.list(input);
		} else {
		    //check if the user is logged in
		    if (request.getCurrentUser() != null && request.isShakedHands()) {
			//go on
		    } else {
			try {
			    return request.shakeHands(inputWhole);


			} catch (InvalidAlgorithmParameterException ex) {
			    Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
			} catch (NoSuchAlgorithmException ex) {
			    Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
			} catch (NoSuchPaddingException ex) {
			    Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
			} catch (InvalidKeyException ex) {
			    Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IllegalBlockSizeException ex) {
			    Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
			} catch (BadPaddingException ex) {
			    Logger.getLogger(Protocol.class.getName()).log(Level.SEVERE, null, ex);
			}
		    }
		}
		if (input[0].equals("!logout")) {
		    return this.logout(input, request);
		}
		if (input[0].equals("!create")) {
		    return this.create(input);
		}
		if (input[0].equals("!end")) {
		    return this.end(input);
		}
		if (input[0].equals("!bid")) {
		    return this.bid(input);
		}
		return "Command not recognized! Use !login, !logout, !list, !create, !bid or !end!";
	    }
	    return "Empty input!";
	}
	return "No input!";
    }

    private String logout(String[] input, tcpRequestCommunication request) {
	//TODO: kill the secure channel
	
	if (input.length == 1) {
	    if (currentUser != null) {
		currentUser.logout();
		String username = currentUser.getUsername();
		currentUser = null;
		try {
		    AnalyticsServerProtocol.getInstance().processEvent(new UserEvent(UserEvent.logout, new Date().getTime(), username));
		} catch (EventNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		return "!logout" + System.getProperty("line.separator") + "Successfully logged out as " + username + "!";
	    }
	    return "You have to log in first!";
	}
	return "Wrong command: !logout requires no additional argument! Usage: !logout";
    }

    private String create(String[] input) {
	if (input.length >= 3) {
	    if (currentUser != null) {
		long duration;
		try {
		    duration = Long.parseLong(input[1]);
		} catch (NumberFormatException e) {
		    return "duration must be a long! Please try again!";
		}
		String description = input[2];
		for (int i = 3; i < input.length; i++) {
		    description += " " + input[i];
		}
		Auction auction = new Auction(description, duration, new Date(), currentUser);
		synchronized (Data.getInstance()) {
		    Data.getInstance().addAuction(auction);
		    try {
			int i = Data.getInstance().getAuctionIndex(auction);
			AnalyticsServerProtocol.getInstance().processEvent(new AuctionEvent(AuctionEvent.started, new Date().getTime(), Auction.getCounter() - 1));
		    } catch (EventNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
		return "An auction '" + auction.getDescription() + "' with id " + auction.getId() + " has been created and will end on " + auction.getEndDate() + ".";
	    }
	    return "You have to log in to create an auction item!";
	}
	return "Wrong command: !create requires at least two additional arguments! Usage: !create <duration> <description>";

    }

    private String end(String[] input) {

	if (input.length == 1) {
	    if (currentUser != null) {
		currentUser.logout();
		currentUser = null;
		return "!end";
	    }
	    return "!end";
	}
	return "Wrong command: !end requires no additional argument! Usage: !end";
    }

    private String bid(String[] input) {
	if (input.length == 3) {
	    if (currentUser != null) {
		int id;
		try {
		    id = Integer.parseInt(input[1]);
		} catch (NumberFormatException e) {
		    return "auctionid must be an integer! Please try again!";
		}
		double amount;
		try {
		    amount = Double.parseDouble(input[2]);
		} catch (NumberFormatException e) {
		    return "amount must be a BigDecimal! Please try again!";
		}
		Auction auction = new Auction(id);
		int index = Data.getInstance().getAuctionIndex(auction);
		if (index == -1) {
		    return "No such id exists! Please check !list again!";
		}
		auction = Data.getInstance().getAuction(index);
		String bidOut = auction.bid(currentUser, amount);
		if (bidOut.equals("You can't bid any more!")) {
		    return bidOut;
		}
		boolean success = Boolean.parseBoolean(bidOut);
		if (success) {
		    try {
			AnalyticsServerProtocol.getInstance().processEvent(new BidEvent(BidEvent.placed, new Date().getTime(), currentUser.getUsername(), id, amount));
		    } catch (EventNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    return "You successfully bid with " + amount + " on '" + auction.getDescription() + "'.";
		}
		return "You unsuccessfully bid with " + amount + " on '" + auction.getDescription() + "'. Current highest bid is " + auction.getHighestBid() + ".";
	    }
	    return "You have to log in to bid on an auction item!";
	}
	return "Wrong command: !bid requires two additional arguments! Usage: bid <auction-id> <amount>";
    }

    private String list(String[] input) {
	if (input.length == 1) {
	    return Data.getInstance().listAuctions();
	}
	return "Wrong command: !list requires no additional argument! Usage: !list";
    }
}