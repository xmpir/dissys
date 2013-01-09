package dslab.auctionserver;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;

public class ServerNotifier{
	private InetAddress address;
	private int udpPort;
	private DatagramSocket socket = null;

	public ServerNotifier(InetAddress address, int udpPort){
		this.address = address;
		this.udpPort = udpPort;
	}

	public void send(String message) {
		try {
			socket = new DatagramSocket();
			byte[] buf = new byte[256];
			buf = message.getBytes(Charset.defaultCharset());
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, udpPort);
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		socket.close();
	}
}