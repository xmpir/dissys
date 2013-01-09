/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.channels;

/**
 *
 * @author Robert Rainer
 */
public class ChannelDecorator implements Channel{

    private Channel decoratedChannel;
    
    public ChannelDecorator(Channel decoratedChannel){
	this.decoratedChannel=decoratedChannel;
    }
    
    
    
    @Override
    public void send(String message) {
	decoratedChannel.send(message);
    }

    @Override
    public String receive() {
	return decoratedChannel.receive();
    }

    @Override
    public void close() {
	decoratedChannel.close();
    }

    @Override
    public boolean isOpen() {
	return decoratedChannel.isOpen();
    }


    
    
}
