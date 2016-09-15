import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
 
public class Client {
 
    public void go() {
    	
    	try {
        System.out.println("Getting some advice: ");
        Socket socket = new Socket("localhost", 8080);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String advice = reader.readLine();
        System.out.println(advice);
        reader.close();
        socket.close();
    } catch (IOException e) {
    	e.printStackTrace();
    }
    }
    
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.go();
    }
}