package dslab.bidclient;

import java.io.*;
import java.net.*;

public class Client {
	public static void main(String[] args){
		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		String host;
		int tcpPort;
		int udpPort;
		ClientUser user = new ClientUser();

		if (args.length == 3){
			host = args[0];
			try {
				tcpPort = Integer.parseInt(args[1]);
				try {
					udpPort = Integer.parseInt(args[2]);
					socket = new Socket(host, tcpPort);
					out = new PrintWriter(socket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
					BufferedReader stdIn = new BufferedReader(
							new InputStreamReader(System.in));
					String fromUser;
					DatagramSocket datagramSocket = new DatagramSocket(udpPort);
					ClientListener c = new ClientListener(datagramSocket, user);
					c.start();
					ClientResponseHandler crh = new ClientResponseHandler(in, host, user);

					while ((fromUser = stdIn.readLine()) != null) {
						if (fromUser.length() > 7 && fromUser.substring(0, 6).equals("!login")){
							fromUser = fromUser + " " + udpPort;
						}
						out.println(fromUser);
						crh.start();
						if (fromUser.equals("!end")){
							break;
						}
					}
					out.close();
					in.close();
					stdIn.close();
					socket.close();
					datagramSocket.close();
				} catch (NumberFormatException e) {
					System.out.println("udpPort must be an integer!");
				}
				catch (UnknownHostException e) {
					System.err.println("Don't know about host: " + host + ".");
				} catch (IOException e) {
					System.err.println("Couldn't get I/O for the connection to: " + host + ".");
				}
			} catch (NumberFormatException e2) {
				System.out.println("tcpPort must be an integer!");
			}
		}
		else  System.out.println("Three arguments required! Usage: java Client host tcpPort udpPort");
	}
}

