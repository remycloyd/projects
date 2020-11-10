import java.net.InetSocketAddress;

public class Driver
{
    public static void main(String[] args)
    {
        //this will run sender and receiver

        String fname = "suavemente.txt";
        RSendUDP sender = new RSendUDP();
        sender.setMode(0);
        sender.setFilename(fname);
        sender.setModeParameter(512);
        sender.setTimeout(1000);
        sender.setReceiver(new InetSocketAddress("localhost", 32456));
        RReceiveUDP rcvr = new RReceiveUDP();
        rcvr.setLocalPort(32456);
        rcvr.setFilename("recieved.txt");
        new Thread() 
        {
            public void run()
            {
                rcvr.receiveFile();
                Thread.currentThread().stop();
            }
        }.start();
        sender.sendFile();
    }
}
