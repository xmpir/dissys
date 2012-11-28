/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.billingserver;

import java.io.Serializable;

/**
 * Entity-Klasse für eine Auction
 * @author Robert Rainer
 */
public class Auction implements Serializable{
    private String user;
    private long id;
    private double price;

    public Auction(String user, long id, double price) {
        this.user = user;
        this.id = id;
        this.price = price;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    
    /**
     * 
     * @return a String representing this auction on a Bill
     */
    public String getLineForBill(){
        
        String line = "  "  + this.id + "          "  + this.getPrice() + "           " + 
                PriceSteps.getInstance().getFixed(this.price) + "          " + 
                PriceSteps.getInstance().getVariable(this.price)  + "          " 
                + (PriceSteps.getInstance().getFixed(this.price)+PriceSteps.getInstance().getVariable(price) );
        return line;
    }
    
}
