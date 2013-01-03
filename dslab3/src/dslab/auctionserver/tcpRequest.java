package dslab.auctionserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class tcpRequest implements Runnable {
	private final Socket socket;
	private Lists lists;
	private User currentUser;
	private BufferedReader in;
	private PrintWriter out;
	tcpRequestCommunication t;

	tcpRequest(Socket socket, Lists lists) {
		this.socket = socket;
		this.lists = lists;
	}

	@Override
	public void run() {
		try{
			in = new BufferedReader(
					new InputStreamReader(
							socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			t = new tcpRequestCommunication(lists, in, out, currentUser, socket.getInetAddress());
			t.start();

			t.join();
			t.interrupt();
			System.out.println("Exiting tcpRequest");
			out.close();
			in.close();
			socket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			t.interrupt();
			System.out.println("Exiting tcpRequest");
			out.close();
			try {
				in.close();
			socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void interrupt(){
		try {
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
