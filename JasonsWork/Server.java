import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.*;

public class Server {
	
	private static int PORT = 6760;
	private static Vector<ThreadedServer> connectedClients = new Vector<ThreadedServer>();
	
	public Server(){
		
		ServerSocket ss = null; 
		
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
				connectedClients.add(ths);
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
		}
		
		public void run() {
			BufferedReader br; 
			PrintWriter pw; 
			String transmission; 
			
			try {
				br = new BufferedReader(new InputStreamReader(cs.getInputStream())); 
				pw = new PrintWriter(new OutputStreamWriter(cs.getOutputStream())); 
				
				transmission = br.readLine(); 
				
				System.out.println("Server Recieved: " + transmission); 
				
				for (ThreadedServer ths : connectedClients) {
					((ThreadedServer)ths).sendTransmission(transmission);
				}
			} catch (Exception e) {
				System.out.println("Error creating input/output streams"); 
				e.getMessage(); 
				e.printStackTrace();
			}
		}
		
		public void sendTransmission(String trans) {
			PrintWriter pw; 
			try {
				pw = new PrintWriter(new OutputStreamWriter(cs.getOutputStream())); 
				pw.println(trans); 
				pw.flush();
			} catch (Exception e) {
				System.out.println("Error sending transmission"); 
				e.getMessage(); 
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args){
		new Server();
	}
}