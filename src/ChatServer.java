import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {
	
	ArrayList<Writer> clientOutputStreams;
	
	public class ClientHandler implements Runnable { 
		BufferedReader reader;
		Socket sock;
		
		public ClientHandler(Socket clientSocket) { //constructor to take socket input 
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (Exception ex) {ex.printStackTrace();}
		} //close constructor
		
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("read " + message);
					tellEveryone(message);
				}
			} catch (Exception ex) {ex.printStackTrace();}
		}//close run
	}//close inner class

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatServer().activate();
	}
	
	public void activate() {
		clientOutputStreams = new ArrayList<Writer>();
		try {
			ServerSocket serverSock = new ServerSocket(8080);
			
			while (true) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStreams.add(writer);
				
				Thread t = new Thread(new ClientHandler(clientSocket)); //creates a ClientHandler thread to run
				t.start();												//with chat client
				System.out.println("got a connection");
			}
		} catch (Exception ex) {ex.printStackTrace();}
	} //close activate
	
	public void tellEveryone(String message) {
		
		Iterator it = clientOutputStreams.iterator();
		while(it.hasNext()) { //runs if it has more elements
			try {
				PrintWriter writer = (PrintWriter) it.next(); //returns the next element
				writer.println(message); //
				writer.flush();
			} catch (Exception ex) {ex.printStackTrace();}
		}  
	} //close tellEveryone
} //close class