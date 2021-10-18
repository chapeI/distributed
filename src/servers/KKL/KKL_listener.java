package servers.KKL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class KKL_listener extends Thread {
    String date;
    KKL_listener() {
        System.out.println("starting a thread for listening. opening PORT 2170 (KKL-listener listening for requests)");  // <-- CHANGE CODE
    }
    KKL kkl = new KKL();

    public void run() {
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
                String r = new String(request.getData());
                String op = r.substring(0,2);
                System.out.println("KKL_Listener: op: " + op);

                // bookRoom()
                if(op.equals("BR")) {
                    String campus = r.substring(2, 5);
                    System.out.println("campus for booking: " + campus); // should always be kkl no?
                    String rno = r.substring(5, 6);
                    String date = r.substring(6, 9);
                    String time = r.substring(9, 13);
                    System.out.println("r unwrapped: " + rno + date + time);
                    kkl.bookroom("KKL", rno, date, time, "DVLSTEST");

                    // do we need to send anything back?
                }

                // getAvailability()
                if(op.equals("GA")) {
                    String date = r.substring(2, 5);
                    System.out.println("date: " + date);
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

//                String s = new String(request.getData());
//                this.date = s.trim();
//
//                System.out.println("KKL-Listener: processing request for available rooms on: " + this.date);
//                int c = kkl.get_count(date);
//                System.out.println("KKL-Listener: Processed. For " + date + ", available rooms is, count => " + c);
//
//
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
