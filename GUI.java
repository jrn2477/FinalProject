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
	
	/**
	**
	** @return String; text inside the chat area
	**/
	public String getChatText(){
		return chatArea.getText();
	}
	
	/**
	** @param String - Text to be placed in the chat window
	** @return void 
	**/
	public void setChatText(String s) {
		chatArea.setText(s);
	}
	
	/**
	**
	** @return String - The message text 
	**/
	public String getSendMessageText() {
		return messageField.getText();
	}
	
	/**
	** @param String - Text to be placed in the message field 
	** @return void 
	**/
	public void setMessageText(String s) {
		messageField.setText(s);
	}
	
	/**
	** 
	** @return String - The Name of the user (could be void) 
	**/
	public String getNameText() {
		return nameField.getText();
	}
	
	/**
	** @param String - Text (the name of the player) to be set in the Name field 
	** @return void 
	**/
	public void setNameField(String s) {
		nameField.setText(s);
	}
	
	/**
	**
	** @return String - String of the IP Address 
	**/
	public String getIPAddress() {
		return ipAddressField.getText();
	}
	
	/**
	** @param String - Text to set the textfield of the IP address
	** @return void 
	**/
	public void setIPAddress(String s) {
		ipAddressField.setText(s);
	}
	
	/**
	**  
	** @return String  - the Port Number 
	**/
	public String getPort(){
		return portNumField.getText();
	}
	
	/**
	** @param String - The String to be placed into the port num 
	** @return void 
	**/
	public void setPort(String s) {
		portNumField.setText(s);
	}
	
	/**
	** @param String - Message to be appended to the chat window 
	** @return void 
	**/
	public void appendChat(String s) {
		chatArea.append(s);
	}
}