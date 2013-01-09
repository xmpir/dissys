package dslab.auctionserver;

import dslab.channels.Channel;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.security.PublicKey;
import java.util.ArrayList;
import org.bouncycastle.openssl.PEMReader;

public class User {

    private String username;
    private boolean active = false;
    private int udpPort;
    private InetAddress address;
    private ArrayList<String> messages = new ArrayList<String>();
    private PublicKey publicKey = null;
    private String tcpPort;
    
    public User(String username) {
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

    public synchronized String getUsername() {
	return username;
    }

    public synchronized InetAddress getAddress() {
	return address;
    }

    public synchronized void setAddress(InetAddress address) {
	this.address = address;
    }

    public synchronized void logout() {
	setActive(false);
    }

    @Override
    public synchronized boolean equals(Object user) {
	if (user == null) {
	    return false;
	}
	if (getClass() != user.getClass()) {
	    return false;
	}
	return getUsername().equals(((User) user).getUsername());
    }

    public synchronized int compareTo(User user) {
	return getUsername().compareTo(((User) user).getUsername());

    }

    @Override
    public synchronized String toString() {
	return username;
    }

    public synchronized void getMessages() {
	for (String s : messages) {
	    ServerNotifier sn = new ServerNotifier(getAddress(), getUdpPort());
	    sn.send(s);
	}
    }

    public synchronized void addMessage(String message) {
	messages.add(message);
    }

    void initPublicKey() {
	if (this.publicKey == null) {
	    PEMReader publIn = null;
	    try {
		publIn = new PEMReader(new FileReader(Data.getInstance().getKeydirpath()+this.username+".pub.pem"));
	    } catch (FileNotFoundException ex) {
		System.out.println(": cannot find the public key of " + this.username);
	    }
	    try {
		publicKey = (PublicKey) publIn.readObject();
	    } catch (IOException ex) {
		//TODO
	    }
	    try {
		publIn.close();
	    } catch (IOException ex) {
		//TODO
	    }
	}
    }

    public PublicKey getPublicKey() {
	return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
	this.publicKey = publicKey;
    }

    public String getTcpPort() {
	return tcpPort;
    }

    public void setTcpPort(String tcpPort) {
	this.tcpPort = tcpPort;
    }
    
}
