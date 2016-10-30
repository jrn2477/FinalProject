import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
	
	JFrame frame; 
	JMenu fileMenu; 
	JMenuItem helpMenuItem, exitMenuItem; 
	JMenuBar menuBar; 
	JPanel mainPanel, southPanel, northPanel, connectionPanel; 
	JButton sendMessageButton, connectButton;
	JLabel nameFieldLabel, ipAddressFieldLabel, portNumFieldLabel, messageLabel; 
	JTextField ipAddressField, nameField, portNumField; 
	JTextArea chatArea;  
	
	public static void main(String[] args) {
		GUI g1 = new GUI(); 
	}
	
	public GUI() {
		
	}
	
	
	/*
	* Method to intitialize components 
	*/ 
	public void init(){
		
		frame = new JFrame(); 
		
		mainPanel = new JPanel(new BorderLayout());
		northPanel = new JPanel(new GridLayout(1,2));
		connectionPanel = new JPanel(new GridLayout(3,2));
		southPanel = new JPanel(new GridLayout(1,2));
		
		sendMessageButton = new JButton("Send Message");
		sendMessageButton.enable(false);
			// TODO: Add actionListener 
		connectButton = new JButton("Connect"); 
			// TODO: Add actionListener 
		
		helpMenuItem = new JMenuItem("Help");
			// TODO: Add actionListener 
		exitMenuItem = new JMenuItem("Exit");
			// TODO: Add actionListener 
		
		nameFieldLabel = new JLabel("Name: "); 
		ipAddressFieldLabel = new JLabel("IP Address"); 
		portNumFieldLabel = new JLabel("Port Number:"); 
		messageLabel = new JLabel("Message:");
		
		ipAddressField = new JTextField(15); 
		nameField = new JTextField(15);
		portNumField = new JTextField(5); 
		
		/*
			CODE TO SETUP LAYOUT 
		*/ 
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	}
	
}