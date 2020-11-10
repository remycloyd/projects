//Jeremy Cloyd 
// september 24, 2019
import java.io.*;
import java.net.*;
public class Talk
	{
		public static void main(String [] args)
		{
			Talk talk = new Talk();			
			talk.argParse(args);													
		}
		
		
		public void argParse(String[] args)
		{
			
			if (args.length < 1|| args.length > 4)
			{
				System.out.println("Invalid Argument Length");
				System.exit(0);
			}
			else
			{
				switch(args[0])
				{
				case "-h":
					//Talk –h [hostname | IPaddress] [–p portnumber] 
					//The program behaves as a client connecting to [hostname | IPaddress] on port portnumber.
					System.out.println("Attempting Client Mode.");
					String hostname = "";
					int portnum; // = Integer.parseInt(args[3]);			
					String messageH = null;
					if(args.length == 2 && args[0].equals("-h")) // Talk -h hostname
					{
						portnum = 12987;
						hostname = args[1];
						try
						{  
							Socket socket = new Socket(hostname, portnum);
							BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
							PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
							
							while(true)
							   {
							   	messageH = in.readLine();
							   	if(messageH.contentEquals("STATUS")) 
							   	{
							   		System.out.println("IP/Host: " + hostname + "\n" + "Port Number: " + portnum);
							   		break;
							   	}
								out.println(messageH);	   
							   }
					    } 
						catch (UnknownHostException e) 
						{
						       System.out.println("Unknown host:"+hostname);
						       System.exit(1);
						} 
						catch  (IOException e) 
						{
						       System.out.println("No I/O");
						       System.exit(1);
						}
					}
					if(args.length == 3) // Talk -h -p portnum
					{
						portnum = Integer.parseInt(args[2]);
						hostname = "localhost";
						if(args[1].equals("-p")) 
						{
							try
							{  
								Socket socket = new Socket(hostname, portnum);
								BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
								PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
								
								while(true)
								   {
								   	messageH = in.readLine();
								   	if(messageH.contentEquals("STATUS")) 
								   	{
								   		System.out.println("IP/Host: " + hostname + "\n" + "Port Number: " + portnum);
								   		break;
								   	}
									out.println(messageH);	   
								   }
						    } 
							catch (UnknownHostException e) 
							{
							       System.out.println("Unknown host:"+ hostname);
							       System.exit(1);
							} 
							catch  (IOException e) 
							{
							       System.out.println("No I/O");
							       System.exit(1);
							}
						}
					}
					if(args.length == 4) // Talk -h hostname -p portnum
					{
						portnum = Integer.parseInt(args[3]);
						hostname = args[1];
						if(args[2].equals("-p"))
						{
							try
							{  
								Socket socket = new Socket(hostname, portnum);
								BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
								PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
								
								while(true)
								   {
								   	messageH = in.readLine();
								   	if(messageH.contentEquals("STATUS")) 
								   	{
								   		System.out.println("IP/Host: " + hostname + "\n" + "Port Number: " + portnum);
								   		break;
								   	}
									out.println(messageH);	   
								   }
						    } 
							catch (UnknownHostException e) 
							{
							       System.out.println("Unknown host:"+hostname);
							       System.exit(1);
							} 
							catch  (IOException e) 
							{
							       System.out.println("No I/O");
							       System.exit(1);
							}
						}
						else
						{
							System.out.println(" Input Format Unacceptable, no -p at index 2 ");
							System.exit(0);
						}
					}
					else
					{
						System.out.println("Unacceptable number of arguments for -h input");
						System.exit(0);
					}						
					break;
					
				case "-s":
					//Talk –s [–p portnum]

					//The program behaves as a server listening for connections on port portnumber. 
					if(args[0].contentEquals("-s") && args.length==1)// Talk -s and nothing else
					{
						portnum = 12987;
						//hostname = args[1];	
						System.out.println("Attempting Server Mode");
						BufferedReader in = null; //new BufferedReader(new InputStreamReader(System.in));						
						String message = null;
						Socket client = null;
						ServerSocket server = null;
						try 
						{
							server = new ServerSocket(portnum);
							System.out.println("Server listening on port " + portnum);
						}
						catch (IOException e)
						{
							System.out.println("Could not listen on port " + portnum);
							System.exit(-1);
						}
						try
						{
							client=server.accept();
							System.out.println("Server accepted connection from " + client.getInetAddress());
						}
						catch (IOException e)
						{
							System.out.println("Accept failed on port " + portnum);
							System.exit(-1);
						}
						try
						{
							this.looper(client,null,portnum);
						} 
						catch (Exception X) 
						{
								//Auto-generated catch block
								X.printStackTrace();
								System.exit(0);
						}			
					}
					if(args.length == 3)//Talk -s -p portnum
						{
							hostname= "localhost";	
							System.out.println("Attempting Server Mode");
							BufferedReader in = null; 
							//new BufferedReader(new InputStreamReader(System.in));
//							portnum = Integer.parseInt(args[2]);
							String message = null;
							Socket client = null;
							ServerSocket server = null;
							try
							{
								portnum = Integer.parseInt(args[2]);
							}
							catch (NumberFormatException x)
							{
								System.out.println("-s PORT NUMBER ERROR: Non digit characters present in port Number.");
								System.exit(0);
							}
							try 
							{
								portnum = Integer.parseInt(args[2]);
								server = new ServerSocket(portnum);
								System.out.println("Server listening on port "+portnum);
							}
							catch (IOException e)
							{
								portnum = Integer.parseInt(args[2]);
								System.out.println("Could not listen on port "+portnum);
								System.exit(-1);
							}
							try
							{
								client=server.accept();
								System.out.println("Server accepted connection from "+client.getInetAddress());
							}
							catch (IOException e)
							{
								System.out.println("Accept failed on port "+ portnum);
								System.exit(-1);
							}
							try
							{
								this.looper(client,hostname,portnum);
							} 
							catch (Exception X) 
							{
									//Auto-generated catch block
									X.printStackTrace();
									System.exit(0);
							}			
						}
					else 
					{
						System.out.println("ERROR: Incorrect number of arguments for -s Mode");
						System.exit(0);					
					}
						break;
						
						
				case "-a":
				//	Talk –a [hostname|IPaddress] [–p portnumber]
					System.out.println("Attempting to connect in auto Mode.");
					if(args.length == 1 && args[0].equals("-a")) // Talk -p portnum
					{
						portnum = 12987;	
						hostname = "localhost";	
						try
							{  
								System.out.println("Initiating client mode on"+ "\n" + "port number: " + portnum + "\n" + "hostname: " + hostname);
								Socket socket = new Socket();			
								socket = new Socket (hostname, portnum);
								this.looper(socket, hostname, portnum);	
							}
							catch (UnknownHostException e) 
							{
								System.out.println("Unknown host: "+ hostname);
								System.exit(1);
							}						
							catch (IOException f) 
							{
								System.out.println("Server Not Detected.");
								try
								{
									ServerSocket server = new ServerSocket(portnum);
									System.out.println("Initiating Server mode connection on port number: " + portnum);
									Socket client = server.accept();
									System.out.println("Server accepted connection from "+ client.getInetAddress());
									this.looper(client, hostname, portnum);
								}
								catch (IOException e)
								{
									System.out.println("Accept failed on port "+ portnum);
									System.exit(-1);
								}
							}						
					}
					if(args.length == 2 && args[0].equals("-a")) // Talk -a hostname
					{
						portnum = 12987;	
						hostname = args[1];	
						try
							{  
								System.out.println("Initiating client mode on"+ "\n" + "port number: " + portnum + "\n" + "hostname: " + hostname);
								Socket socket = new Socket();			
								socket = new Socket (hostname, portnum);
								this.looper(socket, hostname, portnum);	
							}
							catch (UnknownHostException e) 
							{
								System.out.println("Unknown host: "+ hostname);
								System.exit(1);
							}						
							catch (IOException f) 
							{
								System.out.println("Server Not Detected.");
								try
								{
									ServerSocket server = new ServerSocket(portnum);
									System.out.println("Initiating Server mode connection on port number: " + portnum);
									Socket client = server.accept();
									System.out.println("Server accepted connection from "+ client.getInetAddress());
									this.looper(client, hostname, portnum);
								}
								catch (IOException e)
								{
									System.out.println("Accept failed on port "+ portnum);
									System.exit(-1);
								}
							}						
					}
					if(args.length == 3 && args[1].equals("-p")) // Talk -p portnum
					{
						portnum = Integer.parseInt(args[2]);	
						hostname = "localhost";	
						try
							{  
								System.out.println("Initiating client mode on"+ "\n" + "port number: " + portnum + "\n" + "hostname: " + hostname);
								Socket socket = new Socket();			
								socket = new Socket (hostname, portnum);
								this.looper(socket, hostname, portnum);	
							}
							catch (UnknownHostException e) 
							{
								System.out.println("Unknown host: "+ hostname);
								System.exit(1);
							}						
							catch (IOException f) 
							{
								System.out.println("Server Not Detected.");
								try
								{
									ServerSocket server = new ServerSocket(portnum);
									System.out.println("Initiating Server mode connection on port number: " + portnum);
									Socket client = server.accept();
									System.out.println("Server accepted connection from "+ client.getInetAddress());
									this.looper(client, hostname, portnum);
								}
								catch (IOException e)
								{
									System.out.println("Accept failed on port "+ portnum);
									System.exit(-1);
								}
							}						
					}
					else if(args.length == 4 )
					{	
						portnum = Integer.parseInt(args[3]);	
						hostname = args[1];				
						if(args[2].equals("-p"))
						{
							try
							{   
								System.out.println("Initiating Client mode on port number: " + portnum + "\n" + "attempting hostname: " + hostname);
								Socket socket = new Socket();			
								socket = new Socket (hostname, portnum);
								this.looper(socket, hostname, portnum);	
							} 
							catch (UnknownHostException e) 
							{
								System.out.println("Unknown host: "+ hostname);
								System.exit(1);
							}						
							catch (IOException f) 
							{
								System.out.println("Server Not Detected.");
								try
								{
									ServerSocket server = new ServerSocket(portnum);
									System.out.println("Initiating Server mode connection on port number: " + portnum);
									Socket client = server.accept();
									System.out.println("Server accepted connection from "+ client.getInetAddress());
									this.looper(client, hostname, portnum);
								}
								catch (IOException e)
								{
									System.out.println("Accept failed on port "+ portnum);
									System.exit(-1);
								}
							}
						}
						else
						{
							System.out.println("Your input was unacceptable. Invalid [2] argument");
							System.exit(0);
						}
					}
					else
					{
						System.out.println("input bad, Invalid parameter length.");
						System.exit(0);
					}						
					break;
			
				
				case "-help":
					if(args.length == 1)
						{
							System.out.println("My name is Jeremy Cloyd and I hear you need help." + "\n" + 
								"To behave as a server listening for connections on a portnumber, type: Talk –s [–p portnum] " + "\n"+
								"To behave as a Client on an IP Address and portnumber, type: Talk –h [hostname | IPaddress] [–p portnumber] "+ "\n"+	
								"For Auto Mode type: Talk –a [hostname|IPaddress] [–p portnumber]");
							System.exit(0);
						}
					break;
					
				default:
					System.out.println("Error: Incorrectly Formatted Command.");
					System.exit(-1);
				}				
			}
		}
		
		
		public void looper(Socket soc, String hostname, int portnum)
		{			
			String message = "";				
			BufferedReader inA = null;
			PrintWriter out = null;
			BufferedReader inB = null;
			try
			{
				inA = new BufferedReader(new InputStreamReader(System.in));
				out = new PrintWriter(soc.getOutputStream(), true);						
				inB = new BufferedReader(new InputStreamReader(soc.getInputStream()));
				
			}
			catch(IOException b)
			{
				System.out.println("Unable to establish loop");
				System.exit(-1);
			}	
			try
			{
				while(true)
				{
					if (inA.ready()) 
					{
						message = inA.readLine();	//
						if(message.contentEquals("STATUS")||message.equals("status")) 
					   	{
					   		System.out.println("IP/Host: " + hostname + "\n" + "Port Number: " + portnum);
					   		break;
					   	}
						while ((message = inA.readLine()) != null) 
						{
							out.println(message);	   
						}
					}
					else  
					{
						if(inB.ready()) 
						{
						message = inB.readLine();
						System.out.println("[remote] " + message); 
						}	
					}
				}
			}
			catch (IOException a)
			{
				System.out.println("IO Exception: Unable to read input");
				System.out.println(a.getMessage());
				System.exit(0);
			}		
		}
	}