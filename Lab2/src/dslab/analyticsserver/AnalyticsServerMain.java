package dslab.analyticsserver;

import java.io.IOException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class AnalyticsServerMain {

    private static AnalyticsServer as;

    public static void main(String[] args) throws IOException {


	ioReader ioReader = new ioReader();
	ioReader.start();
	as = new AnalyticsServer();
	as.start(args);
    }

    public static void shutdown() {
	as.shutdown();
	System.out.println("RMI should be shut down.");
    }
}
