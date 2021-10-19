package servers.DVL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class DVL_sender extends Thread {
    public String response;
    String request;
    int port;
    public DVL_sender(String request, int port) {
        this.request = request;
        this.port = port;
    }

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();

            // SEND
            byte[] b = this.request.getBytes();
            InetAddress address = InetAddress.getLocalHost();
            DatagramPacket packet =new DatagramPacket(b, b.length, address, port); // port has to be a variable
            socket.send(packet);
            System.out.println("DVL_sender: sending a request to " + port + ". (go here)");
            System.out.println("--");

            // RECEIVE
            byte[] r = new byte[1024];
            DatagramPacket receiving = new DatagramPacket(r, r.length);
            socket.receive(receiving);
            String response = new String(receiving.getData()).trim();
            System.out.println("DVL_sender: receives back: " + response + ". Storing in sendingThread. DVL can access DVL_sender.response");
            synchronized (this) {
                this.response = response;
            }
        }
        catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            if(socket != null) {
                socket.close();
                System.out.println("DVL_sender: closing DVL sending socket");
            }
        }
    }
}