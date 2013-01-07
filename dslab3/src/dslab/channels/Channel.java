/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dslab.channels;

/**
 *
 * @author rr
 */
public interface Channel {
    
    public boolean isOpen();
    
    public void send(String message);
    
    public String receive();

    public void close();
    
}
