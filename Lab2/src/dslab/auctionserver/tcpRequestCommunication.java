package dslab.auctionserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;

public class tcpRequestCommunication extends Thread{
	private User currentUser;
	private Lists lists;
	private BufferedReader in;
	private PrintWriter out;
	private InetAddress address;

	tcpRequestCommunication(Lists lists, BufferedReader in, PrintWriter out, User currentUser, InetAddress address){
		this.lists = lists;
		this.in = in;
		this.out = out;
		this.currentUser = currentUser;
		this.address = address;
	}

	public void run(){
		try{
			Protocol p = new Protocol(currentUser, lists);
			String inputLine;
			String outputWhole;
			while ((inputLine = in.readLine()) != null) {
				try{
					outputWhole = p.processInput(inputLine, address);
				}
				catch (UnknownParameterException upe){
					outputWhole=upe.getMessage();
				}
				String[] output = outputWhole.split(System.getProperty("line.separator"));
				out.println(output.length);
				for (int i=0; i<output.length; i++){
					out.println(output[i]);
				}
			}
		}
		catch (IOException e) {
			System.out.println("Exiting tcpRequestCommunication");
		}
	}
}
