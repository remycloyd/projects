import java.io.*;
import java.net.*;
public class talki
{
  public static void main(String[] args) throws Exception
  {
     Socket socket = new Socket("127.0.0.1", 16405);
     // read from keyboard 
     BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
     // send to client 
     OutputStream outstream = socket.getOutputStream(); 
     PrintWriter pwrite = new PrintWriter(outstream, true);
     // receive from server 
     InputStream instream = socket.getInputStream();
     BufferedReader receiveRead = new BufferedReader(new InputStreamReader(instream));
     System.out.println("Connection Established, type a message.");
     String receiveMessage, sendMessage;               
     while(true)
     {
    	// keyboard reading
        sendMessage = keyRead.readLine();  
        // sending to server	
        pwrite.println(sendMessage);       
        // flush the data
        pwrite.flush();            
        //receive from server
        if((receiveMessage = receiveRead.readLine()) != null) 
        {
        	// display prompt
            System.out.println(receiveMessage); 
        }         
      }               
    }                    
}                        