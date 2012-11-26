package dslab.auctionserver;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.io.*;
import java.net.ServerSocket;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Server {
    
    
	public static void main(String[] args) {
		int tcpPort = 0;
		Lists lists = new Lists();
		BufferedReader stdIn = new BufferedReader(
				new InputStreamReader(System.in));
		ServerSocket serverSocket;
		ScheduledExecutorService scheduler;
		Updater updater;

		if (args.length == 1) {
			try{
				tcpPort = Integer.parseInt(args[0]);
				serverSocket = new ServerSocket(tcpPort);
				ServerListener s = new ServerListener(serverSocket, lists);
				s.start();
				scheduler = Executors.newScheduledThreadPool(2);
				updater = new Updater(lists);
				scheduler.scheduleAtFixedRate(updater, 1000, 1000, MILLISECONDS);				
				if ((stdIn.readLine()) != null) {
					s.interrupt();
					lists.logoutUsers();
					serverSocket.close();
					stdIn.close();
					scheduler.shutdown();
				}
			} catch (NumberFormatException e) {
				System.err.println("tcpPort must be an integer!");
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		else System.err.println("Please specify tcpPort!");
	}
}
