package servers.WST;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class WST_send_request extends Thread {
    public String response;
    String request;
    int port;
    public WST_send_request(String request, int port) {
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
            System.out.println("WST_sender sending request to " + port + ". (DVL-2172, KKL-2170, WST-2171)");
            System.out.println("--");

            // RECEIVE
            byte[] r = new byte[1024];
            DatagramPacket response = new DatagramPacket(r, r.length);
            socket.receive(response);
            String s = new String(response.getData()).trim();
            System.out.println("WST_sender receiving back: " + s + ". Storing in this sender thread. Remember WST can access sender.response");
            synchronized (this) {
                this.response = s;
            }
        }
        catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            if(socket != null) {
                socket.close();
                System.out.println("sender: closing WST sending socket");
            }
        }
    }

}
