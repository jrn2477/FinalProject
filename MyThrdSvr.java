import java.net.*;
import java.io.*;
import java.io.Reader.*;
import java.util.concurrent.*;


public class MyThrdSvr{
	//ConcurrentLinkedQueue s are more thread friendly, they also don't have a max size.
	private static ConcurrentLinkedQueue<String> messages;
	private ConcurrentLinkedQueue<ThreadServer> threads;
	/*
	** Overloaded Constructor 
	** @return void 
	** @param null
	*/
	public MyThrdSvr(){
		
		threads = new ConcurrentLinkedQueue<ThreadServer>();//max chatroom size is 15. this can be increased.
		messages = new ConcurrentLinkedQueue<String>();//only stores 45 messages at a time, is then cleared.
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
				threads.add(ts);
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
	** Adds Message to ArrayBlockingQueue<String> Messages
	** @param m String representation of a message 
	** @return void
	*/
	public static void addMessage(String m){
		if(!messages.add(m)){//will add the item unless the queue is full.
			messages.clear();//if queue is full, it is cleared
			messages.add(m);//and the add is attempted again
		}
		System.out.println(messages.poll());//then prints whatever message is at the head of the queue
		//this will need to be replaced with a broadcast method.
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
	BufferedReader br;
	DataOutputStream dos;
	
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
		//BufferedReader br = null;
		//DataOutputStream dos = null;//moved to attributes of this class.
		// variable to hold the message - content not relevant 
		String message = "f";
		
		
		
		// Try to form the connection
		try{
			// Create reader objects that will interpret information being sent. 
			br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
			dos = new DataOutputStream(cs.getOutputStream());
			boolean connected = true;
			while(connected){//will loop until the user wants to exit.
				message = br.readLine();
				if(message != null && !message.equals("exit")){//if "exit" is sent, it will not be printed
					//System.out.println(message); //does not do the thing that we want.
					MyThrdSvr.addMessage(message);
				}
				if(message.equals("exit")){
					connected = false;
					dos.close();//these all need to close before we try to loop again, otherwise things get fucked up for some reason.
					br.close();
					cs.close();
				}
			}
		}
		catch(IOException ioe){
			System.out.println("there was an i/o exception during the run method");
			ioe.printStackTrace();
		}
	}
}