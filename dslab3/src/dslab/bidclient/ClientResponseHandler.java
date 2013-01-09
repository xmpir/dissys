package dslab.bidclient;

import dslab.channels.Channel;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientResponseHandler extends Thread {

    public ClientResponseHandler() {
    }

    @Override
    public void run() {
	String fromServer;
	while (Data.getInstance().channel.isOpen()) {
	    fromServer = Data.getInstance().channel.receive();
	    if (fromServer != null) {
		if (fromServer.equals("logging out CODE")) {
		    System.out.println("logoutCode received");
		    Data.getInstance().channel.reset();
		    System.out.println("Channel Reset");
		    continue;
		}
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
