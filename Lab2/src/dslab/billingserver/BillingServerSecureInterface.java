package dslab.billingserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Robert Rainer
 */
public interface BillingServerSecureInterface extends Remote{

    /**
     * 
     * @return This method returns the current configuration of price steps. 
     * Think of a suitable way to represent the list of price step 
     * configurations inside your PriceSteps class.
     */
    public PriceSteps getPriceSteps()  throws RemoteException;
    
    /**
     * This method allows to create a price step for a given price interval. 
     * Throw a RemoteException (or a subclass thereof) if any of the specified 
     * values is negative. Also throw a RemoteException (or a subclass thereof) 
     * if the provided price interval collides (overlaps) with an existing 
     * price step (in this case the user would have to delete the other price step first). 
     * To represent an infinite value for the endPrice 
     * parameter (e.g., in the example price step "> 1000") 
     * you can use the value 0.
     * @param startPrice
     * @param endPrice
     * @param fixedPrice
     * @param variablePricePercent 
     */
    public void createPriceStep(double startPrice, double endPrice, double fixedPrice, double variablePricePercent) throws RemoteException;
    
    /**
     * This method allows to delete a price step for the pricing curve. 
     * Throws a RemoteException (or a subclass thereof) if the 
     * specified interval does not match an existing price step interval.
     * @param startPrice
     * @param endPrice 
     */
    public void deletePriceStep(double startPrice, double endPrice) throws RemoteException;
    
    /**
     * This method is called by the auction server as soon as an auction has ended. 
     * The billing server stores the auction result (data do not have to be persisted, 
     * storing in memory is sufficient) and later uses this information to calculate 
     * the bill for a user. (updated 01.11.2012). Note: The auction server should 
     * use the same login mechanism as the management clients to talk to the billing server. 
     * You can add pre-defined user credentials (for instance "auctionClientUser = f23c5f9779a3804d586f4e73178e4ef0") to your properties file.
     * @param user
     * @param auctionID
     * @param price 
     */
    public void billAuction(String user, long auctionID, double price)  throws RemoteException;
    
    /**
    * This method calculates and returns the bill for a given user, 
    * based on the price steps stored within the billing server. 
    * Implement a class Bill which encapsulates all billing lines 
    * of a given user (also see sample output of management client further below). 
    * You need not implement any payment mechanism - the bill should simply 
    * show the total history of all auctions created by the user.     
    * @param user
    * @return 
     */
    public Bill getBill(String user)  throws RemoteException;
}
