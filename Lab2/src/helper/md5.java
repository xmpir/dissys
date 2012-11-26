/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import java.math.BigInteger;
import java.util.Scanner;
import java.security.*;
/**
 *
 * @author Robert Rainer
 */
public class md5 {

    public static void main(String[] args) throws NoSuchAlgorithmException{
	
	Scanner sca = new Scanner(System.in);
	
	String linein = "";
	
	while(!linein.equals("end")){
	    linein = sca.nextLine();
	    
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.reset();
	    md.update(linein.getBytes());
	    
	    byte[] digest = md.digest();
	    BigInteger bigInt = new BigInteger(1,digest);
	    String hashtext = bigInt.toString(16);
	    // Now we need to zero pad it if you actually want the full 32 chars.
	    while(hashtext.length() < 32 ){
	      hashtext = "0"+hashtext;
	    }
	    System.out.println(hashtext);
	    
	}
	
	
	
    }
    
}
