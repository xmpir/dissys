/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.billingserver;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Eine Singleton Klasse für die Price-Steps.
 * ein price-step wird als double[4] repräsentiert. dabei gilt folgende zuweisung:
 * step[0] ist der start price
 * step[1] ist der end price
 * step[2] ist der fixed price
 * step[3] ist der prozentsatz des variable price
 * @author Robert Rainer
 */
public class PriceSteps implements Serializable{
   
    private static PriceSteps instance;

    private ArrayList<double[]> steps;
    
    /**
     * Default-Konstruktor, der nicht außerhalb dieser Klasse
     * aufgerufen werden kann
     */
    private PriceSteps() {
        steps= new ArrayList<double[]>();
    }
 
    /**
     * Statische Methode, liefert die einzige Instanz dieser
     * Klasse zurück
     */
    public static PriceSteps getInstance() {
        if (instance == null) {
            instance = new PriceSteps();
        }
        return instance;
    }
    
    
    public void createPriceStep(double startPrice, double endPrice, double fixedPrice, double variablePricePercent) throws InvalidArgumentsException{

	if(startPrice < 0 || endPrice<0 || fixedPrice<0 || variablePricePercent<0 || (endPrice!=0 && startPrice>endPrice)){
	//check for negative arguments
	    throw new InvalidArgumentsException("negative arguments are not allowed here");
	}
	
	if(endPrice==0){
	// zero is for infinity
	    endPrice = Double.POSITIVE_INFINITY;
	}
	
	if(endPrice == startPrice){
	    //TODO decide what happens if interval is only one number
	}
	
	//create a double[] representing the new price step to be added
	double[] step = {startPrice, endPrice, fixedPrice, variablePricePercent};
	
	if(steps.isEmpty()){
	//no steps yet -> add this as the first step 
	    this.steps.add(step);
	    return;
	}
	
	
        for(int i=0; i<this.steps.size(); ++i){
           double[] current = steps.get(i);
           if(endPrice<=current[0]){
               //the interval ends smaller(=) than the current interval starts
               //therefore insert it before the current interval
	       //check for the endprice of the interval before
               if(i>0){
                   double[] before = steps.get(i-1);
		   if(before[1]<=startPrice){
		       //this is ok
		       this.steps.add(i, step);
		       return;
		   }
		   else{
		       throw new InvalidArgumentsException("interval overlaps with an existing interval");
		   }
               }
               else{
               //the first interval starts higher than this one ends
		   steps.add(0, step);
		   return;
	       }
           }
        }
	
	
	//no existing pricestep has a lower startprice than the new one's endPrice
	//try to make the new one the last pricestep
	
	double[] last = steps.get(steps.size()-1);
	
	if(last[1]<=startPrice){
	    steps.add(step);
	} else{
	    throw new InvalidArgumentsException("interval overlaps with the last interval");
	}
	
	
    }
    
    
    public double getFixed(double price){
    //TODO do some calculations
	double[] current;
	for(int i=0; i<this.steps.size(); ++i){
           current = steps.get(i);
           if(price>=current[0] && price<=current[1]){
	       return current[2];
	   }
        }
        return 0;
    }
    
    public double getVariable(double price){
    //TODO do some calculations
        double[] current;
	//System.out.println("looking up variable price for: "+price);
	for(int i=0; i<this.steps.size(); ++i){
           current = steps.get(i);
           if(price>=current[0] && price<=current[1]){
	       return current[3]*price/100.0;
	   }
        }
        return 0;
    }
    
    public String getRepresentation(){
        
	String answer = "    pricespan      fixedPrice     variablePercentage  +\n";
	for(int i=0; i<this.steps.size(); ++i){
           double[] current = steps.get(i);
           
	   if(current[1]==Double.POSITIVE_INFINITY){
	    answer += 
		   "   >" + (double)((int)(current[0]*100))/100.0 + 
		   "         " +
		   "         " + (double)((int)(current[2]*100))/100.0 + 
		   "         " + (double)((int)(current[3]*100))/100.0 + 
		   " % \n";
	   } else{
	   answer += 
		   "   " + (double)((int)(current[0]*100))/100.0 + 
		   "-" + (double)((int)(current[1]*100))/100.0 + 
		   "         " + (double)((int)(current[2]*100))/100.0 + 
		   "         " + (double)((int)(current[3]*100))/100.0 + 
		   " % \n";
	   }
        }
        return answer;
    }

    void deletePriceStep(double startPrice, double endPrice) throws RemoteException {
	
	if(endPrice==0){
	    endPrice=Double.POSITIVE_INFINITY;
	}
	
	for(int i=0; i<this.steps.size(); ++i){
           double[] current = steps.get(i);
           if(current[0]==startPrice && current[1]==endPrice){
	       this.steps.remove(i);
	       return;
	   }
	   
        }
	throw new RemoteException("no such interval");
    }
    
    
}
