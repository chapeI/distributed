package servers.KKL;

import servers.DVL.DVL_i;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class threadlistner extends Thread {

    public int count;
    private int c=0;
    private int d=0;
    String date;
    String uid;

    threadlistner(int c ,int d, String date, String uid) {
//        this.c=c;
//        this.d=d;
//        this.date=date;
        this.uid = uid;
    }

    public void run() {
        System.out.println("KKL server: listener started");
        System.out.println("are we getting UID properly, uid: " + uid);
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(2170);  // hardcoding PORT, instead of reading in d
            byte[] b = new byte[1000];  // b=Integer.toString(i).getBytes();  // I really don't think you need this mannn

            while(true) {

                // RECEIVING
                DatagramPacket packet = new DatagramPacket(b, b.length);
                socket.receive(packet);

                // PROCESSING: return the count based on uid+date in the kkl server

                // SENDING
                DatagramPacket response = new DatagramPacket(packet.getData(),
                        packet.getLength(), packet.getAddress(), packet.getPort());
                // TESTING
//                String s = new String(response.getData());
//                System.out.println("s(KKL): " + s.trim());
                socket.send(response);
            }
        }
        catch (SocketException e) {
            System.out.println("SocketException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if(socket != null) {
                socket.close();
                System.out.println("closing KKL? socket");
            }
        }
    }
}
