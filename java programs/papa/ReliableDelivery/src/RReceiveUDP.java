import java.io.IOException; 
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import edu.utulsa.unet.UDPSocket; //import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;


public class RReceiveUDP extends RUDP implements edu.utulsa.unet.RReceiveUDPI, Runnable{
	
	public static void main(String[] args)
	{
		RReceiveUDP reciever = new RReceiveUDP();
		reciever.setMode(0);
		reciever.setModeParameter(512);
		reciever.setFilename("received.txt");
		reciever.setLocalPort(32456);
		reciever.receiveFile();
	}
	
	
	int reciever = 12987;	
	
	public int getLocalPort() {
		return reciever;
	}

	
	public boolean setLocalPort(int portNum) {
		if(portNum>=0){
			reciever = portNum;
			return true;
		}
		return false;
	}
	
	InetSocketAddress sender;
	Long timeout;
	int mtu;
	ArrayList<byte[]> data = new ArrayList<byte[]>();
	UDPSocket socket;
	boolean finAckRecieved;
	long finishedTimeout;
	
	@Override
	public boolean receiveFile() {
		try
		{
			socket = new UDPSocket(getLocalPort());
			mtu = socket.getSendBufferSize();
			finAckRecieved = false;
			finishedTimeout = Long.MAX_VALUE;
			
			
			System.out.println("RECEIVER: preparing to save incoming file as "+this.filename+" at "+socket.getLocalAddress().getCanonicalHostName()+":"+this.getLocalPort()+" using "+mode.toString());
			Long startTime = System.currentTimeMillis();
			
			Thread t = new Thread(this);
			t.start();
			
			while(finishedTimeout>System.currentTimeMillis()){
				Thread.sleep(10);						
			}
			
			int fullBytes = 0;
			for(byte[] b :data)
				fullBytes += b.length;
			
			byte[] message = new byte[fullBytes];
			ByteBuffer buf = ByteBuffer.wrap(message);
			
			for(byte[] b: data)
				buf.put(b);

			Long stopTime = System.currentTimeMillis();
			System.out.println("Successfully transferred "+this.filename+" ("+message.length+" bytes) in "+ ((stopTime-startTime)/1000.0)+"secs");
			
			
			PrintWriter scribe = new PrintWriter(this.filename);
			scribe.print(new String(message));
			scribe.close();
			
		}
		catch(Exception e){ e.printStackTrace(); 
		}
		return false;
	}
	
	
	@Override
	public void run() {
		while(finishedTimeout>System.currentTimeMillis()){		
			try {
				byte[] buffer = new byte[mtu];
				DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
				socket.receive(packet);
				
				Packet p = Packet.decodePacket(buffer, packet.getLength());
				if(!(finishedTimeout>System.currentTimeMillis()))
					return;
				
				if(sender==null){
			
					
					sender = new InetSocketAddress(packet.getAddress(),packet.getPort());
					System.out.println("Connection established with sender at "+sender.toString());
					timeout = p.timeout;
				}
				if(!p.isFinished){
					while((data.size()-1)<p.sequenceNumber)
						data.add(null);
					
					data.set(p.sequenceNumber, p.data);
					System.out.println("Message "+ p.sequenceNumber+" recieved with "+p.data.length+" bytes of actual data");
					
					
					Packet ack = new Packet(new byte[0], p.sequenceNumber, timeout, false, !sequenceGaps(), true);
					socket.send(new DatagramPacket(ack.toBytes(), ack.toBytes().length, sender.getAddress(), sender.getPort()));
					System.out.println("Message "+p.sequenceNumber+" acknowledgement sent");
				}else{
					// received fin, ack so start the timeout timer
					// stay awake till timer ends in case fin, ack gets lost
					
					System.out.println("Fin received");
					Packet ack = new Packet(new byte[0], -1, timeout, true, true, true);
					socket.send(new DatagramPacket(ack.toBytes(), ack.toBytes().length, sender.getAddress(), sender.getPort()));
					System.out.println("Fin Ack sent");
					
					finishedTimeout = System.currentTimeMillis();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	private boolean sequenceGaps() {
		for(byte[] ba: data)
			if(ba == null)
				return true;
		return false;
	}

	public enum Mode
	{
		SAW, SlidingWindow
	}
	
	
	Mode mode = Mode.SAW;
	long slidingWindowSize = 0;
	String filename;
	
	public boolean setMode(int modeNum) 
	{
		switch (modeNum) 
		{
		case 0:
			mode = Mode.SAW;
			slidingWindowSize = 1;
			return true;
		case 1:
			mode = Mode.SlidingWindow;
			slidingWindowSize = 256;
			return true;
		default:
			return false;
		}
	}
	
	
	public boolean setModeParameter(long param) {
		if (mode == Mode.SlidingWindow) {
			slidingWindowSize = param;
			return true;
		}
		return false;
	}


	public String getFilename() {
		return filename;
	}


	public int getMode() {
		switch (mode) {
		case SAW:
			return 0;
		case SlidingWindow:
			return 1;
		}
		return -1;
	}


	public long getModeParameter() {
		return slidingWindowSize;
	}


	public void setFilename(String fName) 
	{
		filename = fName;
	}
}