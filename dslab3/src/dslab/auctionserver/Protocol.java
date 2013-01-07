package dslab.auctionserver;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.Date;

import dslab.analyticsserver.AuctionEvent;
import dslab.analyticsserver.BidEvent;
import dslab.analyticsserver.EventNotFoundException;
import dslab.analyticsserver.UserEvent;

public class Protocol {

    private User currentUser;
    private Lists lists;

    Protocol(User currentUser, Lists lists) {
	this.lists = lists;
	this.currentUser = currentUser;
    }

    public String processInput(String inputWhole) throws UnknownParameterException {
	if (inputWhole != null) {
	    String[] input = inputWhole.split(" ");
	    if (input.length > 0) {
		if (input[0].equals("!login")) {
		    return this.login(input);
		}
		if (input[0].equals("!logout")) {
		    return this.logout(input);
		}
		if (input[0].equals("!create")) {
		    return this.create(input);
		}
		if (input[0].equals("!list")) {
		    return this.list(input);
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

    private String login(String[] input) {
	if (input.length == 2) {
	    if (currentUser == null) {
		currentUser = new User(input[1]);
		int index = lists.getUserIndex(currentUser);
		if (index == -1) {
		    lists.addUser(currentUser);
		} else {
		    currentUser = lists.getUser(index);
		    if (currentUser.isActive()) {
			currentUser = null;
			return "User already logged in!";
		    }
		}
		currentUser.setActive(true);
		//FIXME set the address of the client
		currentUser.getMessages();
		try {
		    AnalyticsServerProtocol.getInstance().processEvent(new UserEvent(UserEvent.login, new Date().getTime(), currentUser.getUsername()));
		} catch (EventNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		return "!login " + currentUser.getUsername() + System.getProperty("line.separator") + "Successfully logged in as " + currentUser.getUsername() + "!";
	    }
	    return "!login " + currentUser.getUsername() + System.getProperty("line.separator") + "Already logged in as " + currentUser.getUsername() + "! Please log out before you log in again!";
	}
	return "Wrong command: !login requires exactly one additional argument! Usage: !login name";
    }

    private String logout(String[] input) {
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
		synchronized (lists) {
		    lists.addAuction(auction);
		    try {
			int i = lists.getAuctionIndex(auction);
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
		int index = lists.getAuctionIndex(auction);
		if (index == -1) {
		    return "No such id exists! Please check !list again!";
		}
		auction = lists.getAuction(index);
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
	    return lists.listAuctions();
	}
	return "Wrong command: !list requires no additional argument! Usage: !list";
    }
}