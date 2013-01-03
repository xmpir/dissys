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

    
    private String billString;
    
    public Bill(String billString){
	this.billString = billString;
    }
    
    @Override
    public String toString(){
	return this.billString;
    }
    
    
}
