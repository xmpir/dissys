/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.helper;

import dslab.bidclient.Data;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.MGF1ParameterSpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.bouncycastle.util.encoders.Base64;

/**
 *
 * @author Robert Rainer
 */
public class CryptoTester {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
	
	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	System.out.println(System.getProperty("file.encoding"));
	System.setProperty("file.encoding", "ISO-8859-1");
	System.out.println(System.getProperty("file.encoding"));
	
	PublicKey publicKeyServer = null;
	    //first get the private key:
	PEMReader publIn = null;
	try {
	    publIn = new PEMReader(new FileReader("keys/auction-server.pub.pem"));
	} catch (FileNotFoundException ex) {
	    ex.printStackTrace();
	}
	try {
	    publicKeyServer = (PublicKey) publIn.readObject();
	} catch (IOException ex) {
	    Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
	}
	try {
	    publIn.close();
	} catch (IOException ex) {
	    Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
	}
	
	PEMReader privIn = null;
	try {
	    privIn = new PEMReader(new FileReader("keys/auction-server.pem"), new PasswordFinder() {
		@Override
		public char[] getPassword() {
		char[] password = new char[5];
		for(int i=2; i<7; ++i){
		    password[i-2] = Integer.toString(i).charAt(0);
		}
		return password;
		}
	    });
	} catch (FileNotFoundException ex) {
	   System.out.println("server cannot find its private key file...");
	}
	KeyPair keyPair = null; 
	try {
	    keyPair = (KeyPair) privIn.readObject();
	} catch (IOException ex) {
	    System.out.println(" Wrong password for the serverKey WTF?");
	    return;
	}
	PrivateKey privateKeyServer = keyPair.getPrivate();
	System.out.println("Private Key set!");
	
	
	
	SecureRandom secureRandom = new SecureRandom(); 
	final byte[] number = new byte[32]; 
	secureRandom.nextBytes(number);
	
	byte[] encodedNumber = Base64.encode(number);
	
	String firstMessage = "!login alice 10401 "+(new String(encodedNumber, Charset.defaultCharset()));
	
	assert firstMessage.matches("!login [a-zA-Z0-9_\\-]+ [0-9]+ ["+"a-zA-Z0-9/+"+"]{43}=") : "1st message";
	
	byte[] message = firstMessage.getBytes(Charset.defaultCharset());
	
	Cipher crypt = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding"); 
	crypt.init(Cipher.ENCRYPT_MODE, publicKeyServer);
	byte[] encmessage = crypt.doFinal(message);
	byte[] enc64mes1 = Base64.encode(encmessage);
	String encryptedFirstMessage1 = new String(enc64mes1, Charset.defaultCharset());

	byte[] enc64mes = Base64.encode(encryptedFirstMessage1.getBytes());
	System.out.println(enc64mes.length);

	String encryptedFirstMessage = new String(enc64mes, Charset.defaultCharset());
	
	System.out.println(encryptedFirstMessage);
		
	byte[] dec64mess = Base64.decode(Base64.decode(encryptedFirstMessage));
	
	
	System.out.println(dec64mess.length);
	
	crypt = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding");
	
	crypt.init(Cipher.DECRYPT_MODE, privateKeyServer);
	dec64mess = crypt.doFinal(dec64mess);
	
	firstMessage = new String(dec64mess, Charset.defaultCharset());

	System.out.println(firstMessage);
	
	
	byte[] once = Base64.encode("test123kjasd".getBytes());
	byte[] twice= Base64.encode(Base64.encode("test123kjasd".getBytes()));
	
	System.out.println(once==twice);
	
	
    }
    
}
