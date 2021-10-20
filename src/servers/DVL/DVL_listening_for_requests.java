package servers.DVL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.net.SocketException;

public class DVL_listening_for_requests extends Thread {
//    String date;
    DVL_listening_for_requests() {
        System.out.println("DVL-Listener: starting a thread for listening. opening PORT 2172 (DVL-listener listening for requests)");  // <-- CHANGE CODE
    }
    DVL dvl = new DVL();

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(2172);  //  DVL: 2172, KKL: 2170, WST: 2171
            byte[] b = new byte[1000];

            while(true) {
                String response = "DVL_listening: ERROR"; // if you're seeing this, the response in one of the methods is incomplete

                // RECEIVE
                DatagramPacket request = new DatagramPacket(b, b.length);
                socket.receive(request);
                System.out.println("\nDVL-Listener: a request was received (dunno from who)");

                // PROCESS
                String r = new String(request.getData());
                String op = r.substring(0,2);
                System.out.println("DVL_Listener: operation: " + op);

                // bookRoom()
                if(op.equals("BR")) {
                    String campus = r.substring(2, 5);
                    System.out.println("DVL_listener: campus for booking: " + campus); // should always be DVL no?
                    String rno = r.substring(5, 6);
                    String date = r.substring(6, 9);
                    String time = r.substring(9, 13);
                    System.out.println("DVL_listener: r unwrapped: " + rno + date + time);
                    response = dvl.bookroom("DVL", rno, date, time, "DVLSTEST");
                }

                // getAvailability()
                if(op.equals("GA")) {
                    String date = r.substring(2,5);
//                    System.out.println("DVL-Listener: date: " + date);
                    System.out.println("DVL-Listener: processing request for available rooms on: " + date);
                    int c = dvl.get_count(date);
                    System.out.println("DVL-Listener: Processed. For " + date + ", available rooms is, count => " + c);

                }

                if(op.equals("CB")) {
                    String bookingid = r.substring(2, 38);
//                    System.out.println("DVL_Listener: bookingid: " + bookingid);
                    response = dvl.cancelBooking(bookingid, "DVL");
                }

                // SEND
                byte [] reply = response.getBytes();
                DatagramPacket responsePackets = new DatagramPacket(reply,
                        reply.length, request.getAddress(), request.getPort());
                socket.send(responsePackets);
                System.out.println("DVL-Listener: processed request. sending response (" + response + ") back to sender");
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
