import java.net.*;
import java.io.*;

class MyThrdSvr{
	public MyThrdSvr(){
		try{
			ServerSocket ss = new ServerSocket(16798);
			Socket cs = null;
			while(true){
				cs = ss.accept();
				ThreadServer ts = new ThreadServer(cs);
				ts.start();
			}
		}
		catch(BindException be){
			System.out.println("Bind Exception (MyThreadServer constructor)");
		}
		catch(IOException ioe){
			System.out.println("I/O Exception, MyThreadServer constructor");
		}
	}
	public static void main(String[] args){
		new MyThrdSvr();
	}
}

class ThreadServer extends Thread {
	Socket cs;
	
	public ThreadServer(Socket cs){
		this.cs = cs;
	}
	public void run(){
		System.out.println("Connection Detected : "+cs.getInetAddress());
		DataInputStream dis = null;
		DataOutputStream dos = null;
		String message = "f";
		try{
			dis = new DataInputStream(cs.getInputStream());
			dos = new DataOutputStream(cs.getOutputStream());
			//do{
				//message = dis.readLine();
				//System.out.println(message+cs.getInetAddress());
			while(!message.equals("exit")){
				System.out.println("while(message) execution");
				String tempLine = dis.readLine();
				if(tempLine != null){
					System.out.println("if execution");
					message = tempLine;
				}
				System.out.println("recieved: "+ message);
				//message = null;
			}
			
			dos.close();
			dis.close();
			cs.close();
		}
		catch(IOException ioe){
			System.out.println("there was an i/o exception during the run method");
			ioe.printStackTrace();
		}
	}
}