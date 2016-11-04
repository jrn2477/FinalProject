import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.BindException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GUI extends Thread implements ActionListener {

    JFrame frame;
    JMenu fileMenu;
    JMenuItem helpMenuItem, exitMenuItem;
    JMenuBar menuBar;
    JPanel mainPanel, southPanel, northPanel, connectionPanel;
    JButton sendMessageButton, connectButton;
    JLabel nameFieldLabel, ipAddressFieldLabel, portNumFieldLabel, messageLabel;
    JTextField ipAddressField, nameField, portNumField, messageField;
    JTextArea chatArea;

    // Network Stuff
    DataOutputStream dos;
    BufferedReader dis;
    Socket sock;

    public static void main(String[] args) {
        GUI g1 = new GUI();
    }

    public GUI() {
        init();
    }


    /*
	* Method to intitialize components 
	*/
    public void init() {

        frame = new JFrame();

        mainPanel = new JPanel(new BorderLayout());
        northPanel = new JPanel(new GridLayout(1, 2));
        connectionPanel = new JPanel(new GridLayout(3, 2));
        southPanel = new JPanel(new GridLayout(1, 2));

        sendMessageButton = new JButton("Send Message");
        sendMessageButton.setEnabled(false);
        sendMessageButton.addActionListener(this);

        connectButton = new JButton("Connect");
        connectButton.setEnabled(true);
        connectButton.addActionListener(this);


        helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(this);

        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(this);

        nameFieldLabel = new JLabel("Name: ");
        ipAddressFieldLabel = new JLabel("IP Address");
        portNumFieldLabel = new JLabel("Port Number:");
        messageLabel = new JLabel("Message:");

        ipAddressField = new JTextField(15);
        nameField = new JTextField(15);
        portNumField = new JTextField(5);

        chatArea = new JTextArea(15, 20); // 15 rows; 20 columns
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

    public void connect() {
        try {
            sock = new Socket(getIPAddress(), 16798);
            dos = new DataOutputStream(sock.getOutputStream());
            dis = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            //do-while loop is better, nested loops are not necessary.
                dos.writeBytes("Welcome" + getNameText() + "to the server" + "\n");//writes it. the "\n" is necessary because a line is considered to be terminated by the BufferedReader (server-side) if it ends with a new line ("\n")
                dos.flush();//flushes, ensuring that the message is written
                //System.out.println();
            this.start();
        } catch (BindException be) {
            System.out.println("bind exception");
            be.printStackTrace();
        } catch (UnknownHostException uhe) {
            System.out.println("unknown host");
        } catch (IOException ioe) {
            System.out.println("ioe");
            ioe.printStackTrace();
        }

    }

    /**
     * *
     * * @return String; text inside the chat area
     **/
    public String getChatText() {
        return chatArea.getText();
    }

    /**
     * * @param String - Text to be placed in the chat window
     * * @return void
     **/
    public void setChatText(String s) {
        chatArea.setText(s);
    }

    /**
     * *
     * * @return String - The message text
     **/
    public String getSendMessageText() {
        return messageField.getText();
    }

    /**
     * * @param String - Text to be placed in the message field
     * * @return void
     **/
    public void setMessageText(String s) {
        messageField.setText(s);
    }

    /**
     * *
     * * @return String - The Name of the user (could be void)
     **/
    public String getNameText() {
        return nameField.getText();
    }

    /**
     * * @param String - Text (the name of the player) to be set in the Name field
     * * @return void
     **/
    public void setNameField(String s) {
        nameField.setText(s);
    }

    /**
     * *
     * * @return String - String of the IP Address
     **/
    public String getIPAddress() {
        return ipAddressField.getText();
    }

    /**
     * * @param String - Text to set the textfield of the IP address
     * * @return void
     **/
    public void setIPAddress(String s) {
        ipAddressField.setText(s);
    }

    /**
     * *
     * * @return String  - the Port Number
     **/
    public String getPort() {
        return portNumField.getText();
    }

    /**
     * * @param String - The String to be placed into the port num
     * * @return void
     **/
    public void setPort(String s) {
        portNumField.setText(s);
    }

    /**
     * * @param String - Message to be appended to the chat window
     * * @return void
     **/
    public void appendChat(String s) {
        chatArea.append(s);
    }


    public void receivedMessaged() {
        try{
            String message = dis.readLine();
            appendChat(message);
        } catch(IOException ie) {
            System.out.println("Hey something went wrong!");
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand() == "Connect") {

            if (getNameText().isEmpty()) {/**do nothing**/ System.out.println("I stopped here 1");} else if (getIPAddress().isEmpty()) { /**do nothing **/ System.out.println("I stopped here 2");} else {
                connect();
                appendChat("Your connected");
                connectButton.setEnabled(false);
                sendMessageButton.setEnabled(true);
            }

        }
        if (e.getActionCommand() == "Send Message") {
            try {
                String message;
                message = getSendMessageText();
                dos.writeBytes(message+"\n");
                dos.flush();
            } catch(IOException ie) {
                System.out.println("ooops all berries");
            }

        }
    }

    public void run(){
        String recieved;
        while (true){
            try {
                recieved = dis.readLine();
                if (recieved != null) {
                    appendChat(recieved+"\n");
                }
            } catch(IOException ie) {
                System.out.println("FUCKING WORK");
            }
        }
    }
}