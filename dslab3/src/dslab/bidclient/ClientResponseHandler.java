package dslab.bidclient;

import dslab.channels.Channel;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientResponseHandler extends Thread {

    ClientResponseHandler() {
    }

    public void run() {
	String fromServer;
	while (Data.getInstance().channel.isOpen()) {
	    fromServer = Data.getInstance().channel.receive();
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

