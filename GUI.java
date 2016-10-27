import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
	
	JFrame frame; 
	JPanel mainPanel; 
	JButton sendMessageButton, connectButton;
	JTextField ipTextField, targetPortTextField, recievePortTextField; 
	JTextArea chatArea; 
	
	public GUI() {
		
	}
	
	
	/*
	* Method to intitialize components 
	*/ 
	public init(){
		
		frame = new JFrame(); 
		mainPanel = new Panel(new BorderLayout());
		sendMessageButton = new JButton("Send Message"); 
			// ADD ACTIONLISTINER 
		connectButton = new JButton("Connect"); 
			// ADD ACTIONLISTENER 
		
		
		/*
			CODE TO SETUP LAYOUT 
		*/ 
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	}
	
}