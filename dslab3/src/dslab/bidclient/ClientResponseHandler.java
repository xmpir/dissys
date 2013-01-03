package dslab.bidclient;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientResponseHandler extends Thread{
	private BufferedReader in;
	private String host;
	private ClientUser user;

	ClientResponseHandler(BufferedReader in, String host, ClientUser user){
		this.in = in;
		this.host = host;
		this.user = user;
	}

	public void start(){
		try{
			String fromServer;
			if ((fromServer = in.readLine()) != null) {
				try{
					int lines = Integer.parseInt(fromServer);
					for (int i = 0; i < lines; i++){
						if ((fromServer = in.readLine()) != null) {
							if (fromServer.length() >= 8 && fromServer.substring(0, 6).equals("!login")){
								user.setName(fromServer.substring(7));
							}
							else if (fromServer.length() == 7 && fromServer.equals("!logout")){
								user.setName(null);
							}
							else{
								System.out.println(fromServer);
							}
						}
					}
				}
				catch (NumberFormatException e) {
					System.out.println("Number of lines must be an integer!");
				}
			}
			else System.err.println("Server is not responding.");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: " + host + ".");
		}
	} 
}
