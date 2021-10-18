package servers.KKL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class listener extends Thread {
    String date;
    listener() {}
    KKL kkl = new KKL();

    public void run() {
        System.out.println("starting a thread for listening. opening PORT 2170 (KKL-listener listening for requests)");  // <-- CHANGE CODE
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(2170);  // (D-FIX-VL: 2172, KKL: 2170, WST: 2171)
            byte[] b = new byte[1000];

            while(true) {
                // RECEIVE
                DatagramPacket request = new DatagramPacket(b, b.length);
                socket.receive(request);
                System.out.println("\nKKL-Listener: request received (dunno from who)");

                // PROCESS
                String s = new String(request.getData());
                this.date = s.trim();

                System.out.println("KKL-Listener: processing request for available rooms on: " + this.date);
                int c = kkl.get_count(date);
                System.out.println("KKL-Listener: Processed. For " + date + ", available rooms is, count => " + c);

                // SEND

                byte [] reply = Integer.toString(c).getBytes();
                DatagramPacket responsePacket = new DatagramPacket(reply,
                        reply.length, request.getAddress(), request.getPort());
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
