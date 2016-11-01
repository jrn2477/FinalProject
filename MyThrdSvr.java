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
		System.out.println("running");
		DataInputStream dis = null;
		DataOutputStream dos = null;
		try{
			dis = new DataInputStream(cs.getInputStream());
			dos = new DataOutputStream(cs.getOutputStream());
			System.out.println(dis.readLine());
			
			dis.close();
			dos.close();
			cs.close();
		}
		catch(IOException ioe){
			System.out.println("there was an i/o exception during the run method");
		}
	}
}