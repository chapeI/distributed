package servers.KKL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class Listener extends Thread {
    String date;
    Listener() {}
    KKL kkl = new KKL();

    public void run() {
        System.out.println("KKL PORT 2170: open (listening for requests)");  // <-- CHANGE CODE
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(2170);  // (D-FIX-VL: 2172, KKL: 2170, WST: 2171)
            byte[] b = new byte[1000];

            while(true) {
                // RECEIVE
                DatagramPacket packet = new DatagramPacket(b, b.length);
                socket.receive(packet);
                System.out.println("\nKKL-Listener: request received (dunno from who)");

                // PROCESS
                String s = new String(packet.getData());
                this.date = s.trim();

                System.out.println("KKL-Listener: processing request for available rooms on: " + this.date);
                int c = kkl.get_count(date);
                System.out.println("KKL-Listener: Processed. For " + date + ", available rooms is, count => " + c);

                // SEND

                byte [] reply = Integer.toString(c).getBytes();
                DatagramPacket responsePacket = new DatagramPacket(reply,
                        reply.length, packet.getAddress(), packet.getPort());
                socket.send(responsePacket);
                System.out.println("KKL-Listener: sending count " + c + " to the requester");
            }
        }
        catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if(socket != null) {
                socket.close();
                System.out.println("KKL-Listener: closing KKL socket. shouldn't see this");
            }
        }
    }
}
