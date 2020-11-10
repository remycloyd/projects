import java.io.*;
import java.net.*;
public class TalkClient{	
	public static void main(String[] args)	{
	String serverName="127.0.0.1";	
	int serverPortNumber=16405;	
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