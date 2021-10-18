package servers.KKL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class Listener extends Thread {
    String date;
    Listener() {}

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(2170);  // <-- ABSOLUTELY CHANGE PORT  (DVL: 2172, KKL: 2170, WST: 2171)
            byte[] b = new byte[1000];

            while(true) {
                // RECEIVE
                DatagramPacket packet = new DatagramPacket(b, b.length);
                socket.receive(packet);
                System.out.println("KKL PORT 2170: open (listening for requests)");  // <-- CHANGE CODE

                // PROCESS
                String s = new String(packet.getData());
                this.date = s.trim();
//                int c = dvl_i.get_count(date);
//                System.out.println("dvl attempt to send count " + c);

                // SEND
//                byte [] reply = Integer.toString(c).getBytes();
//                DatagramPacket responsePacket = new DatagramPacket(reply,
//                        reply.length, packet.getAddress(), packet.getPort());
//                socket.send(responsePacket);
            }
        }
        catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if(socket != null) {
                socket.close();
                System.out.println("closing DVL socket. shouldn't see this");
            }
        }
    }
}
