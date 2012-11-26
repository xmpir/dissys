/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.billingserver;

import java.rmi.RemoteException;

/**
 *
 * @author Robert Rainer
 */
class InvalidArgumentsException extends RemoteException {

    InvalidArgumentsException(String message) {
	super(message);
    }

}
