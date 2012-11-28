/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.billingserver;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Robert Rainer
 */
public class Bill implements Serializable{

    
    private ArrayList<Auction> auctions;
    
    public Bill(ArrayList<Auction> auctions){
	this.auctions = auctions;
    }
    
    @Override
    public String toString(){
	String answer = "";
	for(Auction a:auctions){
	    answer+=a.getLineForBill() + "\n";
	}
	return answer;
    }
    
    
}
