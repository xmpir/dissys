package dslab.bidclient;

public class ClientUser {
	private String name;

	ClientUser(){
	}

	ClientUser(String name){
		this.name = name;
	}

	public synchronized String getName(){
		return name;
	}

	public synchronized void setName(String name){
		this.name = name;
	}

}
