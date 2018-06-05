import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;


public class ChatClient {
		
	private static String userName;
	
	ChatClient(){
		
	}
	
	ChatClient(String user){
		userName = user;
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException{
		String name = new String();
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter an user name for yourself: ");
		name = in.nextLine();
		ChatClient client = new ChatClient(name);
		client.runchat();
		in.close();
		
	}
	
	
	public void runchat() throws UnknownHostException, IOException {
		Socket connect = new Socket("localhost",60030);
		Scanner input = new Scanner(System.in);
		ObjectOutputStream out = new ObjectOutputStream(connect.getOutputStream());
		//ObjectInputStream in = connect.getOn
		
		ChatPacket pack = new ChatPacket(1, "Goodbye", userName);
	//	String temp = "hello";
	//	out.write(temp.getBytes());
		out.writeObject(pack);
		//ObjectOutputStream clientOutput = new ObjectOutputStream(connect.getOutputStream());
		//clientOutput.writeObject(packet);
		//System.out.println(packet);
		input.close();
		connect.close();
	}
	
}