package dslab.auctionserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ServerListener extends Thread {
	private ServerSocket serverSocket;
	private final ExecutorService pool;
	private boolean listening = true;

	public ServerListener(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		pool = Executors.newCachedThreadPool();
	}

	public void run() { // run the service
		try {
			while (listening) {
				pool.execute(new tcpRequest(serverSocket.accept()));
			}
			try {
				serverSocket.close();
			} catch (IOException e2) {
				System.err.println("Close failed.");
			}
		} catch (IOException e3) {
			System.out.println("Closing ServerListener");
			pool.shutdownNow();
		}
	}
}

