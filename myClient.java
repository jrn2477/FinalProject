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
		
	}
	public void actionPerformed(ActionEvent ae){
		incoming.append(outgoing.getText());
	}
	public static void main(String[] args){
		new myClient();
		try{
			String message;
			Socket sock = new Socket("129.21.113.179", 16798);
			DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
			DataInputStream dis = new DataInputStream(sock.getInputStream());
			//do-while loop is better, nested loops are not necessary. 
			do{
				System.out.print("Enter Message: ");
				message = new Scanner(System.in).nextLine();//takes a message
				dos.writeBytes(message +"\n");//writes it. the "\n" is necessary because a line is considered to be terminated by the BufferedReader (server-side) if it ends with a new line ("\n")
				dos.flush();//flushes, ensuring that the message is written
				//System.out.println();
			}while(!message.equals("exit"));//if the message is not "exit", continue. Otherwise, it indicates that the user wishes to exit. We must send "exit" because that indicates to the Server that the user wants to terminate their connection.
			
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





