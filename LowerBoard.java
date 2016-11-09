/**
*	@author Nick Kowalczuk
*	@version 10-18-18
*	Second version of the LowerBoard class. Many methods are copied directly.
*/
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.LineBorder;
import java.util.*;
//import javax.xml.stream.*;
//import javax.xml.bind.annotation.adapters.*; //I don't think these are necessary.

/**
*	@author Nick
*	@version 10-1-16
*	creates a new LowerBoard object.
*/
public class LowerBoard extends JFrame implements ActionListener{
	private String title;
	private JPanel lowBoard;
	private boolean [][] locations; // will store the locations of ships, true == ship.
	private JPanel uInput;
	private JTextField shipLoc1;
	private JTextField shipLoc2;
	private JTextField shipLoc3;
	private JPanel [][] boxes = new JPanel[7][7];
	private int numShips;
	private boolean readyStatus;
	private JLabel instructions;
	
	private JButton entry;
	private JButton clear;
	private JButton submit;//have the main class implement action listener, which will be the action listener for this button.
	
	private ArrayList< JPanel > panels = new ArrayList< JPanel >();
	private JPanel p00 = new JPanel();
	private JPanel p01 = new JPanel();
	private JPanel p02 = new JPanel();
	private JPanel p03 = new JPanel();
	private JPanel p04 = new JPanel();
	private JPanel p05 = new JPanel();
	private JPanel p06 = new JPanel();
	private JPanel p07 = new JPanel();
	private JPanel p10 = new JPanel();
	private JPanel p11 = new JPanel();
	private JPanel p12 = new JPanel();
	private JPanel p13 = new JPanel();
	private JPanel p14 = new JPanel();
	private JPanel p15 = new JPanel();
	private JPanel p16 = new JPanel();
	private JPanel p17 = new JPanel();
	private JPanel p20 = new JPanel();
	private JPanel p21 = new JPanel();
	private JPanel p22 = new JPanel();
	private JPanel p23 = new JPanel();
	private JPanel p24 = new JPanel();
	private JPanel p25 = new JPanel();
	private JPanel p26 = new JPanel();
	private JPanel p27 = new JPanel();
	private JPanel p30 = new JPanel();
	private JPanel p31 = new JPanel();
	private JPanel p32 = new JPanel();
	private JPanel p33 = new JPanel();
	private JPanel p34 = new JPanel();
	private JPanel p35 = new JPanel();
	private JPanel p36 = new JPanel();
	private JPanel p37 = new JPanel();
	private JPanel p40 = new JPanel();
	private JPanel p41 = new JPanel();
	private JPanel p42 = new JPanel();
	private JPanel p43 = new JPanel();
	private JPanel p44 = new JPanel();
	private JPanel p45 = new JPanel();
	private JPanel p46 = new JPanel();
	private JPanel p47 = new JPanel();
	private JPanel p50 = new JPanel();
	private JPanel p51 = new JPanel();
	private JPanel p52 = new JPanel();
	private JPanel p53 = new JPanel();
	private JPanel p54 = new JPanel();
	private JPanel p55 = new JPanel();
	private JPanel p56 = new JPanel();
	private JPanel p57 = new JPanel();
	private JPanel p60 = new JPanel();
	private JPanel p61 = new JPanel();
	private JPanel p62 = new JPanel();
	private JPanel p63 = new JPanel();
	private JPanel p64 = new JPanel();
	private JPanel p65 = new JPanel();
	private JPanel p66 = new JPanel();
	private JPanel p67 = new JPanel();
	private JPanel p70 = new JPanel();
	private JPanel p71 = new JPanel();
	private JPanel p72 = new JPanel();
	private JPanel p73 = new JPanel();
	private JPanel p74 = new JPanel();
	private JPanel p75 = new JPanel();
	private JPanel p76 = new JPanel();
	private JPanel p77 = new JPanel();
	
	public LowerBoard(String player){
		JPanel board = new JPanel(new GridLayout(8,8));
		title = player;
		readyStatus = false;
		locations = new boolean[][]{
			{false, false, false, false, false, false, false, false},
			{false, false, false, false, false, false, false, false},
			{false, false, false, false, false, false, false, false},
			{false, false, false, false, false, false, false, false},
			{false, false, false, false, false, false, false, false},
			{false, false, false, false, false, false, false, false},
			{false, false, false, false, false, false, false, false},
			{false, false, false, false, false, false, false, false},
		};
		
		panels.add(p00);//1
		panels.add(p01);
		panels.add(p02);
		panels.add(p03);
		panels.add(p04);//5
		panels.add(p05);
		panels.add(p06);
		panels.add(p07);
		panels.add(p10);
		panels.add(p11);//10
		panels.add(p12);
		panels.add(p13);
		panels.add(p14);
		panels.add(p15);
		panels.add(p16);//15
		panels.add(p17);
		panels.add(p20);
		panels.add(p21);
		panels.add(p22);
		panels.add(p23);//20
		panels.add(p24);
		panels.add(p25);
		panels.add(p26);
		panels.add(p27);
		panels.add(p30);//25
		panels.add(p31);
		panels.add(p32);
		panels.add(p33);
		panels.add(p34);
		panels.add(p35);//30
		panels.add(p36);
		panels.add(p37);
		panels.add(p40);
		panels.add(p41);
		panels.add(p42);//35
		panels.add(p43);
		panels.add(p44);
		panels.add(p45);
		panels.add(p46);
		panels.add(p47);//40
		panels.add(p50);
		panels.add(p51);
		panels.add(p52);
		panels.add(p53);
		panels.add(p54);
		panels.add(p55);
		panels.add(p56);
		panels.add(p57);
		panels.add(p60);
		panels.add(p61);
		panels.add(p62);
		panels.add(p63);
		panels.add(p64);
		panels.add(p65);
		panels.add(p66);
		panels.add(p67);
		panels.add(p70);
		panels.add(p71);
		panels.add(p72);
		panels.add(p73);
		panels.add(p74);
		panels.add(p75);
		panels.add(p76);
		panels.add(p77);
		
		for(int i = 0; i < 64; i++){
			panels.get(i).setBorder(LineBorder.createBlackLineBorder());
			board.add(panels.get(i));	
			panels.get(i).add(new JLabel(Integer.toString(i)));
		}
		
		instructions = new JLabel("Enter ship locations");
		clear = new JButton("Reset");
		entry = new JButton("Add");
		submit = new JButton("Submit");
		shipLoc1 = new JTextField(5);
		shipLoc2 = new JTextField(5);
		shipLoc3 = new JTextField(5);
		JPanel reset = new JPanel(new GridLayout(4,2));
		reset.add(shipLoc1);
		reset.add(clear);
		reset.add(shipLoc2);
		reset.add(submit);
		reset.add(shipLoc3);
		reset.add(entry);
		reset.add(instructions);
		entry.addActionListener(this);
		clear.addActionListener(this);
		submit.addActionListener(this);

		add(board, BorderLayout.CENTER);
		add(reset, BorderLayout.SOUTH);
		
		setTitle(player);
		setVisible(true);
		setSize(300,400);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);//should only be enabled during testing
		setResizable(false);
	}
	/*
	*	@author Nick Kowalczuk
	*	@version 10-17-16
	*	@param stern, the end of the ship
	*	@param mid, the middle of the ship
	*	@param bow, the opposite end of the ship
	*	@return true if the add was successful, false otherwise.
	*	This method will take three ints at the same time, see if the represent a horizontal or vertical line, and add a ship.
	*	The ship will be added to the board (GUI) and to the array of location data.
	*	The method will not allow a ship to be added if int params do not represent a straight horizontal or vertical line,
	*	if they are not adjacent
	*	if they are not present on the board (not between 0 and 63(inclusive)),
	*	if any of the indexes are already occupied,
	*	or if three ships have already been created.
	*/
	public boolean addSimultaneousShip(int stern, int mid, int bow){
		boolean goodMove = true;
		if(numShips > 2){
			goodMove = false;
			instructions.setText("Too Many Ships");
		}
		Color shipColor;
		switch(numShips){
			case 0:	shipColor = Color.RED;
					break;
			case 1:	shipColor = Color.BLUE;
					break;
			case 2:	shipColor = Color.GREEN;
					break;
			default: shipColor =Color.BLACK;
					break;
		}
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
			instructions.setText("Invalid Location");
		}

		if(goodMove && sternCol == midCol && midCol == bowCol){
			if(sternRow == midRow+1){
				if(midRow == bowRow +1){
					
				}
				else{
					goodMove = false;
					instructions.setText("Improper alignement");
				}
			}
			else if(sternRow == midRow-1){
				if(midRow == bowRow - 1){
					
				}
				else{
					goodMove = false;
					instructions.setText("Improper alignement");
				}
			}
			else{
				goodMove = false;
				instructions.setText("Improper alignemnt");
			}
		}
		else if(goodMove && sternRow == midRow && midRow == bowRow){
			if(sternCol == midCol + 1){
				if(midCol == bowCol + 1){
					System.out.println("chac");
				}
				else {
					goodMove = false;
					instructions.setText("Improper alignement");
				}
			}
			else if(sternCol == midCol - 1){
				if(midCol == bowCol - 1){
					
				}
				else{
					goodMove = false;
					instructions.setText("Improper alignement");
				}
			}
			else{
				goodMove = false;
				instructions.setText("Improper alignment");
			}	
		}
		else{
			if(goodMove){
				instructions.setText("Improper Alignment");
				goodMove = false;
			}
		}
		
		if(goodMove&&(getLocStat(bow)||getLocStat(mid)||getLocStat(stern))){
			goodMove = false;
			instructions.setText("Space Unavailable");
		}
		
		if(goodMove){
			instructions.setText("Location Accepted");
			addLocation(bow);
			addLocation(mid);
			addLocation(stern);
			
			panels.get(bow).setBackground(shipColor);
			panels.get(mid).setBackground(shipColor);
			panels.get(stern).setBackground(shipColor);
			
			numShips++;
		}
		return(goodMove);
	}
	
	/*
	*	@author Nick Kowalczuk
	*	@version 10-15-16
	*	@param t an int, the number of the panel. should be the same as the number that appears in the gui, method will parse the row and column therefrom.
	*	changes the status of the given location to true. if the location is already true, nothing happens
	*/
	public void addLocation(int t){
		int c = t % 8;
		int r = t / 8;
		locations[r][c] = true;
	}
	/*
	*	@author Nick Kowalczuk
	*	@version 10-15-16
	*	@param t an int, the number of the panel as it appears in gui. Mehtod will parse the row and column therefrom.
	*	@return the boolean value of the location. 
	*	returns true if that value is already occupied. false otherwise
	*/
	public boolean getLocStat(int t){
		int c = t % 8;
		int r = t / 8;
		return locations[r][c];
	}
	/*
	*	@author Nick Kowalczuk
	*	@version 10-15-16
	*	prints the status of each index in locations[][]
	*/
	public void printLocation(){
		for(int r = 0; r < locations.length; r++){
			for(int c = 0; c < locations[r].length; c++){
				if(true){
					System.out.println("Location["+r+"]["+c+"] == "+locations[r][c]);	
				}
			}
		}
	}
	/*
	*	@author Nick Kowalczuk
	*	@version 10-16-16
	*	determines the source of the action, then executes the appropriate methods
	*/
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("Add")){
			addSimultaneousShip(Integer.parseInt(shipLoc1.getText()), Integer.parseInt(shipLoc2.getText()), Integer.parseInt(shipLoc3.getText()));
		}
		else if(e.getActionCommand().equals("Reset")){
			resetBoard();
			instructions.setText("Board: Reset");
		}
		else if(e.getActionCommand().equals("Submit")){
			if(numShips == 3){
				readyStatus = true;
				entry.setEnabled(false);
				clear.setEnabled(false);
				instructions.setText("Board: Ready");
				UpperBoard up = new UpperBoard(locations, title);
			}
			else{
				instructions.setText("Submission failed");
			}
		}
		else{
			System.out.println(e.getActionCommand());
			instructions.setText("COMMAND NOT RECOGNIZED");
		}
	}
	
	/**
	*	@author Nick Kowalczuk
	*	@version 10-18-16
	*	@return true if the user has clicked the submit button.
	*	used to determine if the user is ready to submit their ships.
	*/
	public boolean getReadyStatus(){
		return readyStatus;
	}
	
	/*
	*	@author Nick Kowalczuk
	*	@version 10-16-16
	*	resets the board, the array containing the locations data, and the count of ships
	*	is invoked by clicking the "Reset" button on the gui
	*/
	public void resetBoard(){
		numShips = 0;
		for(int r = 0; r < locations.length; r++){
			for(int c = 0; c < locations[r].length; c++){
				locations[r][c] = false;
			}
		}
		for(int i = 0; i < 64; i++){
			panels.get(i).setBackground(Color.WHITE);
		}
	}
	
	/*
	*	@author Nick Kowalczuk
	*	@version 10-17-16
	*	@return returns numShips, the number of ships currently on the board
	*	will return the number of ships currently on the board.
	*/
	public int getNumShips(){
		return(numShips);
	}

	/**
	*	@author Nick Kowalczuk
	*	@version 10-18-16
	*	Closes this window, will be invoked by the main method of the BattleBoats class after submission is complete.
	*/
	
	public void close(){
		System.exit(0);
	}
}