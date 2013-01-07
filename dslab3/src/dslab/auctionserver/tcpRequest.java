package dslab.auctionserver;

import dslab.channels.Channel;
import dslab.channels.ChannelDecorator;
import dslab.channels.TcpChannel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class tcpRequest implements Runnable {
	private final Socket socket;
	private Lists lists;
	private User currentUser;
	private BufferedReader in;
	private PrintWriter out;
	private tcpRequestCommunication t;
	private Channel channel;

	tcpRequest(Socket socket, Lists lists) throws IOException {
		this.socket = socket;
		this.lists = lists;
		this.channel = new ChannelDecorator(new TcpChannel(socket));
	}
	@Override
	public void run() {
		try{
			t = new tcpRequestCommunication(lists, channel,	currentUser);
			t.start();
			t.join();
			t.interrupt();
			System.out.println("Exiting tcpRequest");
			channel.close();
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			t.interrupt();
			System.out.println("Exiting tcpRequest");
			channel.close();
		}
	}
	public void interrupt(){
		channel.close();
		
	}
}
