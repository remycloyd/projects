import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import edu.utulsa.unet.UDPSocket;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;


public class RReceiveUDP implements edu.utulsa.unet.RReceiveUDPI {

	public static void main(String[] args) {
		RReceiveUDP receiver = new RReceiveUDP();
		receiver.setMode(0);
		receiver.setModeParameter(512);
		LocalDateTime x =LocalDateTime.now();
		receiver.setFilename("received_"+x.toString());//("receivedfile.pdf");//("receivedfile.gif");
		receiver.setLocalPort(32456);
		receiver.receiveFile();
	}

	int receiver = 12987;

	public int getLocalPort() {
		return receiver;
	}

	public boolean setLocalPort(int portNum) {
		if (portNum >= 0) {
			receiver = portNum;
			return true;
		}
		return false;
	}

	public enum Mode {
		stopAndWait
	}

	Mode mode = Mode.stopAndWait;
	String filename;
	

	public boolean setModeParameter(long arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getFilename() {
		return filename;
	}

	public int getMode() {
		switch (mode) {
		case stopAndWait:
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
			mode = Mode.stopAndWait;
			return true;
		default:
			return false;
		}
	}
//global variables
	InetSocketAddress sender;
	Long timeout;
	UDPSocket receiveSocket;
	//UDPSocket receiveSocket;
	int mtu;
	boolean ackReceived;
	long finishedTimeout;
	//byte[] sequenceNumberByte = new byte[4];
	byte[] ackBuffer = new byte[4];
	

	@Override
	public boolean receiveFile() {

		Long startTime = System.currentTimeMillis();
		try {

			receiveSocket = new UDPSocket(receiver);
			mtu = receiveSocket.getSendBufferSize();
			System.out.println("prepared to receive, data pipe Volume set to " + mtu + " Bytes");
			byte[] fileBuffer = new byte[mtu + 12];

			
			ByteBuffer headerBuffer = ByteBuffer.allocate(4);
			File incomingFile = new File(this.filename);
			FileOutputStream fos = new FileOutputStream(incomingFile);

			int pLen = 0;
			int sequenceNumber = 0;
			int lastIndicator = 0;
			int x = 0;

			while (true) 
			{
				DatagramPacket incomingPacket = new DatagramPacket(fileBuffer, fileBuffer.length);
				receiveSocket.receive(incomingPacket);
				headerBuffer.clear();
				headerBuffer.put(fileBuffer[0]).put(fileBuffer[1]).put(fileBuffer[2]).put(fileBuffer[3]);
				headerBuffer.rewind();
				pLen = headerBuffer.getInt();

				headerBuffer.clear();
				headerBuffer.put(fileBuffer[4]).put(fileBuffer[5]).put(fileBuffer[6]).put(fileBuffer[7]);
				headerBuffer.rewind();
				sequenceNumber = headerBuffer.getInt();
				
				ackBuffer = RSendUDP.intToBytes(sequenceNumber);
				DatagramPacket ackPacket = new DatagramPacket(ackBuffer, 4, incomingPacket.getSocketAddress());
				receiveSocket.send(ackPacket);//send ack to sender

				System.out.println("Segment with sequence number = " + sequenceNumber + " received. Corresponding Ack was sent to sender. \n ");

				headerBuffer.clear();
				headerBuffer.put(fileBuffer[8]).put(fileBuffer[9]).put(fileBuffer[10]).put(fileBuffer[11]);
				headerBuffer.rewind();
				lastIndicator = headerBuffer.getInt();

				byte[] fileData = new byte[pLen];
				System.arraycopy(fileBuffer, 12, fileData, 0, pLen);
				fos.write(fileData);

				System.out.println("Segment " + x + " was written to the array: fileData, waiting for all segments to begin Demux.");
				System.out.println("lastIndicator extracted: " + lastIndicator);
				System.out.println("sequence number extracted:" + sequenceNumber);
				x += 1;
				if(lastIndicator == 1)
					{
					break;
					}
			}
			System.out.println("no more segments queued, breaking out of receiver loop.");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		Long stopTime = System.currentTimeMillis();
		System.out.println("Number of seconds to receive file: " + ((stopTime - startTime) / 1000.));
//			System.out.println("Successfully transferred " + (fileData.length) + " of " + this.filename +" at "
//			+socket.getLocalAddress().getCanonicalHostName()+":"+this.getLocalPort()+" in "+ ((stopTime-startTime)/1000.)+" seconds");
		return false;
	}

}