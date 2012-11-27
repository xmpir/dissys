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
    public PriceSteps getPriceSteps() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createPriceStep(double startPrice, double endPrice, double fixedPrice, double variablePricePercent) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deletePriceStep(double startPrice, double endPrice) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void billAuction(String user, long auctionID, double price) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Bill getBill(String user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    
}
