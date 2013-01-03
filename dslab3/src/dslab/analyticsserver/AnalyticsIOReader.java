package dslab.analyticsserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AnalyticsIOReader extends Thread{

	@Override
	public void run(){

		BufferedReader stdIn = new BufferedReader(
				new InputStreamReader(System.in));
		String fromUser;
		try {
			while ((fromUser = stdIn.readLine()) != null) {
			}
		}
		catch (IOException ex) {
			System.out.println("Problems with reading from the IO-Stream");	
		}
		try{
			stdIn.close();
			System.out.println("IO Stream closed");
		} catch (IOException ex) {
			System.out.println("Problems with closing the IO-Stream");
		}
	}
}
