import javax.swing.*;
import java.awt.*;

public class GUI {
	
	JFrame frame; 
	JMenu fileMenu; 
	JMenuItem helpMenuItem, exitMenuItem; 
	JMenuBar menuBar; 
	JPanel mainPanel, southPanel, northPanel, connectionPanel; 
	JButton sendMessageButton, connectButton;
	JLabel nameFieldLabel, ipAddressFieldLabel, portNumFieldLabel, messageLabel; 
	JTextField ipAddressField, nameField, portNumField, messageField; 
	JTextArea chatArea;  
	
	public static void main(String[] args) {
		GUI g1 = new GUI(); 
	}
	
	public GUI() {
		init();
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
		sendMessageButton.setEnabled(false);
			// TODO: Add actionListener 
		connectButton = new JButton("Connect"); 
		connectButton.setEnabled(false);
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
		
		chatArea = new JTextArea(15,20); // 15 rows; 20 columns 
		messageField = new JTextField(20); 
		
		
		connectionPanel.add(nameFieldLabel);
		connectionPanel.add(nameField); 
		connectionPanel.add(ipAddressFieldLabel); 
		connectionPanel.add(ipAddressField);
		connectionPanel.add(portNumFieldLabel); 
		connectionPanel.add(portNumField);
		
		northPanel.add(connectionPanel); 
		northPanel.add(connectButton); 
		
		southPanel.add(messageField); 
		southPanel.add(sendMessageButton); 
		
		mainPanel.add(northPanel, BorderLayout.NORTH); 
		mainPanel.add(chatArea, BorderLayout.CENTER); 
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		mainPanel.setVisible(true);
		
		/*
			CODE TO SETUP LAYOUT 
		*/ 
		
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}