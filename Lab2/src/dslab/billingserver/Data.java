/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.billingserver;

import java.io.IOException;
import java.util.ArrayList;

/**
 * "Datenbank" des Billing-Servers
 * @author Robert Rainer
 */
public class Data {

    private static Data instance = null;
    private ArrayList<Auction> auctions;
    
    /**
     * Default-Konstruktor, der nicht außerhalb dieser Klasse
     * aufgerufen werden kann
     */
    private Data() {
        auctions = new ArrayList<Auction>();
    }
 
    /**
     * Statische Methode, liefert die einzige Instanz dieser
     * Klasse zurück
     */
    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    public ArrayList<Auction> getAuctions() {
        return auctions;
    }
    
    public void addAuction(String user, long id, double price){
        synchronized(auctions){
	    instance.auctions.add(new Auction(user, id, price));
	}
    }

    public ArrayList<Auction> getAuctionsByUser(String user){
        
        ArrayList<Auction> answer = new ArrayList<Auction>();
        synchronized(auctions){
	    for(Auction a : auctions){
		if(a.getUser().equals(user)){
		    answer.add(a);
		}
	    }
	}
        return answer;
    }

    
    
    
    
}
