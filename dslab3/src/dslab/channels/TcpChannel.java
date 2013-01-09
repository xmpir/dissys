/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dslab.channels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert Rainer
 */
public class TcpChannel implements Channel{

    private BufferedReader in;
    private Socket socket;
    private final PrintWriter out;
    private boolean open = true;
    private boolean reset = false;
    
    public TcpChannel(Socket socket) throws IOException{
	open = true;
	this.socket = socket;
	this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	this.out = new PrintWriter(socket.getOutputStream(), true);
    }
    
    @Override
    public void send(String message) {
	out.println(message);
	out.flush();
    }

   
    
    
    @Override
    public String receive() {
	try {
	    if(in.ready()){
		    return in.readLine();
	    }
	} catch (IOException ex) {
	    return null;
	}
	return null;
    }

    @Override
    public void close() {
	open = false;
	try {
	    this.in.close();
	} catch (IOException ex) {
	    System.out.println("problems shutting down...");
	}
	this.out.close();
    }

    @Override
    public boolean isOpen() {
	return this.open;
    }

    @Override
    public void reset() {
	this.reset=true;
    }

    

    
    
}
