package servers.DVL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class threadsender3 extends Thread {

    private int c = 0;
    public int count,d;
    public boolean ready = false;

    public threadsender3(int c, int d)
    {
        this.c=c;
        this.d=d;
    }

    public void run()
    {
        int e1;
        System.out.println(" Inside DVL server");
        DatagramSocket aSocket = null;
        try {
            System.out.println("did I reach");

            aSocket = new DatagramSocket();

            byte [] m1 = Integer.toString(5).getBytes();  // sending 5 instead of c
            InetAddress aHost = InetAddress.getByName("localhost");  // do we need localhost? test w s.out. can use getLocalHost()
            System.out.println("localhost?: " + aHost);
            int serverPort = 2170;
            DatagramPacket request =new DatagramPacket(m1, m1.length, aHost, serverPort); // changed _.length
            aSocket.send(request);

            System.out.println("where am i getting stuck tho");
            byte[] buffer1 = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer1, buffer1.length);
            aSocket.receive(reply);
            System.out.println("here?");

//            byte  d1[]=(reply.getData());
//            e1= ByteBuffer.wrap(d1).getInt();
//            count=e1;
//            System.out.println("e1: " + e1);

            String s = new String(reply.getData());
            System.out.println("s: " + s.trim());

            ready =true;

        }
        catch (SocketException e)
        {	System.out.println("Socket: " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("IO: " + e.getMessage());
        }
        finally
        {
            if(aSocket != null)
                aSocket.close();
        }
    }
}
