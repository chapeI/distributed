package servers.WST;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class WST_Sender extends Thread {
    public int count;
    String date;
    int port;

    public WST_Sender(String date, int port) {
        this.date = date;
        this.port = port;
    }

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();

            // SEND
            byte[] b = this.date.getBytes();
            InetAddress address = InetAddress.getLocalHost();
            DatagramPacket packet =new DatagramPacket(b, b.length, address, port); // port has to be a variable
            socket.send(packet);
            System.out.println("WST-Sender: sending a request somewhere: " + port + ". (go here)");
            System.out.println("--");

            // RECEIVE
            byte[] r = new byte[1024];
            DatagramPacket response = new DatagramPacket(r, r.length);
            socket.receive(response);
            String s = new String(response.getData());
            String s_ = s.trim();
            System.out.println("WST-Sender: receives back: " + s_ + ". Storing in WST-Sender. WST can access count from WST-Sender");
            this.count = Integer.parseInt(s_);
        }
        catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            if(socket != null) {
                socket.close();
                System.out.println("WST-Sender: closing WST sending socket");
            }
        }
    }

}
