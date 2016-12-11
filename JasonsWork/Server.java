import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.*;
import java.util.concurrent.*;

/*
	@author: Team Flying Mongooeses (Nick Kowalczuk, Jason Nordheim, Lauren Hoffman) 
	@version: 12/4/16 - revised 12/6/16 
	@description: ... 
	
*/ 
public class Server {
	
	private static int PORT = 6760;
	// the connections of clients 
	private static Vector<ThreadedServer> connectedClients = new Vector<ThreadedServer>(); 	
	// names of the people who are connected
	private static ArrayList<String> connectedUsers = new ArrayList<String>();
	// queue of clients who have not yet been placed into a game
	private ConcurrentLinkedQueue<ThreadedServer> matchMakingQueue = new ConcurrentLinkedQueue<ThreadedServer>();
	private final String REGEX = "_h3ll_";//used in server generated transmissions
	private static final String GAME_PLACEMENT_INDICATOR = "GP"; //indicates that the transmission contains the game ID in which the user has been placed
	
	/* 
		Default Constructor 
		Creates Serer Object and sets up the Server to recieve an unlimited number
			clients 
		@author: Jason Nordheim 
		@version: 12/5/16 
		@param: void 
		@rerturn : void 
	*/ 
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
			int matchCount = 0;
			while (keepRunning) {
				cs = ss.accept();
				ThreadedServer ths = new ThreadedServer(cs); 
				ths.start();
				connectedClients.add(ths);
				matchMakingQueue.add(ths);
				if(matchMakingQueue.size()> 1){
					matchCount++;
					matchMakingQueue.poll().setGameID(matchCount);
					matchMakingQueue.poll().setGameID(matchCount);
				}
			}
		} catch (Exception e) {
			// Something went wrong 
			e.getMessage();
			e.printStackTrace();
		}
		
	}
	
	/*
		Inner class of threadedServers that will hold the individual connections for
		each of the connected clients. 
		@author: Jason Nordheim 
		@version 12/5/16 
	*/ 
	public class ThreadedServer extends Thread {
		Socket cs; 
		String screenName; 
		
		/*
			Default Constructor 
			Passess in variables into the thread 
			@author: Jason Nordheim 
			@param: s - the socket on which the connection was formed. 
			@return: void 
		*/ 
		public ThreadedServer(Socket s){
			cs = s; 
		}
		
		/* 
			Run Method 
			Executes series of tasks for the thread 
			@author: Jason Nordheim 
			@param: void 
			@return: void 
		*/ 
		public void run() {
			BufferedReader br; 
			PrintWriter pw; 
			String transmission; 
			
			try {
				br = new BufferedReader(new InputStreamReader(cs.getInputStream())); 
				pw = new PrintWriter(new OutputStreamWriter(cs.getOutputStream())); 
				
				transmission = br.readLine(); 
				System.out.println("Server Recieved transmission: " + transmission); 
				
				if (transmission.startsWith("_US3R_")) {
					// Process Username 
					String usrName = transmission.substring(6); 
					screenName = usrName; 
					// Print to console 
					System.out.println("User <" + screenName + "> connected"); 
					// add user to arraylist
					connectedUsers.add(screenName);
					// send list out to clients 
					String outmsg = announceUsers();
					for (ThreadedServer ths : connectedClients) {
						((ThreadedServer)ths).sendTransmission(outmsg);
					}
				} else {
					
					// Send the transmission verbatem to all connected clients
					for (ThreadedServer ths : connectedClients) {
						((ThreadedServer)ths).sendTransmission(transmission);
						((ThreadedServer)ths).sendTransmission(announceUsers());
					}
				}
			} catch (Exception e) {
				System.out.println("Error creating input/output streams"); 
				e.getMessage(); 
				e.printStackTrace();
			}
		}
		
		/*
			Method for sending out the transmission - send transmsission specified connected client
			@author: Jason Nordheim 
			@version: 12/5/16 
			@param trans - transmission to be sent over the network 
			@return void 
		*/ 
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
		/*
		** Sets the game ID of the current client by sending a formatted string informing them of their game ID.
		** Is basically a shell method that calls sendTransmission()
		** @author Nick Kowalczuk
		** @version 12-7-16
		** @param gameNum the new game ID number to be sent to the client.
		** @return void
		*/
		public void setGameID(int gameNum){
			sendTransmission(REGEX+GAME_PLACEMENT_INDICATOR+REGEX+gameNum);
			//sends a transmission to user indicating the game in which the have been placed
			//client-side: see processTransmission().
			//this might not work, I didnt test it.
			//TODO test this.
		}
		
		public String announceUsers() {
			PrintWriter pw; 
			String out = null;
			try {
				pw = new PrintWriter(new OutputStreamWriter(cs.getOutputStream())); 
				// format string to send 
				out = "_N3WUS3R_"; 
				for (String user: connectedUsers) {
					out += user + "88888"; 
				}
			} catch (Exception e) {
				System.out.println("Error sending username to clients"); 
				e.getMessage();
				e.printStackTrace();
			}
			return out; 
		}
		
	}
	/*
		Main method to run the application 
		@author: Jason Nordheim 
		@version: 12/5/16 
		@param: args - command line arguments entered at applicaiton launch
			as an array of strigns  
			Note: application does not require any command-line arguements 
		@return void
	*/ 
	public static void main(String[] args){
		new Server();
	}
}