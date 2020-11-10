import java.io.*;
import java.net.*;

public class Talk 
	{
		public static void main(String [] args)
		{
			BufferedReader in=null;
			String message=null;
			Socket client=null;
			ServerSocket server=null;
			String hostname = null;
			int portnum;
			switch(args[0])
				{				
				case "-h": 
					//Talk –h [hostname | IPaddress] [–p portnumber]
					//The program behaves as a client connecting to [hostname | IPaddress] on port portnumber.
					//If a server is not available your program should exit with the message 
					//“Client unable to communicate with server”.
					//Note: portnumber in this case refers to the server and not to the client.
					if(args.length == 4 )
					{
						hostname = args[1];
						if(args[2].equals("-p"))
						{
							try{
								portnum = Integer.parseInt(args[3]);
							}
							catch (NumberFormatException x)
							{
								System.out.println("-h ERROR: Non digit characters present in port Number.");
								System.exit(0);
							}				
						}
						else
						{
							System.out.println("input bad: case -h inner else statement.");
							System.exit(0);
						}
		
					}
					else
					{
						System.out.println("input bad: case -h outer else statement");
						System.exit(0);
					}						
					break;
							
				case "-a":
					//Talk –a [hostname|IPaddress] [–p portnumber]
					//The program enters ``auto’’ mode. When in auto mode, 
					//your program should start as a client attempting to communicate with hostname|IPaddress on portnum
					//If a server is not found, your program should detect this condition
					//and start behaving as a server listening for connections on port portnumber.
					if(args.length == 4 )
					{		
						portnum = Integer.parseInt(args[3]);		
						hostname = args[1];				
						if(args[2].equals("-p"))
						{
		
							System.out.println("Connecting to server on port " + portnum);
							try
							{       					
								@SuppressWarnings("resource")
								Socket socket = new Socket(hostname, portnum);
								BufferedReader in1 = new BufferedReader(new InputStreamReader(System.in));
								PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
								while(true)
								{
									message = in1.readLine();	   
									out.println(message);	   
								}
							} 
							catch (UnknownHostException e) 
							{
								System.out.println("Unknown host: "+ hostname);
								System.exit(1);
							}						
							catch (IOException f) 
							{
								System.out.println("No I/O detected.");
								try
								{
									server= new ServerSocket(portnum);
									System.out.println("Auto mode transition to Server, now listening on port "+ portnum);
								}
								catch (IOException g)
								{
									System.out.println("Server could not listen on port "+ portnum);
									System.exit(-1);
								}
								try
								{
									client = server.accept();
									System.out.println("Server accepted connection from "+ client.getInetAddress());
								}
								catch (IOException h)
								{
									System.out.println("Server acceptance failed on port "+ portnum);
									System.exit(-1);
								}
								try
								{
									in = new BufferedReader(new InputStreamReader(client.getInputStream()));
								}
								catch (IOException i)
								{
									System.out.println("Couldn't get an inputStream from the client");
									System.exit(-1);
								}	
								try
								{
									while(true)
										{
										 if (in.ready()) 
											 {
												message = in.readLine();
												System.out.println(message); 
											 }				
										
										}
								}			
								catch (IOException j) 
								{
									System.out.println("Read failed");
									System.exit(-1);
								}
							}
						}
							else
							{
								System.out.println("Your input was unacceptable. Please try again. -a inner else statement");
								System.exit(0);
							}
		
						}
						else
						{
							System.out.println("input bad, missing port parameter: -a outer else statement");
							System.exit(0);
						}						
						break;		
		
						
					case "-s":
						//Talk –s [–p portnum]
						//The program behaves as a server listening for connections on port portnumber. 
						//If the port is not available for use, your program should exit with the message 
						//“Server unable to listen on specified port”.
						if(args.length == 1)
							{
								portnum = 80;
								System.out.println("No port number entered, port number auto set to 80.");
								try
									{
										server = new ServerSocket(portnum);
										System.out.println("Server listening on port "+ portnum);
										while(true)
											{
												if (in.ready()) 
													{
														message = in.readLine();
														System.out.println(message);
													}
											}
									}
								catch (IOException e)
									{
										System.out.println("Could not listen on port "+ portnum);
										System.exit(-1);
									}
								try
									{
										client = server.accept();
										System.out.println("Server accepted connection from "+ client.getInetAddress());
									}
								catch (IOException e)
									{
										System.out.println("Acceptance failed on port "+ portnum);
										System.exit(-1);
									}
								try
									{
										in = new BufferedReader(new InputStreamReader(client.getInputStream()));
									}
								catch (IOException e)
									{
										System.out.println("Couldn't obtain an inputStream from the client");
										System.exit(-1);
									}
							}
		
						else if(args.length == 3)
						{
							try
							{ 
								portnum = Integer.parseInt(args[2]);
							}
							catch (NumberFormatException x)
							{
								System.out.println("ERROR: Non digit characters present in Port Number");
								System.exit(0);				
							}
							break;
						}
						//Talk –help
						//The program prints your name and instructions on how to use your program.
					case "-help":
						if(args.length == 1 )
						{
							System.out.println(" I am Jeremy Cloyd and I hear you need help");
							System.exit(0);
						}
					default: 	
						System.out.println("System did not understand your command, Please try again." +
								"\n" + " Options:"+ "\n"+ "–a [hostname|IPaddress] [–p portnumber]" +"\n" + "–s [–p portnumber]"+"\n" +
								"-h [hostname | IPaddress] [–p portnumber]" +"\n"+"-help");		
		
					}				
				}
			}
	
	//	class Server
	//	{
	//		BufferedReader in=null;
	//		int serverPortNumber = 16405;
	//		String message=null;
	//		Socket client=null;
	//		ServerSocket server=null;
	//		
	//		try{
	//			server= new ServerSocket(serverPortNumber);
	//			System.out.println("Server listening on port "+ serverPortNumber);
	//			}
	//		catch (IOException e)
	//		{
	//			System.out.println("Could not listen on port "+ serverPortNumber);
	//			System.exit(-1);
	//		}
	//		try
	//		{
	//			client=server.accept();
	//			System.out.println("Server accepted connection from "+ client.getInetAddress());
	//		}
	//		catch (IOException e)
	//		{
	//			System.out.println("Accept failed on port "+ serverPortNumber);
	//			System.exit(-1);
	//		}
	//		try
	//		{
	//			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	//		}
	//		catch (IOException e)
	//		{
	//			System.out.println("Couldn't get an inputStream from the client");
	//			System.exit(-1);
	//		}
	//		try
	//		{
	//			while(true)
	//			{
	//			 if (in.ready()) {
	//				message = in.readLine();
	//				System.out.println(message);}
	//			}
	//		}
	
	//	public static void main(String [] args)
	//	{
	//		for(int i=0; i< args.length; i++)
	//		{
	//			System.out.println(args[i]);
	//				
	//			}
	//		
	//	catch (IOException e) 
	//		{
	//		System.out.println("Read failed");
	//			System.exit(-1);
	//	}
	//		}
	
	//	class Client{	
	//			//Create socket connection	
	//			String serverName="127.0.0.1";	
	//			int serverPortNumber = 16405;	
	//			String message=null;  
	//			System.out.println("Connecting to server on port" + serverPortNumber);
	//			try{       
	//				@SuppressWarnings("resource")
	//				Socket socket = new Socket(serverName, serverPortNumber);
	//		       	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	//		       	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	//		       	while(true)
	//		       		{
	//					   	message = in.readLine();	   
	//						out.println(message);	   
	//		       		}
	//		        } catch (UnknownHostException e) 
	//					{
	//				       System.out.println("Unknown host:"+serverName);
	//				       System.exit(1);
	//		     } catch  (IOException e) 
	//					{
	//						System.out.println("No I/O");
	//						System.exit(1);      
	//		     		}
	//			}
	//		}
	//	}
	//}