import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.JOptionPane.*;
import java.net.*;
import java.io.*;
import javax.xml.ws.handler.*;

/*
	@author: Team Flying Mongooses (Nick Koowalcuk, Lauren Hoffman, Jason Nordheim) 
	@version: 11/30/16 v1 
	@decription: Class responsible for the visual interface of the client 
*/ 
public class GUI extends JFrame implements ActionListener{
	
	private static String IP_ADDRESS, PORT, SCREEN_NAME, MY_IP; 
	private static final String YOUR_BOARD = "Your Board"; 
	private static final String REGEX = "_h3ll_"; 
	private static final String MESSAGE_INDICATOR = "M"; 
	private static final String ATTACK_INDICATOR = "A";
	private static final String STATUS_INDICATOR = "S";
	private static final String GAME_PLACEMENT_INDICATOR = "GP";
	private static final String GAME_MOVE = "GM"; 
	private static final String GAME_RESPONSE  = "GR";
	private static final String GAME_START = "GS"; 
	private static int gameID;//will indicate which game the user is in
	private static String OPPONENT_IP = null; 
	private static int hits = 0; 
	

	private static final String OPP_BOARD = "Opponents Board"; 
	private static ArrayList<JButton> board1 = new ArrayList<JButton>(); 
	private static ArrayList<JButton> board2 = new ArrayList<JButton>(); 
	private JPanel myBoardPanel, oppBoardPanel, boardsPanel, gamePanel, gameBoard1, gameBoard2, 
		chatPanel, shipPositionsPanel, connectionPanel; 
	private static JTextArea chatTextArea, userList; 
	private static JTextField messageTextField, ipAddressTextField, portTextField, userNameTextField,
		ship1bow, ship1mid, ship1stern, ship2bow, ship2mid, ship2stern, ship3bow, ship3mid, ship3stern; 
	private static JButton sendMessageBtn, connectBtn, placeshipsBtn; 
	private static Socket socket;
	private static BufferedReader br; 
	private static PrintWriter pw; 
	private static boolean connected;
	private static Vector<ChatReader> chatReaders = new Vector<ChatReader>();
	public static ArrayList<String> connectedUserList = new ArrayList<String>();
	private static ArrayList<Integer> shipLocations = new ArrayList<Integer>();
	
	/*
		Default Constructor 
		@author: Jason Nordheim 
		@version: 11/20/16 
	*/ 
	public GUI(){
		// Get IP Address: 
		try {
			String fullIP = InetAddress.getLocalHost().toString(); 
			int index = fullIP.indexOf("/");
			String trimedIP = fullIP.substring(index + 1);
			MY_IP = trimedIP;
			System.out.println("IP Address: " + trimedIP); 
		} catch (Exception e) {
			System.out.println("Failed to pull IP address"); 
			e.getMessage();
			e.printStackTrace();
		}
		
		JMenuBar jmb = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem jmi_help = new JMenuItem("Help"); 
		jmi_help.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				JOptionPane.showMessageDialog(null, 
				"Before the players begin guessing the locations of the ships they first have to place \n"+
				"their ships on the game board. Each player has exactly three ships and the ships are \n"+
				"three units long.  If the player does not have enter positions for all 3 ships the game \n"+
				"displays an error message. The application will also display error messages if the \n"+
				"player tries to place a ship less than 3 units long, or the entered locations are not\n"+
				"contiguous . The ships can only be placed vertically and horizontally on the board \n"+
				"and the ships cannot overlap.  Players can reset their ship locations before the game\n"+
				"starts, but once the game starts, the ship's positions are fixed. \n\n\n"+
				
				"Once all the players have committed their ships locations, the players can start guessing\n"+
				"the locations.  That location then changes color according to the result. Red marks \n"+ 
				"result. Red marks a place where a ship is located and blue marks empty water where there is \n"+
				"no ship. The game is over when all the ships have been found. The winner is determined by \n"+
				"whichever player had the least amount of moves. When the game ends a pop up appears\n"+
				"displaying the number of moves for that user and the winning player’s username.\n"
				);
			}
		});
		
		
		JMenuItem jmi_about = new JMenuItem("About");
		jmi_about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				JOptionPane.showMessageDialog(null, 
				"BattleBoats is a multiplayer battleship game with a chat feature.  Each\n" +
				"player loads the game and enters a the IP address, and Port of the server in order to\n" + 
				"connect. They also must provide a username, which will identify them in the chat \n"+ 
				"feature of the application. All this must occur before the user can launch the game.\n\n\n"+
				
				"Game Created By: Jason Nordheim, Nick Kowalczuk, Lauren Hoffman"
				);
			}
		});

		JMenuItem jmi_exit = new JMenuItem("Exit");
		jmi_exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae ){
				System.exit(0); 
			}
		});
		
		file.add(jmi_about);
		file.add(jmi_help);
		file.add(jmi_exit);
		jmb.add(file);
		
		
		//Listener listen = new Listener(); 
		setTitle("Let's Play Battleship"); 
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
		setJMenuBar(jmb);
		getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
		setUpGameBoards(); 
		setUpChat();
		setVisible(true);
		pack(); 
	}
	
	/*
		@author: Jason Nordheim 
		@version: 11/29/30; updated 12/1/16 
		@description: Method to setup the initial GUI
	*/ 
	public void setUpGameBoards(){
		//Setup Boards Panel 
		gameBoard1 = new JPanel();
		gameBoard1.setLayout(new BorderLayout(10,10)); 
		gameBoard2 = new JPanel();
		gameBoard2.setLayout(new BorderLayout(10,10));
		
		//Setup Labels 
		JLabel lbl1 = new JLabel(YOUR_BOARD);
		lbl1.setHorizontalAlignment(JLabel.CENTER);
		JLabel lbl2 = new JLabel(OPP_BOARD); 
		lbl2.setHorizontalAlignment(JLabel.CENTER);
		gameBoard1.add(lbl1, BorderLayout.NORTH); 
		gameBoard2.add(lbl2, BorderLayout.NORTH); 

		// Setup Board 1 
		myBoardPanel = makeBoard(board1);
		gameBoard1.add(myBoardPanel, BorderLayout.CENTER); 
		
		// Setup Board 2 
		oppBoardPanel = makeBoard(board2); 
		gameBoard2.add(oppBoardPanel, BorderLayout.CENTER); 
		
		gamePanel = new JPanel(new GridLayout(1,2,10,25));
		gamePanel.add(gameBoard1); 
		gamePanel.add(gameBoard2); 
		
		// Setup Ship positioning elements 
		JLabel ship1Label = new JLabel("Ship 1"); 
		JLabel ship2Label = new JLabel("Ship 2"); 
		JLabel ship3Label = new JLabel("Ship 3"); 

		JLabel ship1Position1 = new JLabel("Bow"); 
		ship1Position1.setHorizontalAlignment(JLabel.RIGHT); 
		JLabel ship2Position1 = new JLabel("Bow"); 
		ship2Position1.setHorizontalAlignment(JLabel.RIGHT); 
		JLabel ship3Position1 = new JLabel("Bow"); 
		ship3Position1.setHorizontalAlignment(JLabel.RIGHT);
		
		JLabel ship1Position2 = new JLabel("Mid"); 
		ship1Position2.setHorizontalAlignment(JLabel.RIGHT);
		JLabel ship2Position2 = new JLabel("Mid"); 
		ship2Position2.setHorizontalAlignment(JLabel.RIGHT);
		JLabel ship3Position2 = new JLabel("Mid");
		ship3Position2.setHorizontalAlignment(JLabel.RIGHT);
		
		JLabel ship1Position3 = new JLabel("Stern"); 
		ship1Position3.setHorizontalAlignment(JLabel.RIGHT);
		JLabel ship2Position3 = new JLabel("Stern"); 
		ship2Position3.setHorizontalAlignment(JLabel.RIGHT);
		JLabel ship3Position3 = new JLabel("Stern");
		ship3Position3.setHorizontalAlignment(JLabel.RIGHT);
		
		// setup the text fields for position entry 
		// disable fields by default 
		// Ship 1 Textfields 
		ship1bow = new JTextField(2);
		ship2bow = new JTextField(2);
		ship3bow = new JTextField(2);

		// Ship 2 Textfields 
		ship1mid = new JTextField(2);
	    ship2mid = new JTextField(2);
		ship3mid = new JTextField(2);
		
		// ship 3 textfields 
		ship1stern = new JTextField(2);
		ship2stern = new JTextField(2);
		ship3stern = new JTextField(2);
		
		// place ships button
		placeshipsBtn = new JButton("Place Ships!");
		placeshipsBtn.setHorizontalAlignment(JButton.CENTER);     
		placeshipsBtn.setEnabled(false);
		placeshipsBtn.addActionListener(new PlaceShipsListener()); 
							
		shipPositionsPanel = new JPanel(new GridLayout(3,7));
		
		// Add ship 1 
		shipPositionsPanel.add(ship1Label); 
		shipPositionsPanel.add(ship1Position1);
		shipPositionsPanel.add(ship1bow); 
		shipPositionsPanel.add(ship1Position2);
		shipPositionsPanel.add(ship1mid); 
		shipPositionsPanel.add(ship1Position3);
		shipPositionsPanel.add(ship1stern); 
		
		// add ship 2 
		shipPositionsPanel.add(ship2Label); 
		shipPositionsPanel.add(ship2Position1);
		shipPositionsPanel.add(ship2bow); 
		shipPositionsPanel.add(ship2Position2);
		shipPositionsPanel.add(ship2mid); 
		shipPositionsPanel.add(ship2Position3);
		shipPositionsPanel.add(ship2stern);  
		
		// add ship 3 
		shipPositionsPanel.add(ship3Label); 
		shipPositionsPanel.add(ship3Position1);
		shipPositionsPanel.add(ship3bow); 
		shipPositionsPanel.add(ship3Position2);
		shipPositionsPanel.add(ship3mid); 
		shipPositionsPanel.add(ship3Position3);
		shipPositionsPanel.add(ship3stern);  
		
		boardsPanel = new JPanel(new BorderLayout(10,10)); 
		boardsPanel.add(gamePanel, BorderLayout.NORTH); 
		boardsPanel.add(shipPositionsPanel, BorderLayout.CENTER); 
		boardsPanel.add(placeshipsBtn, BorderLayout.SOUTH);
		// Add the Boards Panel to the contentPane 
		this.getContentPane().add(boardsPanel); 
	}
	
	public void setUpChat() {
		// setup connection Labels 
		JLabel ipLabel, portLabel, screenNameLabel; 
		ipLabel = new JLabel("IP Address:"); 
		portLabel = new JLabel("Port Number:"); 
		screenNameLabel = new JLabel("Screen Name");
		
		// setup textfields for connection
		ipAddressTextField = new JTextField(36); 
		portTextField = new JTextField(6);
		portTextField.setText("6760");
		userNameTextField = new JTextField(20); 
		
		// setup level 1 of connection panel 
		JPanel level1 = new JPanel(new FlowLayout()); 
		level1.add(ipLabel);
		level1.add(ipAddressTextField);  
		
		// setup level 2 of connection panel 
		JPanel level2 = new JPanel(new FlowLayout()); 
		level2.add(portLabel); 
		level2.add(portTextField); 
		level2.add(screenNameLabel); 
		level2.add(userNameTextField); 
		
		
		JPanel level3 = new JPanel(new FlowLayout()); 
		connectBtn = new JButton("Connect"); 
		connectBtn.addActionListener(new ConnectListener()); 
		level3.add(connectBtn); 
		
		// setup connection panel 
		connectionPanel = new JPanel(new GridLayout(3,1)); 
		connectionPanel.add(level1); 
		connectionPanel.add(level2);
		connectionPanel.add(level3); 
		
		// setup the chat area
		chatTextArea = new JTextArea(15,20); 
		chatTextArea.setEnabled(false);
		userList = new JTextArea(15,10);
		JScrollPane jscp1 = new JScrollPane(userList);
		JScrollPane jscp2 = new JScrollPane(chatTextArea); 
		
		// setup the message textfield 
		messageTextField = new JTextField(40); 
		messageTextField.setEditable(false); // disable until connection is established 
		userList.setEditable(false);
		
		// send button 
		sendMessageBtn = new JButton("Send"); 
		sendMessageBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				sendTransmission("M",messageTextField.getText(), userNameTextField.getText());
				messageTextField.setText(null);
			}
		});
		
		//set up chat panel 
		chatPanel = new JPanel(new BorderLayout(5,5)); 
		chatPanel.add(jscp2, BorderLayout.CENTER); 
		
		// set up messaging panel 
		JPanel sendMessagePanel = new JPanel(new FlowLayout()); 
		sendMessagePanel.add(messageTextField); 
		sendMessagePanel.add(sendMessageBtn); 
		chatPanel.add(sendMessagePanel, BorderLayout.SOUTH);
		chatPanel.add(connectionPanel, BorderLayout.NORTH);
		chatPanel.add(jscp1, BorderLayout.WEST);
		
		// add to the content pane
		this.getContentPane().add(chatPanel);
	}
	
	/*
		@author: Jason Nordheim
		@version: 12/2/16 
		@param: void 
		@return: void 
		*/ 
	public static void createConnection(){
		try {
			// Create connection
			socket = new Socket(IP_ADDRESS,Integer.parseInt(PORT)); 
			InetAddress add = socket.getInetAddress(); 
			System.out.println("Connect to Server at: " + IP_ADDRESS);        
			
			// Pass the username to server
			try {
				pw = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream())); 
				pw.println("_US3R_" + SCREEN_NAME);
				pw.flush(); 
			} catch (Exception e) {
				System.out.println("Unable to send username to server"); 
				e.getMessage(); 
				e.printStackTrace();
			}
			ChatReader cr = new ChatReader(socket);
			cr.start();
		} catch (Exception e) {  
			// re-enable disabled fields
			createErrorMessage(e.getMessage());
			ipAddressTextField.setEditable(true);
			portTextField.setEditable(true);
			userNameTextField.setEditable(true);
			connectBtn.setEnabled(true);
			messageTextField.setEditable(false);
			e.getMessage(); 
			e.printStackTrace(); 
			}
	}
	/*
		Method to send a transmission to the server 
		@author: Jason Nordheim 
		@version: 12/5/16 
		@param: transmissionType - the type of transimssion to be sent 
		@param: transmissionContent - the content of the transmission sent 
		@param: userName - the user name of the person sending the message. 
	*/ 
	public static void sendTransmission(String transmissionType, String transmissionContent, String userName) {
		try {
			// Create connection
			socket = new Socket(IP_ADDRESS,Integer.parseInt(PORT)); 
			
			// Create Output Streams
			PrintWriter pw = new PrintWriter(
				new OutputStreamWriter(socket.getOutputStream())); 
				
			String regex = REGEX;
			// TESTING PURPOSES:
			System.out.println("PW: " + pw.toString());
			pw.println(regex + transmissionType + regex + userName + regex + transmissionContent + regex + gameID);
			pw.flush();
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}
	
	public static void createErrorMessage(String s) {
		JOptionPane.showMessageDialog(null, s,
					 "ERROR", JOptionPane.ERROR_MESSAGE);
	}
	
	/*
		@author: Jason Norhdeim 
		@version: 11/29/16 
		@description: Method to create a battlefield board. 
		@param: ArrayList which will store each position panel 
		@return: JPanel of board
	*/ 
	public JPanel makeBoard(ArrayList<JButton> aryLst){
		JPanel board = new JPanel(new GridLayout(8,8)); 
		for (int i = 1; i < 65; i++) {
			JButton b = new JButton(Integer.toString(i)); 
			aryLst.add(b); 
			aryLst.get(i-1).setPreferredSize(new Dimension(35,35)); 
			aryLst.get(i-1).setBorder(LineBorder.createBlackLineBorder());
			// REMOVED -- aryLst.get(i-1).add(new JLabel(Integer.toString(i))); -- 
			aryLst.get(i-1).addActionListener(this);
			aryLst.get(i-1).setEnabled(false); // DEFAULT TO DISABLED 
			board.add(aryLst.get(i-1)); 
		}
		return board; 
	}
	
	public static void appendChat(String s) {
		chatTextArea.append(s + "\n"); 
	}

	
	/*
		Inner Class that creates a thread to constantly read in transmissions 
		recieved from the server without stopping other functionality as reqiured. 
		@author: Jason Nordheim 
		@version: 12/5/16
	*/ 
	public static class ChatReader extends Thread {
		private Socket socket; 
		
		/*
			Default Constructor - creates ChatReader object 
			@author: Jason Nordheim 
			@version: 12/5/16 
			@return: null 
		*/ 
		public ChatReader(Socket s){
			socket = s; 
			connected = true;
			chatReaders.add(this);
		}
		
		/*
			Run Method - Activiated by .start() command 
				begins thread actions 
			@author: Jason Nordheim 
			@version: 12/5/16 
			@param: void 
			@return: void 
		*/
		public void run() {
			
			System.out.println("Chatreader Started");

			connected = true;
			
			while (connected) {
				pullFromServer();
			}
		}
		
		/*
			Method to check the server and pull and transmissions sent by the server 
			@author: Jason Nordheim 
			@version: 12/5/16 
			@param: void 
			@return: void 
		*/ 
		public void pullFromServer(){
			try {
				// FOR TESTING --> System.out.println("In try");
				BufferedReader br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
				String transmission = br.readLine(); 
				
				if (transmission != null) {
					System.out.println("My IP" + MY_IP);
					System.out.println("transmission recieved: "+ transmission);
					processTransmission(transmission);
				} else {
					createErrorMessage("Connection to server lost");
				}
								
				} catch (Exception e) {
				e.getMessage(); 
				e.printStackTrace();
			}    
		}
		
	}//end ChatReader 
	public static void startGame(String in){
	    System.out.println("IN : " + in); 
		String ind = "_G@M3_"; 
	    String message = in.substring(ind.length(), in.length());
		System.out.println("MESSAGE : " + message); 
	
		String[] split = message.split(REGEX);
		
		String p1IP = split[0]; 
		String p2IP = split[1]; 
		String gameNum = split[2]; 
		
		/* I am player 1 */ 
		if (p1IP.equals(MY_IP)) { OPPONENT_IP = p2IP; }
		/* I am player 2 */
		else { OPPONENT_IP = p1IP; }
		gameID = Integer.parseInt(gameNum);
		
		// ENABLE COMPONENTS 
		placeshipsBtn.setEnabled(true); 
		appendChat("Your game has started... Place your ships");  
	}
	
	/*
	    Method to process game attacks
	    @author: Paul 
	    @version 12/13/16
	    @param: input - the transmission recieved by the client 
	*/
	public static void processGameAttack(String input){
	    // 1. Attack indicator 
	    // 2. person from 
	    // 3. person to 
	    // 4. attack posittion 
	    
	    // Removed ATTACK INDICATOR, parse and split string 
		String put = input.substring(1, input.length());
	    String split[] = put.split(REGEX); 
	    
	    String personFrom = split[1]; 
	    String personTo = split[2]; 
	    String attackPos = split[3]; 
	    
	    int attIndex = (Integer.parseInt(attackPos) - 1) ;
	    
	    // Test attacked Location 
	    if(shipLocations.contains(attIndex)){
	        board1.get(attIndex).setBackground(Color.RED);
	        hits++;
	        replyToAttack(attackPos, true);
	    }
	    else {
	        board1.get(attIndex).setOpaque(true);
	        board1.get(attIndex).setBackground(Color.BLUE);
	        replyToAttack(attackPos, false);
	    }
	}
	
	
	
	public static void processResponse(String index, String hitMiss) {
	    int ind = Integer.parseInt(index);
	    ind--;
	    if(hitMiss.equals("h")){
	        board2.get(ind).setOpaque(true);
	        board2.get(ind).setBackground(Color.RED);
	    }
	    else{
	        board2.get(ind).setOpaque(true);
	        board2.get(ind).setBackground(Color.BLUE);
	    }
	}
	
	
	
	/*
	    Method to announce hit or miss to opponent 
	    @author: Paul 
	    @version 12/13/16 
	    @param: index - the position attacked 
	    @param: hitMiss - whether the position attacked was a hit/miss 
	*/ 
	public static void replyToAttack(String index, boolean hitMiss){
	    if(hitMiss){
	        sendTransmission(GAME_RESPONSE, index+REGEX+"h", "The game is trying to respond" );
	    }
	    else{
	        sendTransmission(GAME_RESPONSE, index+REGEX+"m", "The game is trying to respond" );
	    }
	}
	/* 
		Method to Process Transmissions from the server, the method will parse 
		the transmission to determine the type of transmission before processing 
		@author: Jason Nordheim 
		@param: trans - the transmission recieved from the server 
		@version: 12/5/16
	*/ 
	public static void processTransmission(String trans) {
		if (trans.equals(null)) {
			JOptionPane.showMessageDialog(null, "Lost connection to server. Please close and re-connect");
		}
		
		String[] split = trans.split(REGEX);
		
		System.out.println("Split 1: " + split[0]);
		
		if (split[0].equals(GAME_RESPONSE)) {
			System.out.println("Game Response Recieved"); 
		    String index = split[1];
		    String hitMiss = split[2];
			processResponse(index, hitMiss);
		}
				
		if(split[0].equals(ATTACK_INDICATOR)){
			System.out.println("Game Attack Recieved"); 
			processGameAttack(trans);
		}
		
		if (split[0].equals(GAME_PLACEMENT_INDICATOR)) {
			String p1Name = split[2]; 
			String p2Name = split[3]; 
			String game = split[4];
			appendChat("Game " + game + " started between " + p1Name 
			+ "(p1) and " + p2Name + "(p2)"); 
		}
		
		
		// NEW GAME 
		if (trans.startsWith("_G@M3_")) {
			if (trans.substring(6).equals(MY_IP)) {
				startGame(trans); 
			} else if (split[2].equals(MY_IP)){
				startGame(trans);
			}
		}
		
		if (trans.startsWith("_N3WUS3R_")) {
			// get username 
			connectedUserList.clear();
			//System.out.println("inside N3WUS3R block");
			int startPos = ("_N3WUS3R/_").length() - 1;
			String nameList = trans.substring(startPos); 
			//System.out.println("Name List: " + nameList); 
			String[] tempList = nameList.split("_E50328A_");
			for (int i = 0; i < tempList.length; i++) {
				connectedUserList.add(tempList[i]);
			}
			updateUserList();
		}
		
		else if (trans.startsWith(REGEX)) {
			String[] splitTrans = trans.split(REGEX); 
			
			if (splitTrans[1].equals(MESSAGE_INDICATOR)) {
				// Transmission is a message 
				processMessage(splitTrans[2], splitTrans[3]);
			}
			
			/*
			if (splitTrans[1].equals(GAME_MOVE) && !splitTrans[2].equals(SCREEN_NAME)) {
				String userName = splitTrans[2]; // get the username 
				String tempPos = splitTrans[3]; // Position as a string 
				String game = splitTrans[4]; // Get the game ID 
				// getting the position of the button in the ArrayList
				Integer attackPos = Integer.parseInt(tempPos) - 1;
				// Store game id            
				if(gameID == Integer.parseInt(game)){
					// validate the move, send the new transmission, return as hit/miss (h/m) 
					System.out.println("processTransmission.Game_Move"); // CHECK 
					if(shipLocations.remove(attackPos)){
						// sendTransmission (TransmissionType, userName, Content)
						sendTransmission(GAME_RESPONSE, SCREEN_NAME, tempPos+'h'); // h = hit
						System.out.println("Sending hit indicator"); // CHECK 
					} // remove the attacked location from t
					else{
						sendTransmission(GAME_RESPONSE, SCREEN_NAME, tempPos+'m'); // m = miss 
						System.out.println("Sending miss indicator"); // CHECK  
					}
				}
			}*/
			if (splitTrans[1].equals(GAME_RESPONSE) && splitTrans[3].equals(SCREEN_NAME)) {
				// Pos 2 = Username
				// Pos 3 = Content + hit/miss (h = hit; m = miss)
				// Pos 4 = GAME_ID 
				System.out.println("Game Response Detected");
				int length = splitTrans[2].length(); 
				String temp = splitTrans[2].substring(0,(length-1));
				System.out.println("SplitTrans[4] == "+splitTrans[2]);
				System.out.println("String 'temp' == "+ temp);
				
				int pos = Integer.parseInt(temp); 
				
				if (splitTrans[4].equals(gameID)) {
					System.out.println("ProcessTransmission.gameMove.thisGameID");
					// Change colors 
					// go through the arraylist, get the position (plus 1), 
					// and change the color to blue (if a miss) or red (if a hit)
					if(splitTrans[3].charAt((splitTrans[3].length()-1)) == ('h')) {
						//change color to red
						System.out.println("Inside Hit Block");
						board2.get(pos).setBackground(Color.RED);
						board2.get(pos).setOpaque(true);
						board2.get(pos).setEnabled(false);
						System.out.println("RED RED RED RED"); 
						
					} else { // it was a miss 
						// change color to blue 
						System.out.println("Inside Miss Block");
						board2.get(pos).setBackground(Color.BLUE);
						board2.get(pos).setOpaque(true); 
						board2.get(pos).setEnabled(false);
						System.out.println("BLUE BLUE BLUE BLUE");
					}
				}
			}
		}
	}

	/*
		Method to be called the processTransmission Method, at this point the transmission 
		has been determined to be a message, and associated actions should be taken 
		@author: Jason Nordheim 
		@version: 12/5/16 
		@param usr - the user sending the messag e
		@param msg - the message being sent by the user 
	*/ 
	public static void processMessage(String usr, String msg){
		appendChat(usr + ": " + msg);
	}
	
	public void addSimultaneousShip(int bow, int mid, int stern){
		bow--;
		mid--;
		stern--;

		final Color friendlyShipColor = Color.GRAY;
		//now we actually "place" the ship
		//re color my board
		board1.get(bow).setOpaque(true);//if the boxes are not set to be opaque, they will not change color.
		board1.get(bow).setBackground(friendlyShipColor);
		board1.get(mid).setOpaque(true);
		board1.get(mid).setBackground(friendlyShipColor);
		board1.get(stern).setOpaque(true);
		board1.get(stern).setBackground(friendlyShipColor);
		//TODO these values are still not currently stored anywhere, 
		//the change is purely aesthetic. so, figure that out soon. could 
		//probably just be done in an int[] containing indexes of valid ships.
		shipLocations.add(bow);
		shipLocations.add(mid);
		shipLocations.add(stern);
	}
	
	public boolean checkValidLocation(int bow, int mid, int stern){
		boolean goodMove = true;
		bow--;
		mid--;
		stern--;
		int sternCol = stern%8;
		int sternRow = stern/8;
		int midCol = mid%8;
		int midRow = mid/8;
		int bowCol = bow%8;
		int bowRow = bow/8;
		
		if(    (bow < 0 || bow > 63)||
		(mid < 0 || mid > 63)||
		(stern < 0 || stern > 63)){
			goodMove =  false;
		}

		if(goodMove && sternCol == midCol && midCol == bowCol){
			if(sternRow == midRow+1){
				if(midRow == bowRow +1){ }
					else { goodMove = false;}
			} else if(sternRow == midRow-1){
				if(midRow == bowRow - 1){ }
					else{ goodMove = false;}
			} else{ goodMove = false; }
		}
		else if(goodMove && sternRow == midRow && midRow == bowRow){
			if(sternCol == midCol + 1){
				if(midCol == bowCol + 1){ }
				else { goodMove = false; }
			} else if(sternCol == midCol - 1){
				if(midCol == bowCol - 1){ }
					else{ goodMove = false;}
			} else{ goodMove = false; }    
		} else{ if(goodMove){ goodMove = false;} }
		return goodMove;
	}
	
	/*
		Method to process game response to a move 
	*/ 
	public void processGameResponse(int target){
		
	}
	
	/*
		Method to process a status change 
	*/ 
	public void processStatusChange(){
		
	}
	
	/*
		Method to update the displayed userlist of connected clients 
			Appends userList (JTextArea) with users 
		@author: Jason Nordheim 
		@version: 12/6/16 
		@param: null 
		@return: void 
	*/ 
	public static void updateUserList(){
		// clear the user list box 
		System.out.println("updating user list");
		userList.setText(null);
		// iterate through the list of connected user 
		// and add them to the JTextarea
		for (String s: connectedUserList) {
			userList.append(s + "\n");
		}
	}
	
	/*
		ActionListener to be added to each position on the grid. 
		
		// ToDo: 
		- Disable the botton after (idoit-proof)
		- Send move 
		
	*/
	public void actionPerformed(ActionEvent ae) {
		// o is the object that was clicked 
		Object o = ae.getSource(); 
		JButton jb; 
		String btnText = null;
		
		try {
			jb = (JButton)o; 
			btnText = jb.getText();
		} catch (Exception e) {
			System.out.println("Unable to cast Object to JButton"); 
		}

		// Sends (1) GameMove indicator, indicating that the transmission is a game move 
		// Sends (2) the buttons text [btnText] (identifying the location to be attacked)
		// Sends (2) the SCREEN_NAME of the person attacking 
		sendTransmission(ATTACK_INDICATOR,MY_IP+REGEX+OPPONENT_IP+REGEX+btnText, SCREEN_NAME); 
		// NOTE: That location will be 1 greater than ArrayList location 
	}
		
	
	/*
		Main Method - launches application 
	*/ 
	public static void main(String[] args) {
		GUI test = new GUI();
	}
	
	
	
	public class ConnectListener implements ActionListener {
		public ConnectListener(){}
		
		public void actionPerformed(ActionEvent ae) {
			// pull in values from text boxes 
				String tempIP, tempPort, tempSN; 
				tempIP = ipAddressTextField.getText(); 
				tempPort = portTextField.getText(); 
				tempSN = userNameTextField.getText(); 
				
				/* FOR TESTING PURPOSES
				System.out.println("IP Address: " + tempIP
					+ "\n" + "Port: " + tempPort
					+ "\n" + "Screen Name: " + tempSN);
				*/                 
				
				
				if (tempIP.equals("")) {
					JOptionPane.showMessageDialog(null,
						 "Please enter an IP Address", 
						"Error", JOptionPane.ERROR_MESSAGE);
				} else if (tempPort.equals("")) {
					JOptionPane.showMessageDialog(null, 
						" Please enter a Port Number", 
						"Error", JOptionPane.ERROR_MESSAGE);
				} else if (tempSN.equals("")) {
					JOptionPane.showMessageDialog(null, 
						"Please enter a Screen Name", 
						"Error", JOptionPane.ERROR_MESSAGE);
				} else if (tempIP.length() < 5) {
					JOptionPane.showMessageDialog(null, 
						"Invalid IP Address Entered\n\nPlease enter a valid IP address", 
						"Error", JOptionPane.ERROR_MESSAGE);
				} else if (tempPort.length() < 3) {
					JOptionPane.showMessageDialog(null, 
					"Invalid Port Number Entered\n\nPlease enter a valid Port Number", 
					"Error", JOptionPane.ERROR_MESSAGE);
				} else if (tempSN.length() < 3) {
					JOptionPane.showMessageDialog(null, 
					"Invalid Screen Name entered\n\nPlease enter a screen name that" + 
					"is greater than 3 characters", 
					"Error", JOptionPane.ERROR_MESSAGE);
				} else {
					// INPUTS ASSUMED TO BE VALID 
					ipAddressTextField.setEditable(false);
					portTextField.setEditable(false);
					userNameTextField.setEditable(false);
					connectBtn.setEnabled(false);
					messageTextField.setEditable(true);
					
					IP_ADDRESS = tempIP; 
					PORT = tempPort; 
					SCREEN_NAME = tempSN; 
					
					// FORM THE CONNECTION 
					try {
						createConnection();
					} catch (Exception e) {
						System.out.println("Connection to Server could not be created.");
						e.getMessage();
						e.printStackTrace();
				}
			}
		}
	}
	
	
	
	
	
	
	
	public class PlaceShipsListener implements ActionListener {
		
		public PlaceShipsListener(){}
		
		public void actionPerformed(ActionEvent ae){
			for (JButton b: board2) {
					b.setEnabled(true);
				}
				
				boolean validShips = true;
				
				// default ship position to impossible position 
				// of negative 1 
				int s1Bow = -1; 
				int s1Mid = -1;                 
				int s1Stern = -1; 
				
				int s2Bow = -1;
				int s2Mid = -1;
				int s2Stern = -1; 
				
				int s3Bow = -1; 
				int s3Mid = -1; 
				int s3Stern = -1;  
				
				try{
					s1Bow = Integer.parseInt(ship1bow.getText().trim());
					//System.out.println("S1 BOW:" + s1Bow);
					s1Mid = Integer.parseInt(ship1mid.getText().trim());
					//System.out.println("S1 MID:" + s1Mid);
					s1Stern = Integer.parseInt(ship1stern.getText().trim());
					//System.out.println("S1 STERN:" + s1Stern);
					
					s2Bow = Integer.parseInt(ship2bow.getText().trim());
					//System.out.println("S2 BOW:" + s2Bow);
					s2Mid = Integer.parseInt(ship2mid.getText().trim());
					//System.out.println("S2 MID:" + s2Mid);
					s2Stern = Integer.parseInt(ship2stern.getText().trim());
					//System.out.println("S1 STERN:" + s2Stern);
					
					s3Bow = Integer.parseInt(ship3bow.getText().trim());
					//System.out.println("S3 BOW:" + s3Bow);
					s3Mid = Integer.parseInt(ship3mid.getText().trim());
					//System.out.println("S3 MID:" + s3Mid);
					s3Stern = Integer.parseInt(ship3stern.getText().trim());
					//System.out.println("S3 STERN:" + s3Bow);
					
					
					if(validShips && checkValidLocation(s1Bow, s1Mid, s1Stern)
					&& checkValidLocation(s2Bow, s2Mid, s2Stern)
					&& checkValidLocation(s3Bow, s3Mid, s3Stern)){
						addSimultaneousShip(s1Bow, s1Mid, s1Stern);
						addSimultaneousShip(s2Bow, s2Mid, s2Stern);
						addSimultaneousShip(s3Bow, s3Mid, s3Stern);
					}
					else {
						createErrorMessage("Ship Placement Error: placements must be between 1 and 63, and must be in a horizontal or vertical line");
					}
				} catch (Exception e){
					createErrorMessage("Ship Placement Error: All values must be supplied, all must be numeric");
				}
			
				
				//TODO catch exceptions thrown by somebody putting in a non numeric value here. 
				// and trying to Integer.parseInt().
				ArrayList<Integer> locs = new ArrayList<Integer>();
				locs.add(s1Bow);
				locs.add(s1Mid);
				locs.add(s1Stern);
				locs.add(s2Bow);
				locs.add(s2Mid);
				locs.add(s2Stern);
				locs.add(s3Bow);
				locs.add(s3Mid);
				locs.add(s3Stern);
				for (int i = 0; i < 9; i++) {//checks to see if any ships overlap
					if(locs.indexOf(locs.get(i)) != i){
						validShips = false;
						System.out.println("Ships overlap");
					}
					if(locs.get(i) > 64 || locs.get(i) < 1){//ensures that all ships are in bounds
						System.out.println("Ships out of bounds");
						validShips = false;
					}
				}
				
				if(validShips && checkValidLocation(s1Bow, s1Mid, s1Stern)
				&& checkValidLocation(s2Bow, s2Mid, s2Stern)
				&& checkValidLocation(s3Bow, s3Mid, s3Stern)){
					addSimultaneousShip(s1Bow, s1Mid, s1Stern);
					addSimultaneousShip(s2Bow, s2Mid, s2Stern);
					addSimultaneousShip(s3Bow, s3Mid, s3Stern);
			}
		}
	}
}