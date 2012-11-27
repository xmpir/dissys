/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.billingserver;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 *
 * @author Robert Rainer
 */
public class BillingServerSecure implements BillingServerSecureInterface, Serializable{

    @Override
    public PriceSteps getPriceSteps() throws RemoteException{
        return PriceSteps.getInstance();
    }

    @Override
    public void createPriceStep(double startPrice, double endPrice, double fixedPrice, double variablePricePercent) throws RemoteException {
        PriceSteps.getInstance().createPriceStep(startPrice, endPrice, fixedPrice, variablePricePercent);
    }

    @Override
    public void deletePriceStep(double startPrice, double endPrice) throws RemoteException {
        PriceSteps.getInstance().deletePriceStep();
    }

    @Override
    public void billAuction(String user, long auctionID, double price)  throws RemoteException{
	System.out.println("getting in a bill");
	Data.getInstance().addAuction(user, auctionID, price);
    }

    @Override
    public Bill getBill(String user)  throws RemoteException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    
}
