package backend;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import frontend.*;

public class Server implements Runnable{

	private ServerSocket listenSock;
	int clientID = 0;
	private ArrayList<Client> clientList = new ArrayList<Client>();
	public ServerController cont;
	
	Server() throws IOException{
		listenSock = new ServerSocket(60001);
	}
	
	Server(int port) throws IOException{
		listenSock = new ServerSocket(port);
	}
	
	public Server(ServerController cont) throws IOException{
		listenSock = new ServerSocket(60000);
		this.cont = cont;
	}
	
	
	/*public static void  main(String[] args) throws NumberFormatException, IOException {
		Server srv;
		
		if(args.length < 0) {
			srv = new Server(Integer.parseInt(args[0]));
		}
		else {
			srv = new Server();
		}
		
		if(srv.listenSock != null) {
			Thread acceptThread = new Thread(srv);
			acceptThread.start();
		}
		
		
	}*/

	
	@Override
	public void run() {
		while(true) {
			Socket temp = null;
			try {
				temp = listenSock.accept();
				addClient(temp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	private void addClient(Socket sock) throws IOException {
		if(sock != null) {
			System.out.println("Got a new client!!");
			Client newClient = new Client(sock);
			clientList.add(newClient);
			newClient.start();
			
		}
	}
	
	
	public void removeClient(int id) {
		Client temp = null;
		for(int i = 0; i < clientList.size(); i++) {
			temp = clientList.get(i);
			if(temp.getClientID() == id) {
				clientList.remove(i);
				break;
			}
		}
	}
	
	
	public void displayMessage(String mess) {
		System.out.println("User has written a message! - " + mess);
		
	}
	
	
	
	
	
	
	class Client extends Thread{
		
		Socket sock;
		OutputStream out;
		InputStream in;
		String user;
		int id;
		
		
		Client(Socket sock) throws IOException {
			try{
				out = sock.getOutputStream();
				in = sock.getInputStream();
				id = ++clientID;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		public int getClientID() {
			return id;
		}
		
		
		public void run() {
			boolean alive = true;
			
			while(alive) {
				System.out.println("Client sends in a message!");
				displayMessage(Integer.toString(id));
				alive = false;
			}
			
			try {
				close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		public void close() throws IOException {
			removeClient(id);
			if(out != null) {
				out.close();
			}
			if(in != null) {
				in.close();
			}
			if(sock != null) {
				sock.close();
			}
		}
	}
}
