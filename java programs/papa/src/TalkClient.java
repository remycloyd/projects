
import java.io.*;
import java.net.*;
public class TalkClient
{	
	public static void main(String[] args)	{
	//Create socket connection	
	//System.out.println("Starting TalkClient");
	String serverName="linux1.ens.utulsa.edu";	
	int serverPortNumber=16406;	
	String message=null;    
	try{       
		Socket socket = new Socket(serverName, serverPortNumber);
       		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
       		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	   while(true)
	   {
	   	message = in.readLine();	   
		out.println(message);	   }
     } catch (UnknownHostException e) {
       System.out.println("Unknown host:"+serverName);
       System.exit(1);
     } catch  (IOException e) {
       System.out.println("No I/O");
       System.exit(1);
     }
  }
}
public class TalkServer
{
	public static void main(String [] args)
	{
	System.out.println("Starting TalkServer");
	BufferedReader in=null;
	int serverPortNumber=16405;
	String message=null;
	Socket client=null;
	ServerSocket server=null;
	try{
		server= new ServerSocket(serverPortNumber);
		System.out.println("Server listening on port "+serverPortNumber);}
	catch (IOException e)
	{
		System.out.println("Could not listen on port "+serverPortNumber);
		System.exit(-1);
	}
	try{
		client=server.accept();
		System.out.println("Server accepted connection from "+client.getInetAddress());}
	catch (IOException e)
	{
		System.out.println("Accept failed on port "+ serverPortNumber);
		System.exit(-1);
	}
	try{
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	}
	catch (IOException e)
	{
		System.out.println("Couldn't get an inputStream from the client");
		System.exit(-1);
	}
	try{
		while(true){
		 if (in.ready()) {
			message=in.readLine();
			System.out.println(message);}
		}
	}
	catch (IOException e) {
		System.out.println("Read failed");
		System.exit(-1);
	}
}
}


