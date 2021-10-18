package servers.DVL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.net.SocketException;

public class listener extends Thread {
    String date;
    listener() {
        System.out.println("starting a thread for listening. opening PORT 2172 (DVL-listener listening for requests)");  // <-- CHANGE CODE
    }
    DVL dvl = new DVL();

    public void run() {
        DatagramSocket socket = null;
        try {
//            System.out.println("DVL-Listener: run()");
            socket = new DatagramSocket(2172);  //  DVL: 2172, KKL: 2170, WST: 2171
            byte[] b = new byte[1000];

            while(true) {
                // RECEIVE
                DatagramPacket request = new DatagramPacket(b, b.length);
                socket.receive(request);
                System.out.println("\nDVL-Listener: request received (dunno from who)");
                System.out.println("reached-4");

                // PROCESS
                String r = new String(request.getData());
                System.out.println("r: " + r.trim());



//                this.date = r.trim();
//                System.out.println("DVL-Listener: processing request for available rooms on: " + this.date);
//                int c = dvl.get_count(date);
//                System.out.println("DVL-Listener: Processed. For " + date + ", available rooms is, count => " + c);
//
//                // SEND
//                byte [] reply = Integer.toString(c).getBytes();
//                DatagramPacket responsePacket = new DatagramPacket(reply,
//                        reply.length, request.getAddress(), request.getPort());
//                socket.send(responsePacket);
//                System.out.println("DVL-Listener: sending count " + c + " to the requester");
            }
        }
        catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if(socket != null) {
                socket.close();
                System.out.println("DVL-Listener: closing DVL socket. shouldn't see this");
            }
        }
    }
}
