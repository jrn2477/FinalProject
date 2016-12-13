import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.*;
import java.util.concurrent.*;
import java.lang.Math;

/*
	@author: Team Flying Mongooeses (Nick Kowalczuk, Jason Nordheim, Lauren Hoffman) 
	@version: 12/4/16 - revised 12/6/16 
	@description: ... 
	
*/ 
public class Server {
	
	private static int PORT = 6761;
	private static final String REGEX = "_h3ll_"; 
	private static final String GAME_MOVE = "GM"; 
	private static final String ATTACK_INDICATOR = "A";
	private static final String GAME_RESPONSE  = "GR";

	// the connections of clients 
	private static ConcurrentLinkedQueue<ThreadedServer> connectedClients = new ConcurrentLinkedQueue<ThreadedServer>();     
	// names of the people who are connected
	private static ArrayList<String> connectedUsers = new ArrayList<String>();
	// queue of clients who have not yet been placed into a game
	private ConcurrentLinkedQueue<ThreadedServer> matchMakingQueue = new ConcurrentLinkedQueue<ThreadedServer>();
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
					// CREATE A DELAY 
					double b = 30; 
					for (int i = 29; i > 0; i--) {
						b = b * i * Math.PI * Math.PI;
						//System.out.println(b);
					}
					matchCount++;
					System.out.println("MatchMakingQueue Block, Size : "+matchMakingQueue.size());
					ThreadedServer p1 = matchMakingQueue.poll();
					ThreadedServer p2 = matchMakingQueue.poll();
					
					// Set game id for both clients
					p1.setGameID(matchCount,p1,p2);
					p2.setGameID(matchCount,p1,p2);
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
		String clientIP; 
		
		/*
			Default Constructor 
			Passess in variables into the thread 
			@author: Jason Nordheim 
			@param: s - the socket on which the connection was formed. 
			@return: void 
		*/ 
		public ThreadedServer(Socket s){
			cs = s; 
			System.out.println("Threaded Server Created");    
			String fullip = this.cs.getInetAddress().toString(); 
			int index = fullip.indexOf("/");
			clientIP = fullip.substring(index + 1);    
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
				
				String parsedTrans = transmission.substring(REGEX.length()); 
				System.out.println("Parsed Trans: "+parsedTrans);
				String[] t = transmission.split(REGEX);
				
				
				
				
				
				// GAME RESPONSE 
				if (parsedTrans.startsWith(GAME_RESPONSE)) {
					System.out.println("Game Response Recieved");
				    //TRANSMISSION IS A GAME RESPONSE 
				    String trans = transmission.substring(GAME_RESPONSE.length(), transmission.length());
				    String split[] = trans.split(REGEX); 
				    
				    String index = split[0]; 
				    String hitMiss = split[1]; 
				    String oppIP = split[2]; // TO 
				    String myIP = split[3]; // FROM 
				    
				    System.out.println(split[5]); 
				    
				    if(myIP.equals(this.clientIP)) {
				        for (ThreadedServer ths: connectedClients) {
    						if (ths.clientIP.equals(oppIP)) {
    							sendTransmission(GAME_RESPONSE + index + REGEX + hitMiss +REGEX); 
    						}
    					}
				    } 
				    else { /* I am not being attacked, do nothing */ }
				}
				
				
				
				
				
				
				// TRANSMISSION IS A GAME ATTACK 
				if (parsedTrans.startsWith(ATTACK_INDICATOR)) {
					System.out.println("Attacked Recieved"); 
    				String[] split = transmission.split(REGEX); 
    				


    				String personFromIP = split[3];
    				String personToIP = split[4]; 
    				String locationAttacked = split[5];

					System.out.println("FROM: " + personFromIP );
					System.out.println("TO: " + personToIP); 
					System.out.println("POS: " + locationAttacked); 

    				
    				if (personToIP.equals(this.clientIP)) {
    					// I am being attacked 
    					// send the attack to the appropirate client 
    					for (ThreadedServer ths: connectedClients) {
    						if (ths.clientIP == personToIP) {
								String outmsg= (ATTACK_INDICATOR + personFromIP 
								+ REGEX + personToIP +REGEX + locationAttacked);
								System.out.println("Sending message to: " + ths.clientIP); 
								System.out.println("Out Message: " + outmsg);
    							sendTransmission(outmsg); 
    						}
    					}
    				} else { /* I am attacking, do nothing */  }
    			}// end trasminssion.startsWith(GAME_MOVE)
    			
    			



    			
    			// TRANSMISSION IS A NEW USER 
				if (transmission.startsWith("_US3R_")) {
					String usrName = transmission.substring(6);	// Process Username 
					screenName = usrName; 
					System.out.println("User <" + screenName + "> connected"); 	// Print to console 
					connectedUsers.add(screenName);// add user to arraylist
					// send list out to clients 
					String outmsg = announceUsers();
					for (ThreadedServer ths : connectedClients) {
						((ThreadedServer)ths).sendTransmission(outmsg);
    					}
				}//end transmission.startWith("_US3R_"); 
				else { // Send the transmission verbatem to all connected clients
					for (ThreadedServer ths : connectedClients) {
						System.out.println("Sending: "+ transmission);
						if (ths.screenName == null) {
							// do nothing 
						} else {
							System.out.println("To: "+ ths.screenName);
							((ThreadedServer)ths).sendTransmission(transmission);
							((ThreadedServer)ths).sendTransmission(announceUsers());
						}
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
		public void setGameID(int gameNum, ThreadedServer player1, ThreadedServer player2){
	
			String p1Name = player1.screenName; 
			String p1fullIP = player1.cs.getInetAddress().toString(); 
			int p1index = p1fullIP.indexOf("/");
			String p1trimedIP = p1fullIP.substring(p1index + 1);
			
			String p2Name = player2.screenName;
			String p2fullIP = player2.cs.getInetAddress().toString(); 
			int p2index = p2fullIP.indexOf("/");
			String p2trimedIP = p2fullIP.substring(p2index + 2);
			
			System.out.println("Starting Game"); 
			sendTransmission("_G@M3_" + p1trimedIP + REGEX + p2fullIP + REGEX + gameNum + REGEX); 
			sendTransmission(REGEX + GAME_PLACEMENT_INDICATOR + REGEX 
			+ p1Name + REGEX + p2Name + REGEX + gameNum);
			//sendTransmission(REGEX+"M"+"Server"+"You were placed in game")
			//sends a transmission to user indicating the game in which the have been placed
			//client-side: see processTransmission().
			//this might not work, I didnt test it.
			//TODO test this.
		}
		
		
		/*
			Method to Announce Users 
			@author: Jason Nordheim 
			@return: String (the string to be transmitted to the connected clients) 
		*/ 
		public String announceUsers() {
			PrintWriter pw; 
			String out = null;
			try {
				pw = new PrintWriter(new OutputStreamWriter(cs.getOutputStream())); 
				// format string to send 
				out = "_N3WUS3R_"; 
				for (String user: connectedUsers) {
					out += user + "_E50328A_"; 
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