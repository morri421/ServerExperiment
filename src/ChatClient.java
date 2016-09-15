import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient {
		
	JTextArea incoming;
	JTextField outgoing;
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChatClient client = new ChatClient();
		client.activate();
	}
		
	public void activate() {  //creates GUI 
			
		JFrame frame = new JFrame("Chat Client");    
		JPanel mainPanel = new JPanel();
		incoming = new JTextArea(15,50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");  //Sets listener for when Send button is clicked
		sendButton.addActionListener(new SendButtonListener());
		
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(qScroller, BorderLayout.NORTH); //Need to manually set BorderLayout else they'll stack on top of each other
		mainPanel.add(outgoing, BorderLayout.CENTER); 
		mainPanel.add(sendButton, BorderLayout.SOUTH);
		setUpNetworking(); 
		
		Thread readerThread = new Thread(new IncomingReader()); //New Thread to allow server to send text to Jtext 
		readerThread.start();
		
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 350);
		frame.setVisible(true);
	}//close activate
	
	private void setUpNetworking() {
		
		try {
			sock = new Socket("localhost", 8080);
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("network established");
		} catch (IOException e)  {e.printStackTrace();}
	}//close setUpNetworking
	
	public class SendButtonListener implements ActionListener {  //send button action listener
		public void actionPerformed(ActionEvent ev) {
		try {
			writer.println(outgoing.getText());  //any text in send box is sent to server
			writer.flush();
		} catch (Exception ex) {ex.printStackTrace();}
		outgoing.setText(""); //text in send box is deleted
		outgoing.requestFocus();
		}
	}//close inner class
	
	public class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) { //as long as server is running, reads line and appends it to JText 
					System.out.println("read " + message);
					incoming.append(message + "\n");
				}
			} catch (Exception ex) {ex.printStackTrace();}
		}
	}// close IncomingReader
	
}
