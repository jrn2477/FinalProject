import java.net.*;
import java.io.*;
import javax.lang.model.element.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class myClient extends JFrame implements ActionListener{
	JTextArea incoming;
	JTextField outgoing;
	JButton send;
	
	public myClient(){
		JPanel display = new JPanel(new FlowLayout());
		JPanel input = new JPanel(new FlowLayout());
		

		incoming = new JTextArea();
		outgoing = new JTextField(15);
		JScrollPane sp = new JScrollPane(incoming);
		send = new JButton("Send");
		send.addActionListener(this);
		
		display.add(incoming);
		input.add(outgoing);
		input.add(send);
		
		
		add(sp);
		add(display, BorderLayout.CENTER);
		add(input, BorderLayout.SOUTH);
		setSize(400,200);
		setLocation(250, 350);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void actionPerformed(ActionEvent ae){
		incoming.append(outgoing.getText());
	}
	public static void main(String[] args){
		new myClient();
		try{
			String message = "";
			boolean cont = true; 
			boolean stay = true;
			Socket sock;
			
			while(stay){
				System.out.println("Stay");
				sock = new Socket("129.21.113.179", 16798);
				DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
				DataInputStream dis = new DataInputStream(sock.getInputStream());
				PrintStream ps = new PrintStream(sock.getOutputStream());
				while (cont) {
					System.out.println("2");
					System.out.println("Enter Message");
					message = new Scanner(System.in).nextLine();
					//ps.write(message.getBytes()); //both of these are good, but only do one.
					dos.writeBytes(message);
					System.out.println("sent: "+message);	
					dos.flush();
					dos.close();
					dis.close();
					cont = !message.equals("exit");
				}
				if (!cont) {
					System.out.println("1");
					sock.close();
					stay = false;
				}
			}

			
			
		}
		catch(BindException be){
			System.out.println("bind exception");
			be.printStackTrace();
		}
		catch(UnknownHostException uhe){
			System.out.println("unknown host");
		}
		catch(IOException ioe){
			System.out.println("ioe");
			ioe.printStackTrace();
		}
	}
}





