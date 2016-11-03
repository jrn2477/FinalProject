import java.net.*;
import java.io.*;
import java.io.Reader.*;


public class MyThrdSvr{
	
	/*
	** Overloaded Constructor 
	** @return void 
	** @param null
	*/ 
	public MyThrdSvr(){
		
		
		// Code to create the ServerSocket 
		// the ServerSocket will continue to accept threads
		try{
			
			ServerSocket ss = new ServerSocket(16798);
			Socket cs = null;
			
			// Continually keep accepting connections 
			// (this is an infinite loop by design) 
			while(true){
				cs = ss.accept();
				ThreadServer ts = new ThreadServer(cs);
				// TODO: 
				// Add ArrayBlockingQueue to store threads of the client 
				ts.start();
			}
		}
		// Catch any binding exceptions assocaited with the creation of
		// the Socket, which is connected to a Thread instance of the client 
		catch(BindException be){
			System.out.println("Bind Exception (MyThreadServer constructor)");
		}
		
		// Catch any IOExceptions assocaited with the creation of
		// the Socket, which is connected to a Thread instance of the client  
		catch(IOException ioe){
			System.out.println("I/O Exception, MyThreadServer constructor");
		}
	}
	
	/*
	** Main Method
	** @param String[] 
	** @return void 
	** @decription - Class main method. 
	*/ 
	public static void main(String[] args){
		new MyThrdSvr();
	}
}

/* 
** Innner Class - ThreadServer 
** Each thread will serve as a connection for which MyClient.java will connect 
**/ 
class ThreadServer extends Thread {
	
	// Socket connnection between client and server threads 
	Socket cs;
	
	/*
	** Overloaded Constructor 
	** @param Socket - This is the connection between the server and the client 
	** @return void - Nothing to return 
	*/ 
	public ThreadServer(Socket cs){
		this.cs = cs;
	}
	
	
	/*
	** Run Method 
	** @param null 
	** @return void
	** @description: Mandatory Thread run method  
	*/ 
	public void run(){
		
		// Connection Established: Notify developer 
		System.out.println("Connection Detected : "+cs.getInetAddress());
		
		// Objects necesary to form the connection
		BufferedReader br = null;
		DataOutputStream dos = null;
		// variable to hold the message - content not relevant 
		String message = "f";
		
		
		
		// Try to form the connection
		try{
			// Create reader objects that will interpret information being sent. 
			br = new BufferedReader(cs.getInputStream());
			dos = new DataOutputStream(cs.getOutputStream());

			// Continue reading in information until the user types "exit" 
			while(!message.equals("exit")){
				// Error checking 
				System.out.println("while(message) execution");
				
				// While the BufferedReader has something to read 
				// concatenate the tempLine string with the next character 
				// will stop when the BufferedReader has nothing left to read 
				String tempLine;
				while (br.ready()) {
					tempLine = br.read();
				}
				
				// Parse the information read 
				// Store in message variable to output to GUI later 
				// Does it equal exit? 
				if(tempLine != null){
					System.out.println("if execution");
					message = tempLine;
					if (message.equals("exit")) {
						return;
					}
				}
				
				// Check to make sure the message wasn't null
				if (message != null) {
					System.out.println("recieved: "+ message);
				} else {
					System.out.println("No message recieved");
				}
			}
			
			// Close Output Streams. 
			dos.close();
			br.close();
			cs.close();
		}
		// Catch any exceptions 
		catch(IOException ioe){
			System.out.println("there was an i/o exception during the run method");
			ioe.printStackTrace();
		}
	}
}