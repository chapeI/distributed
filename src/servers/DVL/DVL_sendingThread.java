package servers.DVL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class DVL_sendingThread extends Thread {
    public int count;
//    public boolean ready = false;
    String date;

    public DVL_sendingThread(String date) {
        this.date = date;
    }

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();

            // SEND
            byte[] b = this.date.getBytes();
            InetAddress address = InetAddress.getLocalHost();
            DatagramPacket packet =new DatagramPacket(b, b.length, address, 2170); // port has to be a variable
            socket.send(packet);

            // RECEIVE
            byte[] r = new byte[1024];
            DatagramPacket response = new DatagramPacket(r, r.length);
            socket.receive(response);
            String s = new String(response.getData());
            String s_ = s.trim();
            System.out.println("dvl_sendingThread receiving: " + s_);
            this.count = Integer.parseInt(s_);
//            ready =true;
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
