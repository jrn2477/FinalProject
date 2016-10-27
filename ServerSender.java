import java.io.*;
import java.net.*;
import java.util.*;


public class ServerSender extends Thread{
	
	ServerSocket ss; 
	int portNumber; 
	
	
	/* Default Constructor */ 
	public ServerSender(int portNum) {
		// TODO: insert write class, to append messages to the server log 

	
	}
	
	/* Run Method - Required for all classes implementing threads */ 
	public void run(){
		
		Socket clientSocket;
		try {
			while((clientSocket = server.accept()) != null){
				InputStream is = clientSocket.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is)); 
				String line = br.readLine();
				if(line != null){
					gui.write(line);
				}
			}
		} catch (IOException ex) {
			// Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
}