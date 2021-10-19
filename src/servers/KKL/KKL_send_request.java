package servers.KKL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class KKL_send_request extends Thread {
    public String response = "initial response";
    String request;
    int port;
    public KKL_send_request(String request, int port) {
        this.request = request;  // TODO: change all from request to data
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
            System.out.println("KKL_sender: sending a request to " + port + ". (go there)");

            // RECEIVE
            byte[] r = new byte[1024];
            DatagramPacket receiving = new DatagramPacket(r, r.length);
            socket.receive(receiving);
            String response = new String(receiving.getData()).trim();
            System.out.println("KKL_sender: receives back: " + response + ". Storing in sender. KKL can access KKLsender.response");
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
                System.out.println("KKL_sender: closing KKL sending socket");
            }
        }
    }
}