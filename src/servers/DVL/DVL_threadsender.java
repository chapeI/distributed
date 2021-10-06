package servers.DVL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class DVL_threadsender extends Thread {

    private int c = 0;
    public int count,d;
    public boolean ready = false;
    int port;
    String date;

    public DVL_threadsender(int c, int port, String date)
    {
//        this.c=c;
//        this.d=d;  // d was port, delete after
        this.date = date;
    }

    public void run() {
//        int e1;
//        System.out.println(" Inside DVL server");

        DatagramSocket socket = null;

        try {
//            System.out.println("did I reach");

            socket = new DatagramSocket();

            // SENDING
//            int n = 2;  // dont send an int, just send a date..
//            byte [] b = Integer.toString(2).getBytes();
//            byte [] b = String.valueOf(2).getBytes();
            byte[] b = this.date.getBytes();  // we should be sending date.

//            InetAddress address = InetAddress.getByName("localhost");
            InetAddress address = InetAddress.getLocalHost();
            DatagramPacket packet =new DatagramPacket(b, b.length, address, 2170);
            socket.send(packet);

            // RECEIVING
            byte[] r = new byte[1024];
            DatagramPacket response = new DatagramPacket(r, r.length);
            socket.receive(response);
//            byte  d1[]=(response.getData());
//            e1= ByteBuffer.wrap(d1).getInt();
//            count=e1;
//            System.out.println("e1: " + e1);

            String s = new String(response.getData());
            System.out.println("expecting count number 2, s: " + s.trim());

            // TODO: count = s.toInteger()
            ready =true;

        }
        catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            if(socket != null) {
                socket.close();
                System.out.println("socket(DDL) closed");
            }
        }
    }
}
