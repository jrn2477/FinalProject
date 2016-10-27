import java.io.*;
import java.net.*;
import java.util.*;


public class ClientSender extends Thread {
	
	String message; 
	String host; 
	int portNum; 
	
	public ClientSender(String msg,String hostname,int port){
		message = msg; 
		host = hostname; 
		portNum = port; 
	}
	
	public void run(){
		try {
			Socket s = new Socket(host, portNum);
			s.getOutputStream().write(message.getBytes()); 
			s.close(); 
		} catch (Exception e) {
			
		}
	}	
}