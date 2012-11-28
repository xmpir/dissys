package dslab.analyticsserver;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import dslab.billingserver.ioReader;

public class AnalyticsServerMain {

	private static AnalyticsServer as;

	public static void main(String[] args) throws IOException {

		
		ioReader ioReader = new ioReader();
	    ioReader.start();
		as = new AnalyticsServer();
		as.start(args);
	}

	public static void shutdown() throws RemoteException, AccessException, NotBoundException{
		ioReader.interrupted();
		as.shutdown();
		System.out.println("RMI should be shut down.");
		ioReader.currentThread().interrupt();
		System.out.println("IO should be shut down.");
	}

}

