package dslab.auctionserver;

public class Updater implements Runnable{
	Lists lists = new Lists();


	public Updater(Lists lists){
		this.lists = lists;
	}

	public void run(){
		lists.updateAuctions();
	}
}
