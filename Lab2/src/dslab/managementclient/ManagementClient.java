package dslab.managementclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import dslab.analyticsserver.AnalyticsCallback;
import dslab.analyticsserver.AnalyticsCallbackInterface;
import dslab.analyticsserver.EventListenerInterface;
import dslab.billingserver.BillingServerInterface;
import dslab.billingserver.BillingServerSecureInterface;
import java.rmi.NotBoundException;


public class ManagementClient {
	public static void main(String args[]) {

		if (args.length == 3){

			String registryPort="";
			String registryHost="";
			int port = 0;
			String in = "";
			BufferedReader stdIn = new BufferedReader(
					new InputStreamReader(System.in));
			java.io.InputStream is = ClassLoader.getSystemResourceAsStream("registry.properties");
			if (is != null) {
				java.util.Properties props = new java.util.Properties();
				try {
					props.load(is);
					registryHost = props.getProperty("registry.host");
					registryPort = props.getProperty("registry.port");

					try{
						port = Integer.parseInt(registryPort);
					}
					catch (NumberFormatException nfe){
						nfe.printStackTrace();
					}
					//Data.getInstance().initUsers();
					try {
						Registry registry = LocateRegistry.getRegistry(registryHost, port);
						BillingServerInterface billingServer = (BillingServerInterface) registry.lookup(args[2]);
						BillingServerSecureInterface secure = null;
						String user = null;
						EventListener listener = null;;

						try {
							listener = new EventListener();
						} catch (RemoteException e) {
							System.err.println(e.getCause());
						}
						AnalyticsCallbackInterface callback = (AnalyticsCallbackInterface) registry.lookup(args[1]);

						while ((in = stdIn.readLine()) != null) {
							String[] input = in.split(" ");
							if (input.length > 0){
								if (input[0].equals("!login")){
									if (input.length == 3){
										try{
											secure = billingServer.login(input[1], input[2]);
											user = input[1];
											System.out.println(user + " successfully logged in");
										}
										catch (RemoteException e) {
											System.out.println("BillingServer exception: "+e.getMessage());

										}
									}
									else System.out.println("Wrong command: !login requires exactly two additional arguments! Usage: !login name password");
								}
								else if (input[0].equals("!logout")){
									if (input.length == 1){
										if (user != null){
											secure = null;
											System.out.println(user + " successfully logged out");
											user = null;
										}
										else System.out.println("You have to log in first!");
									}
									else System.out.println("Wrong command: !logout requires no additional argument! Usage: !logout");
								}
								else if (input[0].equals("!steps")){
									if (input.length == 1){
										if (secure != null){
											System.out.println(secure.getPriceSteps().getRepresentation());
										}
										else System.out.println("ERROR: You are currently not logged in.");
									}
									else System.out.println("Wrong command: !steps requires no additional argument! Usage: !steps");
								}

								else if (input[0].equals("!addStep")){
									if (input.length == 5){
										if (secure != null){
											double startPrice = 0;
											double endPrice = 0;
											double fixedPrice = 0;
											double variablePricePercent = 0;

											try{
												startPrice = Double.parseDouble(input[1]);
												endPrice = Double.parseDouble(input[2]);
												fixedPrice = Double.parseDouble(input[3]);
												variablePricePercent = Double.parseDouble(input[4]);
												try{
													secure.createPriceStep(startPrice, endPrice, fixedPrice, variablePricePercent);
													System.out.println("Step [" + startPrice + " " + endPrice + "] successfully added");
												}
												catch (RemoteException e) {
													System.out.println("BillingServer exception: "+e.getMessage());
												}
											}
											catch (NumberFormatException nfe){
												System.out.println("ERROR: All arguments of !addStep are double.");
											}
										}
										else System.out.println("ERROR: You are currently not logged in.");
									}
									else System.out.println("Wrong command: !addStep requires exactly four additional arguments! Usage: !addStep startPrice endPrice fixedPrice variablePricePercent");
								}

								else if (input[0].equals("!removeStep")){
									if (input.length == 3){
										if (secure != null){
											double startPrice = 0;
											double endPrice = 0;

											try{
												startPrice = Integer.parseInt(input[1]);
												endPrice = Integer.parseInt(input[2]);
												try{
													secure.deletePriceStep(startPrice, endPrice);
													System.out.println("Price step [" + startPrice + " " + endPrice + "] successfully removed");
												}
												catch (RemoteException e){
													System.out.println("BillingServer exception: "+e.getMessage());
												}
											}
											catch (NumberFormatException nfe){
												System.out.println("ERROR: All arguments of !removeStep are double.");
											}
										}
										else System.out.println("ERROR: You are currently not logged in.");
									}
									else System.out.println("Wrong command: !removeStep requires exactly two additional arguments! Usage: !removeStep startPrice endPrice");
								}

								else if (input[0].equals("!bill")){
									if (input.length == 2){
										if (secure != null){
											try{
												System.out.println(secure.getBill(input[1]).toString());
											}
											catch (RemoteException e){
												System.out.println(e.getMessage());
											}

										}
									}
									else System.out.println("Wrong command: !removeStep requires exactly two additional arguments! Usage: !removeStep startPrice endPrice");
								} 

								else if (input[0].equals("!exit")){
									if (input.length == 1){
										break;
									}
									else System.out.println("Wrong command: !exit requires no additional arguments! Usage: !exit");
								}
								else if (input[0].equals("!subscribe")){
									
									if (input.length == 2){
										String[] regEx = in.substring(10).split("'");
										
										System.out.println(callback.subscribe(listener, regEx[1]));
									}
									else System.out.println("Wrong command: !subscribe requires exactly one additional argument! Usage: !subscribe filterRegex");

								}
								else if (input[0].equals("!unsubscribe")){
									if (input.length == 2){
										int id = 0;
										try{
											id = Integer.parseInt(input[1]);
										}
										catch (NumberFormatException nfe){
											nfe.printStackTrace();
										}
										System.out.println(callback.unsubscribe(id));
									}
									else System.out.println("Wrong command: !unsubscribe requires exactly one additional argument! Usage: !unsubscribe subscriptionID");

								}
								else if (input[0].equals("!auto")){
									if (input.length == 1){
										listener.setAuto(true);
										
									}
									else System.out.println("Wrong command: !auto requires no additional argument! Usage: !auto");

								}	
								else if (input[0].equals("!hide")){
									if (input.length == 1){
										listener.setAuto(false);
										System.out.println();
									}
									else System.out.println("Wrong command: !auto requires no additional argument! Usage: !auto");

								}	
								else if (input[0].equals("!print")){
									if (input.length == 1){
										listener.printBuffer();
										System.out.println();
										
									}
									else System.out.println("Wrong command: !auto requires no additional argument! Usage: !auto");

								}	
								
								else System.out.println("Command not recognized! Please use either !login, !logout, !addStep, !removeStep or !bill, !subscribe, !unsubscribe, !auto, !hide or !print !");
							}
							else System.out.println("Please enter command!");
						}
						stdIn.close();
					} catch (NotBoundException e){
						System.out.println("BillingServer not bound");
					} catch (IOException e) {
						System.out.println("IOException");
						//e.getMessage();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				System.err.println("Properties file not found!");
			}
		}
	}
}
