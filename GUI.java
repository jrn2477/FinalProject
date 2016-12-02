import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.awt.*;

/*
	@author: Jason Norhdeim 
	@version: 11/30/16 v1 
	@decription: Class responsible for the visual interface of the client 
*/ 
public class GUI extends JFrame{
	
	private static final String YOUR_BOARD = "Your Board"; 
	private static final String OPP_BOARD = "Opponents Board"; 
	private ArrayList<JPanel> board1 = new ArrayList<JPanel>(); 
	private ArrayList<JPanel> board2 = new ArrayList<JPanel>(); 
	private JPanel myBoardPanel, oppBoardPanel, boardsPanel, gamePanel, gameBoard1, gameBoard2, 
		chatPanel, shipPositionsPanel, connectionPanel; 
	private JTextArea chatTextArea, userList; 
	private JTextField messageTextField, ipAddressTextField, portTextField, userNameTextField; 
	private JButton sendMessageBtn, connectBtn, placeshipsBtn; 
 
	
	/*
		Default Constructor 
		@author: Jason Nordheim 
		@version: 11/20/16 
	*/ 
	public GUI(){
		setTitle("Let's Play Battleship"); 
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
		ship1bow.setEditable(false); 
		JTextField ship2bow = new JTextField(2);
		ship2bow.setEditable(false);
		JTextField ship3bow = new JTextField(2);
		ship3bow.setEditable(false);
		
		// Ship 2 Textfields 
		JTextField ship1mid = new JTextField(2);
		ship1mid.setEditable(false); 
		JTextField ship2mid = new JTextField(2);
		ship2mid.setEditable(false); 
		JTextField ship3mid = new JTextField(2);
		ship3mid.setEditable(false);	
		
		// ship 3 textfields 
		JTextField ship1stern = new JTextField(2);
		ship1stern.setEditable(false); 
		JTextField ship2stern = new JTextField(2);
		ship2stern.setEditable(false); 
		JTextField ship3stern = new JTextField(2);
		ship3stern.setEditable(false);	
		
		// place ships button
		placeshipsBtn = new JButton("Place Ships!"); 
		placeshipsBtn.setHorizontalAlignment(JButton.CENTER); 	
		placeshipsBtn.setEnabled(false);
		
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
		//TODO: Add ActionListener
		
		//set up panel 
		chatPanel = new JPanel(new BorderLayout(5,5)); 
		chatPanel.add(chatTextArea, BorderLayout.CENTER); 
		
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
		@author: Jason Norhdeim 
		@version: 11/29/16 
		@description: Method to create a battlefield board. 
		@param: ArrayList which will store each position panel 
	*/ 
	public JPanel makeBoard(ArrayList<JPanel> aryLst){
		JPanel board = new JPanel(new GridLayout(8,8)); 
		for (int i = 1; i < 65; i++) {
			JPanel p = new JPanel(); 
			aryLst.add(p); 
			aryLst.get(i-1).setSize(80,80); 
			aryLst.get(i-1).setBorder(LineBorder.createBlackLineBorder());
			aryLst.get(i-1).add(new JLabel(Integer.toString(i))); 
			board.add(aryLst.get(i-1)); 
		}
		return board; 
	}
	
	public static void main(String[] args) {
		new GUI();	
	}
	
}