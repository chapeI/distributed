package sWST;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class WST_listener extends Thread {
    WST_listener() {
        System.out.println("\nWST_listener: starting a thread for listening. opening PORT 2171 (WST-listener listening for requests)");  // <-- CHANGE CODE
    }
    WST wst = new WST();

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(2171);  //  DVL: 2172, KKL: 2170, WST: 2171
            byte[] b = new byte[1000];

            while(true) {

                String response = "WST_listening: see comment"; // if you're seeing this, the response in one of the methods is incomplete

                // RECEIVE
                DatagramPacket request = new DatagramPacket(b, b.length);
                socket.receive(request);
                System.out.println("\nWST-Listener: a request was received (dunno from who)");

                // PROCESS
                String r = new String(request.getData());
                String op = r.substring(0,2);
                System.out.println("WST_Listener: operation: " + op);

                // bookRoom()
                if(op.equals("BR")) {
                    String campus = r.substring(2, 5);
                    System.out.println("campus for booking: " + campus); // should always be DVL no?
                    String rno = r.substring(5, 6);
                    String date = r.substring(6, 9);
                    String time = r.substring(9, 13);
                    System.out.println("r unwrapped: " + rno + date + time);
                    response = wst.bookroom("WST", rno, date, time, "WSTSTEST");
                }

                // getAvailability()
                if(op.equals("GA")) {
                    String date = r.substring(2, 5);
                    int count = wst.get_count(date);
                    String c = Integer.toString(count);
                    response = c;
                }

                if(op.equals("CB")) {
                    String bookingid = r.substring(2, 38);
                    System.out.println("WST_Listener: bookingid: " + bookingid);
                    response = wst.cancelBooking(bookingid, "WST");
                }

                // SEND
                byte [] reply = response.getBytes();
                DatagramPacket responsePackets = new DatagramPacket(reply,
                        reply.length, request.getAddress(), request.getPort());
                socket.send(responsePackets);
                System.out.println("WST-Listener: Request processed. sending response (" + response + ") back to sender");

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
