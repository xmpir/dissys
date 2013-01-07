package dslab.bidclient;

import dslab.channels.Channel;
import dslab.channels.ChannelDecorator;
import dslab.channels.TcpChannel;
import java.io.*;
import java.net.*;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private static Socket socket = null;
    private static PrintWriter out = null;
    private static BufferedReader in = null;

    private static String host;
    private static String tcpPortHost;
    private static String tcpPortClient;
    private static String pathToServerKey;
    private static String clientKeyDir;
    private static int tcpPortHo;
    private static int tcpPortCli;
    private static Channel channel;
    
    
    public static void main(String[] args){
	//add securityprovider - with this finally it works!
	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

	if (args.length == 5) {
	    host = args[0];
	    tcpPortHost = args[1];
	    tcpPortClient = args[2];
	    pathToServerKey = args[3];
	    Data.getInstance().setPathToServerKey(pathToServerKey);
	    clientKeyDir = args[4];
	    Data.getInstance().setKeydirpath(clientKeyDir);
	    Data.getInstance().initKeys();
	    
	    try {
		tcpPortHo = Integer.parseInt(tcpPortHost);
		tcpPortCli = Integer.parseInt(tcpPortClient);
		
		try {
		    socket = new Socket(host, tcpPortHo);
		    channel = new ChannelDecorator(new TcpChannel(socket));
		    
		    ClientWriter clwr = new ClientWriter(channel);
		    clwr.start();
		    
		    ClientResponseHandler crh = new ClientResponseHandler(channel);
		    crh.start();
		    
		} catch (UnknownHostException e) {
		    System.err.println("Don't know about host: " + host + ".");
		} catch (IOException e) {
		    System.err.println("Couldn't get I/O for the connection to: " + host + ".");
		}
	    } catch (NumberFormatException e2) {
		System.out.println("tcpPorts must be integers!");
	    }
	} else {
	    System.out.println("5 arguments required: hostname hostTcpPort clientTcpPort server-key-pub client-key-dir");
	}
    }
    
    
    public static void shutdown(){
	try {
	    socket.close();
	} catch (IOException ex) {
	    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
	}
        channel.close();
    }
    
}
