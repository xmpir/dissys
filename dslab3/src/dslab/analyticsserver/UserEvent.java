package dslab.analyticsserver;

public class UserEvent extends Event{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2412729672938786681L;
	private String userName;
	public static final String login = "USER_LOGIN";
	public static final String logout = "USER_LOGOUT";
	public static final String disconnect = "USER_DISCONNECT";

	public UserEvent(String type, long timestamp, String userName) throws EventNotFoundException{
		if (!type.equals(login) && !type.equals(logout) && !type.equals(disconnect)){
			throw new EventNotFoundException();
		}
		this.type = type;
		this.timestamp = timestamp;
		this.userName = userName;
		
	}


	public String getUserName() {
		return userName;
	}
}
