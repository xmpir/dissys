package dslab.bidclient;

import dslab.channels.Channel;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientResponseHandler extends Thread {

    private Channel channel;

    ClientResponseHandler(Channel channel) {
	this.channel = channel;
    }

    public void run() {
	String fromServer;
	while (channel.isOpen()) {
	    fromServer = channel.receive();
	    if(fromServer!=null){
	    //int lines = Integer.parseInt(fromServer);
	    System.out.println(fromServer);
	    }
	    try {
		Thread.sleep(40);
	    } catch (InterruptedException ex) {
		break;
	    }
	}

    }
}

