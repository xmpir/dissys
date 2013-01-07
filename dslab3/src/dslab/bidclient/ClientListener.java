package dslab.bidclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientListener extends Thread{
	private String user;

	ClientListener(String user){
		this.user = user;
	}

	public void run(){
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		
			while (true){
				String received = new String(packet.getData(), 0, packet.getLength());
				if (received.substring(0, 8).equals("!new-bid")){
					System.out.println("You have been overbid on '" + received.substring(9) + "'");
				}
				else if (received.substring(0, 14).equals("!auction-ended")){
					String[] input = received.split(" ");
					String out = "The auction '";
					for (int i = 3; i < input.length-1; i++){
						out += input[i] + " ";
					}
					out += input[input.length-1];
					out += "' has ended. ";
					if (user != null && user.equals(input[1])){
						out += "You";
					}
					else{
						out += input[1];
					}
					out += " won with ";
					out += input[2];
					if (user != null && user.equals(input[1])){
						out += "!";
					}
					else{
						out += ".";
					}
					System.out.println(out);
				}
			}
		
		
	}
}
