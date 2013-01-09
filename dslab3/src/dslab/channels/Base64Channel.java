/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.channels;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.util.encoders.Base64;

/**
 *
 * @author Robert Rainer
 */
public class Base64Channel extends ChannelDecorator{

    public Base64Channel(Channel decoratedChannel) {
	super(decoratedChannel);
    }

    @Override
    public String receive(){
	String message = super.receive();
	if(message!=null){
		return new String(Base64.decode(message));
	}
	return message;
    }
    
    @Override
    public void send(String message){
	    String b64encMessage = new String(Base64.encode(message.getBytes()));
	    super.send(b64encMessage);
    }
    
   
    
    
}
