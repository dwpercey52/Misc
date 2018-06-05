import java.io.Serializable;

public class ChatPacket implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 10;
	private int messageType;
	private String userName = new String();
	private String message = new String();
	
	ChatPacket(){
		
	}
	
	ChatPacket( int type, String mess, String name){
		messageType = type;
		message = mess;
		userName = name;
	}
	
	
	public int getType() {
		return messageType;
	}
	
	public String getUser() {
		return userName;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String toString() {
		return  "Type:" + messageType + " User:" + userName + " \nMessesge:" + message +"\n";
	}
}