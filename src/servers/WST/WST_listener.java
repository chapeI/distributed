package servers.WST;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class WST_listener extends Thread {
    String date;
    WST_listener() {
        System.out.println("WST_listener: starting a thread for listening. opening PORT 2171 (WST-listener listening for requests)");  // <-- CHANGE CODE
    }
    WST wst = new WST();

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(2171);  //  DVL: 2172, KKL: 2170, WST: 2171
            byte[] b = new byte[1000];

            while(true) {

                // RECEIVE
                DatagramPacket request = new DatagramPacket(b, b.length);
                socket.receive(request);
                System.out.println("\nWST-Listener: request received (dunno from who)");

                // PROCESS
                String r = new String(request.getData());
                String op = r.substring(0,2);
                System.out.println("WST_Listener: op: " + op);

                // bookRoom()
                if(op.equals("BR")) {
                    String campus = r.substring(2, 5);
                    System.out.println("campus for booking: " + campus); // should always be DVL no?
                    String rno = r.substring(5, 6);
                    String date = r.substring(6, 9);
                    String time = r.substring(9, 13);
                    System.out.println("r unwrapped: " + rno + date + time);
                    wst.bookroom("WST", rno, date, time, "WSTSTEST");
                    // do we need to send anything back?
                }

                // getAvailability()
                if(op.equals("GA")) {
                    String date = r.substring(2,5);
                    System.out.println("date: " + date);
                    System.out.println("WST-Listener: processing request for available rooms on: " + this.date);
                    int c = wst.get_count(date);
                    System.out.println("WST-Listener: Processed. For " + date + ", available rooms is, count => " + c);

                    // SEND
                    byte [] reply = Integer.toString(c).getBytes();
                    DatagramPacket responsePacket = new DatagramPacket(reply,
                            reply.length, request.getAddress(), request.getPort());
                    socket.send(responsePacket);
                    System.out.println("WST-Listener: sending count " + c + " to the requester");
                }
            }
        }
        catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if(socket != null) {
                socket.close();
                System.out.println("WST-Listener: closing WST socket. shouldn't see this");
            }
        }
    }
}
