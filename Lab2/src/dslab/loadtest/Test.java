/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.loadtest;

/**
 *
 * @author Robert Rainer
 */
public class Test {

    private static String host;
    private static int port;
    private static String analyticsName;
    private static int auctionDuration;
    private static int auctionsPerMin;
    private static int bidsPerMin;
    private static int clients;
    private static int updateIntervalSec;
    
    
    public static void main(String args[]){
	
	if(args.length!=3){
	    System.out.print("3 arguments required!");
	    return;
	}
	
	host = args[0];
	port = Integer.parseInt(args[1]);
	analyticsName = args[2];
	
	
	
    }
    
    
    
}
