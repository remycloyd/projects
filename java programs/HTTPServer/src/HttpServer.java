import java.io.BufferedInputStream; 
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Date;
//import java.util.Scanner;
public class HttpServer 
{
	static String SLASH = File.separator;
			

	public static void main(String[] args) throws IOException 
	{
		//Scanner input
		ServerSocket serverSocket;
		serverSocket = new ServerSocket(8080);
		System.out.println("Connection is open on port: "+ serverSocket.getLocalPort());
		File root = new File("public_html");
		
		while (true) 
		{	
			// get the server going
			Date changedRequest = new Date(0);
			String reply;
			Socket ConnectSocket = serverSocket.accept();
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(ConnectSocket.getInputStream()));
			DataOutputStream ServerOutput = new DataOutputStream(ConnectSocket.getOutputStream());
			
			
			String filetype = "text/html";
			
			if (clientInput.ready()) 
			{
				String Rin = clientInput.readLine();
				
				System.out.println(Rin);
				String requestFilename = getFileRequest(Rin);
				
				if (requestFilename.matches(".*(.pdf)")) 
				filetype = "pdf";
		
				
				else if (requestFilename.matches(".*(.jpg)")) 
					filetype = "image/jpg";
				
				
				else if (requestFilename.matches(".*(.gif)"))
					filetype = "image/gif";
				
				
				while (clientInput.ready()) 
				{
					String line = clientInput.readLine();
					System.out.println(line);
					
					if (line.matches("(If-modified-since:).*")) 
					{
						changedRequest = new Date(line.replace("if-modified-since: ", ""));
						
					}
					else changedRequest = new Date(0);
				}

				File requestFile = new File(root + requestFilename);
				Date dateOfLastChange = new Date(requestFile.lastModified());
				
				if (requestFile.exists()) {
					reply = setreply("200 OK", filetype);
				}
				else 
				{
					reply = setreply("404, File not Found", filetype);
				}
				
				System.out.println(requestFile);
				
				if (dateOfLastChange.after(changedRequest)) 
				{
					System.out.println(reply);
					sendreply(ServerOutput, reply, requestFile);
				}
			}
		}
		
	}
	private static String getFileRequest(String headerLine)  
	{
		String[] headerTokens = headerLine.split(" ");
		
		for (String sTR : headerTokens) 
		{
			sTR.replace("*..*","index.html");
		
			if (sTR.matches("/.*(.html)?")) 
			{
				if (sTR.matches(".*/")) 
				{
					return sTR.replace("/", SLASH) + "index.html";
				}
				
				else 
				{
					return sTR.replace("/", SLASH);
				}
			}
		}		
		return "0";
	}
	
	
	private static String setreply(String code, String filetype) 
	{
		Date d8 = new Date();
		String reply = "HTTP/1.1 " + code + "\r\n";
		
		reply += "Date: " + d8.toString() + "\r\n";
		reply += "Server: HTTPServer/0.0.3\r\n";
		reply += "Connection: keep-alive\r\n";
		reply += "Content-Type: " + filetype + " charset=UTF-8\r\n";
		reply += "\r\n";
		return reply;
	}
	
	private static void sendreply(DataOutputStream ServerOutput, String reply, File file)throws IOException 
	{
		
		byte[] fileBytes = new byte[0];
		if (file != null) 
		{
			System.out.println("sending file: " + file.toString());
			fileBytes = new byte[(int)file.length()];
			BufferedInputStream fileBuffer = new BufferedInputStream(new FileInputStream(file));
			fileBuffer.read(fileBytes);
		} 
		else 
		{
			String fof = "<h1>404 File was not found</h1>";
			fileBytes = fof.getBytes();
		}
		ServerOutput.write(reply.getBytes());		
		ServerOutput.write(fileBytes);
		ServerOutput.close();
		System.out.println("Connection WAS closed.");
	}
}