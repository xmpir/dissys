package dslab.auctionserver;

import java.net.InetAddress;
import java.util.ArrayList;

public class User{
	private String username;
	private boolean active = false;
	private int udpPort;
	private InetAddress address;
	private ArrayList<String> messages = new ArrayList<String>();

	User(String username){
		this.username = username;
	}

	public synchronized int getUdpPort() {
		return udpPort;
	}

	public synchronized void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	public synchronized boolean isActive() {
		return active;
	}

	public synchronized void setActive(boolean active) {
		this.active = active;
	}

	public synchronized String getUsername(){
		return username;
	}

	public synchronized InetAddress getAddress() {
		return address;
	}

	public synchronized void setAddress(InetAddress address) {
		this.address = address;
	}

	public synchronized void logout(){
		setActive(false);
	}

	@Override
	public synchronized boolean equals(Object user){
		if (user == null){
			return false;
		}
		if (getClass() != user.getClass()){
			return false;
		}
		return getUsername().equals(((User)user).getUsername());
	}

	public synchronized int compareTo(User user){
		return getUsername().compareTo(((User)user).getUsername());

	}

	public synchronized String toString(){
		return username;
	}

	public synchronized void getMessages(){
		for (String s : messages){
			ServerNotifier sn = new ServerNotifier(getAddress(), getUdpPort());
			sn.send(s);
		}
	}

	public synchronized void addMessage(String message){
		messages.add(message);
	}
}
