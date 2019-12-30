import java.net.DatagramPacket; 
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import edu.utulsa.unet.UDPSocket;
import java.io.IOException;
public class RSendUDP implements edu.utulsa.unet.RSendUDPI 
{

	public static void main(String[] args) 
	{
		RSendUDP sender = new RSendUDP();
		sender.setMode(0);
		sender.setModeParameter(512);
		sender.setTimeout(1000);
		sender.setFilename("suavemente.txt");;//("qCrypto.pdf");//("photo.gif")
		sender.setLocalPort(23456);
		sender.setReceiver(new InetSocketAddress("localhost", 32456));
		sender.sendFile();
	}

	// in millisecs
	InetSocketAddress receiver;
	int sendPort = 12987;
	long timeOut = 1000;

	public int getLocalPort() {
		return sendPort;
	}

	public long getTimeout() {
		return timeOut;
	}

	public InetSocketAddress getReceiver() {
		return receiver;
	}

	public boolean setLocalPort(int portNum) {
		if (portNum >= 0) {
			sendPort = portNum;
			return true;
		}
		return false;
	}

	public boolean setReceiver(InetSocketAddress address) {
		receiver = address;
		return true;
	}

	public boolean setTimeout(long time) {
		timeOut = time;
		return true;
	}

	public boolean sendFile() {
		return stopAndWait();
	}

	public enum Mode {
		SAW
	}

	Mode mode = Mode.SAW;
	static String filename;

	@Override
	public boolean setModeParameter(long arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getFilename() {
		return filename;
	}

	public int getMode() {
		switch (mode) {
		case SAW:
			return 0;
		}
		return -1;
	}

	@Override
	public long getModeParameter() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setFilename(String fName) {
		filename = fName;
	}

	public boolean setMode(int modeNum) {
		switch (modeNum) {
		case 0:
			mode = Mode.SAW;
			return true;
		default:
			return false;
		}
	}
	
	public static byte[] intToBytes(int value) 
	{
	    ByteBuffer buffer = ByteBuffer.allocate(4);
	    buffer.putInt(value);
	    return buffer.array();
	}

	public byte[] Fragmentator(String filename) throws IOException {
		System.out.println("Assessing need to fragment file " + filename);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		int nRead=0;
//		// existence
//		File data = new File(filename);
//		FileInputStream fis = new FileInputStream(data);
//
//		// determine size
//		byte[] fBytes = new byte[(int) data.length()]; // number of bytes in file
//
//		// read each byte from input stream and write to byte array output stream.
//		while ( (nRead = fis.read(fBytes, 0, (fBytes.length) ) ) != -1) 
//		{
//			baos.write(fBytes, 0, nRead);
//		}		
		byte[] fBytes = Files.readAllBytes(Paths.get(filename));
		//System.out.println("File size in Bytes is " + fBytes.length);
		// fis.close();
		return fBytes; // return a byte[] named fBytes containing the byte data of the file

	}

	private Boolean stopAndWait() 
	{
		try 
		{
			UDPSocket senderSocket = new UDPSocket(getLocalPort());
			int mtu = senderSocket.getSendBufferSize();
			
			
			int segmentSize = mtu - 12;
			byte[] fBytes = null;
			
			try 
			{
				fBytes = Fragmentator(filename);
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int NoOfSegments = (int) Math.ceil((double) fBytes.length / segmentSize);

			System.out.println("File " + filename + " of " + fBytes.length + " bytes, prepared for transmission with "
					+ NoOfSegments + " segment(s), to accomodate detected MTU size of " + mtu + "\n");

			for (int idx = 0; idx < NoOfSegments; ++idx) 
			{
				Boolean ackReceived=null;
				System.out.println("Entering top of send sequence loop. \n ");
				int sequenceNumber = idx; // sequence number will be the index number of the ArrayList
				int Offset = sequenceNumber * segmentSize;
				int length = 0;
				int lastIndicator = 0;

				if (idx + 1 == NoOfSegments) {
					length = fBytes.length % segmentSize;
					lastIndicator = 1;
					System.out.println("Last segment incoming \n");
				} 
				else
				{
					
					length = segmentSize;
					System.out.println("Segments are queued for transmission." + "\n");
				}

				byte[] payloadChunk = new byte[length];
				System.arraycopy(fBytes, Offset, payloadChunk, 0, length);
				
				System.out.println("payload generated with size " + payloadChunk.length);

				// generate header as byte[]
				ByteBuffer b = ByteBuffer.allocate(12);
				b.putInt(payloadChunk.length); // Plen append
				b.putInt(sequenceNumber);// append fragment sequence number
				b.putInt(lastIndicator);// append last packet indicator
				
				System.out.println("Sending segment with last segment Indicator set to " + lastIndicator);
				
				System.out.println("Sending segment with sequence number: " + sequenceNumber+ "\n");

				byte[] headerChunk = b.array(); // new byte array to become header
				//System.out.println("Header " + headerChunk + " generated with size: " + headerChunk.length);

				// concatenate header chunk and payloadChunk -> packet
				byte[] packet = new byte[headerChunk.length + payloadChunk.length];

				for (int ii = 0; ii < headerChunk.length; ii++) {
					packet[ii] = headerChunk[ii];
				}
				for (int jj = 0; jj < payloadChunk.length; jj++) {
					packet[headerChunk.length + jj] = payloadChunk[jj];
				}
				try 
				{
					senderSocket.send(new DatagramPacket(packet, packet.length, receiver.getAddress(), receiver.getPort()));
					
					System.out.println("Segment with sequence number " + (sequenceNumber) + " of " + (NoOfSegments - 1)
							+ ", a payload size " + payloadChunk.length + ", and total size " + packet.length
							+ " was transmitted \n");
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				byte[] ackRECBuffer = new byte[4];
				DatagramPacket  ACK;
				senderSocket.setSoTimeout((int) timeOut);
				
				try 
					{
					ACK = new DatagramPacket(ackRECBuffer, 4);//ack
					senderSocket.receive(ACK);
					
					} 
				
				catch (IOException e) 
				{
					idx -= 1;//like never happened, do loop again
					ackReceived = false;
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("\nACK NOT RECEIVED IN TIME. Retransmitting Segment.");
				}
				
				
				if(ackReceived == null) 
				{
					System.out.println("\n"+"ACK was recieved for segment " + sequenceNumber);
				}
			}
		} 
		catch (SocketException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return true;
	}
}