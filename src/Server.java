import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
 
public class Server {
    
    private final static String[] alist = {
        "Meditate", 
        "Rome wasn't built in a day", 
        "No zero days",  
        "Don't stress yourself out"
    };
 
    private void go() {
    	try {
        ServerSocket serverSocket = new ServerSocket(8080);
        while(!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String advice = getAdvice();
            System.out.println("Sending advice: " + advice);
            writer.write(advice);
            writer.close();
            System.out.println("Advice sent!");
            socket.close();
        }
    } catch (IOException e) {
    	e.printStackTrace();
    }
    }
    private static String getAdvice() {
        return alist[0];
    }
    
    public static void main(String[] args) throws IOException {
        
        Server server = new Server();
        server.go();
    }
}