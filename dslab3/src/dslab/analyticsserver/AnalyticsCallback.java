package dslab.analyticsserver;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class AnalyticsCallback implements AnalyticsCallbackInterface{
	Pattern pattern;
	List<Subscription> subscriptions = Collections.synchronizedList(new ArrayList<Subscription>());
	List<UserEvent> userevents = Collections.synchronizedList(new ArrayList<UserEvent>());
	List<AuctionEvent> auctionevents = Collections.synchronizedList(new ArrayList<AuctionEvent>());
	List<BidEvent> bidevents = Collections.synchronizedList(new ArrayList<BidEvent>());


	long user_sessiontime_min = -1;
	long user_sessiontime_max = -1;
	long user_sessiontime_avg = -1;
	long auction_time_avg = -1;
	double auction_success_ratio = -1;

	private int sessionCounter = 0;
	private int auctionCounter = 0;
	private int successfulAuctionCounter = 0;
	private int bidCounter = 0;
	private Date dateStart = new Date();

	private long systemStart = dateStart.getTime();

	public AnalyticsCallback(){
	}

	@Override
	public String subscribe(EventListenerInterface eventI, String regEx) throws RemoteException{
		try{
			pattern = Pattern.compile(regEx);
		}
		catch (PatternSyntaxException e) {
			return "Error: You did not use a valid Regular Expression";
		}
		Subscription s = new Subscription(pattern, eventI);
		synchronized (subscriptions) {	
			subscriptions.add(s);
		}
		return "Created subscription with ID " + s.getID() + " for events using filter '" + regEx + "'";
	}

	@Override
	public void processEvent(Event event) throws RemoteException{
		if (event instanceof AuctionEvent){
			processEvent((AuctionEvent)event);
		}
		else if (event instanceof UserEvent){
			processEvent((UserEvent)event);
		}
		else if (event instanceof BidEvent){
			processEvent((BidEvent)event);
		}
		notify(event);

	}

	@Override
	public String unsubscribe(int id) throws RemoteException{
		for (int i = 0; i < subscriptions.size(); i++){
			if (subscriptions.get(i).getID() == id){
				synchronized (subscriptions) {	
					subscriptions.remove(subscriptions.get(i));
				}
				return "subscription "+ id + " terminated";
			}
		}

		return "You do not have a subscription with id " + id + "!";
	}
	private void notify(Event event){
		ArrayList<EventListenerInterface> delete = new ArrayList<EventListenerInterface>();
		ArrayList<EventListenerInterface> notify = new ArrayList<EventListenerInterface>();

		synchronized (subscriptions){

			

			for (Subscription s : subscriptions){
				Matcher matcher = s.getPattern().matcher(event.getType()); 
				if (matcher.find()){
					if (!notify.contains(s.getEventI())){
						notify.add(s.getEventI());
					}
				}

			}

		}

		for (EventListenerInterface e : notify){
			try{
				e.processEvent(event);
			}
			catch (RemoteException re){
				delete.add(e);
			}
		}
		synchronized (subscriptions){
			ArrayList<Subscription> deleteSubscriptions = new ArrayList<Subscription>();

			for (EventListenerInterface e : delete){
				for (Subscription s : subscriptions){
					if (s.getEventI().equals(e)){
						deleteSubscriptions.add(s);

					}
				}
			}
			subscriptions.removeAll(deleteSubscriptions);
			deleteSubscriptions.clear();
		}
		notify.clear();
		delete.clear();

	}

	public void processEvent(AuctionEvent event) throws RemoteException{

		if (event.getType().equals(AuctionEvent.started)){
			synchronized (auctionevents){
				auctionevents.add(event);
			}
		}
		else if (event.getType().equals(AuctionEvent.ended)){

			Date dateNow = new Date();
			long now = dateNow.getTime();
			long auctionTime = 0;
			incrementAuctionCounter();

			synchronized (auctionevents){

			int f = 0;
			boolean found = false;
			for (int i = 0; i< auctionevents.size(); i++){
				if (auctionevents.get(i).getAuctionID() == event.getAuctionID()){
					auctionTime = (event.getTimestamp() - auctionevents.get(i).getTimestamp())/1000;
					found = true;
					f = i;

					break;
				}

			}



			if (found){
				// should always be true
				auctionevents.remove(f);
			}
			}


			long avg = 0;
			if (auction_time_avg != -1){
				avg = auction_time_avg;
			}
			int ac = getAuctionCounter();
			avg = (avg*(ac-1) + auctionTime)/(ac);
			auction_time_avg = avg;

			try{
				StatisticsEvent se = new StatisticsEvent(StatisticsEvent.auctionAvg, now, auction_time_avg);
				notify(se);

			}
			catch (EventNotFoundException e){
				// should not happen
			}

			Date dateNowRatio = new Date();
			long nowRatio = dateNowRatio.getTime();
			double ratio = (double)getSuccessfulAuctionCounter()/(double)getAuctionCounter();
			try{
				StatisticsEvent se = new StatisticsEvent(StatisticsEvent.auctionRatio, nowRatio, ratio);
				notify(se);
			}
			catch (EventNotFoundException e){
				// should not happen
			}
		}
	}

	public void processEvent(UserEvent event) throws RemoteException{

		if (event.getType().equals(UserEvent.login)){
			synchronized (userevents){
				userevents.add(event);
			}
		}
		else if (event.getType().equals(UserEvent.logout) || event.getType().equals(UserEvent.disconnect)){
			Date dateNow = new Date();
			long now = dateNow.getTime();
			long sessionTime = 0;
			incrementSessionCounter();

			synchronized (userevents){
				int i;
				boolean found = false;
				for (i = 0; i< userevents.size(); i++){
					if (userevents.get(i).getUserName().equals(event.getUserName())){
						sessionTime = (event.getTimestamp() - userevents.get(i).getTimestamp())/1000;
						found = true;
						break;
					}
				}

				if (found){
					// should always be true
					userevents.remove(i);
				}
			}

			if (user_sessiontime_min == -1 || user_sessiontime_min > sessionTime){
				try{
					user_sessiontime_min = sessionTime;
					StatisticsEvent se = new StatisticsEvent(StatisticsEvent.userMin, now, sessionTime);
					notify(se);
				}
				catch (EventNotFoundException e){
					// should not happen
				}
			}
			if (user_sessiontime_max == -1 || user_sessiontime_max < sessionTime){
				try{
					user_sessiontime_max = sessionTime;
					StatisticsEvent se = new StatisticsEvent(StatisticsEvent.userMax, now, sessionTime);
					notify(se);
				}
				catch (EventNotFoundException e){
					// should not happen
				}
			}

			long avg = 0;
			if (user_sessiontime_avg != -1){
				avg = user_sessiontime_avg;
			}
			int sc =  getSessionCounter();
			avg = (avg*(sc-1) + sessionTime)/(sc);
			user_sessiontime_avg = avg;

			try{
				StatisticsEvent se = new StatisticsEvent(StatisticsEvent.userAvg, now, user_sessiontime_avg);
				notify(se);
			}
			catch (EventNotFoundException e){
				// should not happen
			}
		}
	}



	public void processEvent(BidEvent event) throws RemoteException{
		if (event.getType().equals(BidEvent.placed)){
			Date dateNow = new Date();
			long now = dateNow.getTime();
			incrementBidCounter();

			synchronized(bidevents){
				int i;
				boolean found = false;
				for (i = 0; i< bidevents.size(); i++){
					if (bidevents.get(i).getAuctionID() == event.getAuctionID()){
						found = true;
						break;
					}
				}
				if (found){
					bidevents.remove(i);
				}
				else{
					incrementSuccessfulAuctionCounter();
				}
				bidevents.add(event);
			}
			try{
				StatisticsEvent se = new StatisticsEvent(StatisticsEvent.bidMax, now, event.getPrice());
				notify(se);
			}
			catch (EventNotFoundException e){
				// should not happen
			}

			Date dateNowCounter = new Date();

			long sinceSystemStart = (dateNowCounter.getTime() - systemStart)/1000;

			try{
				StatisticsEvent se = new StatisticsEvent(StatisticsEvent.bidCount, now, (double)getBidCounter()/sinceSystemStart);
				notify(se);
			}
			catch (EventNotFoundException e){
				// should not happen
			}
		}
		else if (event.getType().equals(BidEvent.overbid)){
			// nothing to do
		}
		else if (event.getType().equals(BidEvent.won)){


			int i;
			boolean found = false;
			for (i = 0; i< bidevents.size(); i++){
				if (bidevents.get(i).getAuctionID() == event.getAuctionID()){
					found = true;
					break;
				}
			}

			if (found){
				bidevents.remove(i);
			}
		}
	}

	private synchronized void incrementBidCounter(){
		bidCounter++;
	}

	private synchronized int getBidCounter(){
		return bidCounter;
	}

	private synchronized void incrementSessionCounter(){
		sessionCounter++;
	}

	private synchronized int getSessionCounter(){
		return sessionCounter;
	}

	private synchronized void incrementAuctionCounter(){
		auctionCounter++;
	}

	private synchronized int getAuctionCounter(){
		return auctionCounter;
	}

	private synchronized void incrementSuccessfulAuctionCounter(){
		successfulAuctionCounter++;
	}

	private synchronized int getSuccessfulAuctionCounter(){
		return successfulAuctionCounter;
	}
}
