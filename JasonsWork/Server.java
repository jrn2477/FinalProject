import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
	
	private static int PORT = 6760;
	private static Vector<ThreadedServer> connectedClients = new Vector<ThreadedServer>();
	
	public Server(){
		
		ServerSocket ss; 
		
		try {
			// Get IP Address: 
			String fullIP = InetAddress.getLocalHost().toString(); 
			int index = fullIP.indexOf("/");
			String trimedIP = fullIP.substring(index + 1);
			System.out.println("IP Address: " + trimedIP); 
			
			// Create Connection
			ss = new ServerSocket(PORT); 
			Socket cs = null; 
			
			// Continue to accept clients 
			Boolean keepRunning = true; 
			while (keepRunning) {
				cs = ss.accept();
				ThreadedServer ths = new ThreadedServer(cs); 
				ths.start();
			}
		} catch (Exception e) {
			// Something went wrong 
			e.getMessage();
			e.printStackTrace();
		}
		
	}
	
	public class ThreadedServer extends Thread {
		Socket cs; 
		String screenName; 
		
		public ThreadedServer(Socket s){
			cs = s; 
			connectedClients.add(this);
		}
		
		public void run() {
			BufferedReader br; 
			PrintWriter pw; 
			String transmission;
			boolean keepRunning = true; 
			
			while (keepRunning) {
				try {
					br = new BufferedReader(new InputStreamReader(cs.getInputStream())); 
					pw = new PrintWriter(new OutputStreamWriter(cs.getOutputStream())); 
					
					
					/* read in message for client  */ 
					transmission = br.readLine(); 

					
					/* Parse transmission */ 
					if (transmission != null) {
						System.out.println("Server Read: " + transmission);
						
						// Parse transmission for username 
						try {
							String[] splitTransmission = transmission.split("_h3lp_"); 
							screenName = splitTransmission[2];
						} 
						catch (Exception e) {
							System.out.println("Unable to get username");							
						}
						
						
						
						// Pass transmission to connected clients 
						pw.println(transmission);
						// Flush output stream
						pw.flush();
					} 
					else {
						// Transmission was null. Client likely disconnected 
						pw.println(screenName + " disconnected");
						keepRunning = false;
					}
				} catch (Exception e) {
					System.out.println("Error with input/output streams");
				} 
			}
		}
	}
	
	public static void main(String[] args){
		new Server();
	}
}