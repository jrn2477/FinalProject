import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane.*;
import java.net.*;
import java.io.*;

/*
	@author: Team Flying Mongooses (Nick Koowalcuk, Lauren Hoffman, Jason Nordheim) 
	@version: 11/30/16 v1 
	@decription: Class responsible for the visual interface of the client 
*/ 
public class GUI extends JFrame implements ActionListener{
	
	private static String IP_ADDRESS, PORT, SCREEN_NAME; 
	private static final String YOUR_BOARD = "Your Board"; 
	private static final String REGEX = "_h3ll_"; 
	private static final String MESSAGE_INDICATOR = "M"; 
	private static final String ATTACK_INDICATOR = "A";
	private static final String STATUS_INDICATOR = "S";
	private static final String GAME_PLACEMENT_INDICATOR = "GP";
	private static final String GAME_MOVE = "GM"; 
	private static final String GAME_RESPONSE  = "GR"; 
	
	private static final String OPP_BOARD = "Opponents Board"; 
	private ArrayList<JButton> board1 = new ArrayList<JButton>(); 
	private ArrayList<JButton> board2 = new ArrayList<JButton>(); 
	private JPanel myBoardPanel, oppBoardPanel, boardsPanel, gamePanel, gameBoard1, gameBoard2, 
		chatPanel, shipPositionsPanel, connectionPanel; 
	private static JTextArea chatTextArea, userList; 
	private JTextField messageTextField, ipAddressTextField, portTextField, userNameTextField; 
	private JButton sendMessageBtn, connectBtn, placeshipsBtn; 
 	private static Socket socket;
	private static BufferedReader br; 
	private static PrintWriter pw; 
	private static boolean connected;
	private static Vector<ChatReader> chatReaders = new Vector<ChatReader>();
	public static ArrayList<String> connectedUserList = new ArrayList<String>();
	//private static int gameID;//will indicate which game the user is in
	
	private int[] shipLocations;
	
	/*
		Default Constructor 
		@author: Jason Nordheim 
		@version: 11/20/16 
	*/ 
	public GUI(){
		//Listener listen = new Listener(); 
		setTitle("Let's Play Battleship"); 
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
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
		JTextField ship1bow = new JTextField(2);
		//ship1bow.setEditable(false); 
		JTextField ship2bow = new JTextField(2);
		//ship2bow.setEditable(false);
		JTextField ship3bow = new JTextField(2);
		//ship3bow.setEditable(false);
		
		// Ship 2 Textfields 
		JTextField ship1mid = new JTextField(2);
		//ship1mid.setEditable(false); 
		JTextField ship2mid = new JTextField(2);
		//ship2mid.setEditable(false); 
		JTextField ship3mid = new JTextField(2);
		//ship3mid.setEditable(false);	
		
		// ship 3 textfields 
		JTextField ship1stern = new JTextField(2);
		//ship1stern.setEditable(false); 
		JTextField ship2stern = new JTextField(2);
		//ship2stern.setEditable(false); 
		JTextField ship3stern = new JTextField(2);
		//ship3stern.setEditable(false);	//TODO uncomment these when out of development
		
		// place ships button
		placeshipsBtn = new JButton("Place Ships!");
		placeshipsBtn.setHorizontalAlignment(JButton.CENTER); 	
		//placeshipsBtn.setEnabled(false);
		placeshipsBtn.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent ae){
				System.out.println("you pressed that button");
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
				
				// Check ship 1 positioning 
				try {
					s1Bow = Integer.parseInt(ship1bow.getText());
					s1Mid = Integer.parseInt(ship1mid.getText());
					s1Stern = Integer.parseInt(ship1stern.getText());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Invalid Input for Ship 1",
						 "Ship Placement Error", JOptionPane.ERROR_MESSAGE);
				}
				
				// Check ship 2 positioning 
				try {
					s2Bow = Integer.parseInt(ship2bow.getText());
					s2Mid = Integer.parseInt(ship2mid.getText());
					s2Stern = Integer.parseInt(ship2stern.getText());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Invalid Input for Ship 2", 
						"Ship Placement Error", JOptionPane.ERROR_MESSAGE);
				}
				
				// Check ship 3 positioning 
				try {
					s3Bow = Integer.parseInt(ship3bow.getText());
					s1Mid = Integer.parseInt(ship1mid.getText());
					s1Stern = Integer.parseInt(ship1stern.getText());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Invalid Input for Ship 3", 
						"Ship Placement", JOptionPane.ERROR_MESSAGE);
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
						System.out.println("ships overlap; get fucked");
					}
					if(locs.get(i) > 64 || locs.get(i) < 1){//ensures that all ships are in bounds
						System.out.println("ships out of bounds; get fucked");
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
		});
		
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
		connectBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
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
		});
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
				// Compose transmission 
				String transmission = ("M" + messageTextField.getText() + userNameTextField.getText());
				sendTransmission("M",messageTextField.getText(), userNameTextField.getText());	
				// writeConsole(transmission);
			}
		});
		
		//set up chat panel 
		chatPanel = new JPanel(new BorderLayout(5,5)); 
		chatPanel.add(chatTextArea, BorderLayout.CENTER); 
		
		// set up messaging panel 
		JPanel sendMessagePanel = new JPanel(new FlowLayout()); 
		sendMessagePanel.add(messageTextField); 
		sendMessagePanel.add(sendMessageBtn); 
		chatPanel.add(sendMessagePanel, BorderLayout.SOUTH);
		chatPanel.add(connectionPanel, BorderLayout.NORTH);
		chatPanel.add(userList, BorderLayout.WEST);
		
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
	public void sendTransmission(String transmissionType, String transmissionContent, String userName) {
		try {
			// Create connection
			socket = new Socket(IP_ADDRESS,Integer.parseInt(PORT)); 
			
			// Create Output Streams
			PrintWriter pw = new PrintWriter(
				new OutputStreamWriter(socket.getOutputStream())); 
				
			String regex = REGEX;
			// TESTING PURPOSES:
			// System.out.println("PW: " + pw.toString());
			pw.println(regex + transmissionType + regex + userName + regex + transmissionContent);
			pw.flush();
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
	}
	
	public void writeConsole(String s){
		System.out.println(s);
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
			
			board.add(aryLst.get(i-1)); 
		}
		return board; 
	}
	
	public static void appendChat(String s) {
		chatTextArea.append(s + "\n"); 
	}
	
	
//	public class Listener implements ActionListener {
//		
//		public Listener(){
//			
//		}
//		
//		public void actionPerformed(ActionEvent ae) {
//			Object o = ae.getSource(); 
//			
//			// If it is a hit 
//			JPanel p = o; 
//			p.setBackground(Color.RED); 
//		}
//	}

	
	
	
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

			int i = 0; 
			
			connected = true;
			
			while (connected) {
				pullFromServer();
				System.out.println(i); 
				try	{
					this.sleep(100);
				} catch (Exception e) {
					e.getMessage();
					e.printStackTrace();
				}
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
				System.out.println(transmission);
				
				//  ADD CODE FOR TRANSMISSION PROCESSING 
				processTransmission(transmission);
								
				} catch (Exception e) {
				e.getMessage(); 
				e.printStackTrace();
			}	
		}
		
	}//end ChatReader 
	
	/* 
		Method to Process Transmissions from the server, the method will parse 
		the transmission to determine the type of transmission before processing 
		@author: Jason Nordheim 
		@param: trans - the transmission recieved from the server 
		@version: 12/5/16
	*/ 
	//Why is this static? -Nick
	public static void processTransmission(String trans) {
		
		if (trans.startsWith(REGEX)) {
			String[] splitTrans = trans.split(REGEX); 
			
			if (splitTrans[1].equals(MESSAGE_INDICATOR)) {//changed this to constant value as declared above -Nick
				// Transmission is a message 
				processMessage(splitTrans[2], splitTrans[3]);
			}
			else if(splitTrans[1].equals(GAME_PLACEMENT_INDICATOR)){//-Nick
				//gameID = Integer.parseInt(splitTrans[2]);//this SHOULD contain the new game ID
				//TODO make sure that it still works when gameID is static, which I had to do to make this method compile.
				//we could get around this if we made the game ID an attribute of the thread rather than of the GUI.java.8
				//nevermind, it all has to be static due to the way that the thing checks for messages.
			}
		}
		
		if (trans.startsWith("_N3WUS3R_")) {
			// get username 
			int startPos = ("_N3WUS3R_").length();
			String nameList = trans.substring(startPos); 
			
			System.out.println("Name List: " + nameList); 
			
			String[] tempList = nameList.split("88888");
			
			for (int i = 0; i < tempList.length; i++) {
				connectedUserList.add(tempList[i]);
			}
			
			updateUserList();
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
		//TODO these values are still not currently stored anywhere, the change is purely aesthetic. so, figure that out soon. could probably just be done in an int[] containing indexes of valid ships.
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
		
		if(	(bow < 0 || bow > 63)||
		(mid < 0 || mid > 63)||
		(stern < 0 || stern > 63)){
			goodMove =  false;
		}

		if(goodMove && sternCol == midCol && midCol == bowCol){
			if(sternRow == midRow+1){
				if(midRow == bowRow +1){
					
				}
				else{
					goodMove = false;
				}
			}
			else if(sternRow == midRow-1){
				if(midRow == bowRow - 1){
					
				}
				else{
					goodMove = false;
				}
			}
			else{
				goodMove = false;
			}
		}
		else if(goodMove && sternRow == midRow && midRow == bowRow){
			if(sternCol == midCol + 1){
				if(midCol == bowCol + 1){
				}
				else {
					goodMove = false;
				}
			}
			else if(sternCol == midCol - 1){
				if(midCol == bowCol - 1){
					
				}
				else{
					goodMove = false;
				}
			}
			else{
				goodMove = false;
			}	
		}
		else{
			if(goodMove){
				goodMove = false;
			}
		}
		return goodMove;
	}
	
	/*
		Method to process game move 
	*/ 
	public void processGameMove(){
		
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
		userList.setText(null);
		// iterate through the list of connected user 
		// and add them to the JTextarea
		for (String s: connectedUserList) {
			userList.append(s + "\n");
		}
	}
	
	
	public void actionPerformed(ActionEvent ae) {
		// o is the object that was clicked 
		Object o = ae.getSource(); 
		JButton jb; 
		
		try {
			jb = (JButton)o; 
		} catch (Exception e) {
			System.out.println("Unable to cast Object to JButton"); 
		}
		
		// Testing 1 2 3 
		// Testiing 456
		
		// NOTE: That location will be 1 greater than ArrayList location 
		sendTransmission(GAME_MOVE, jb.getText(), SCREEN_NAME); 
		

	}
		
	
	/*
		Main Method - launches application 
	*/ 
	public static void main(String[] args) {
		GUI test = new GUI();
	}
	
}