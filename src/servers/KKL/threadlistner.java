package servers.KKL;

import servers.DVL.DVL_i;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class threadlistner extends Thread {

    public int count;
//    private int c=0;
//    private int d=0;
    String date;
    String uid;
    public int count_for_DVLS123_for_DATE1 = 2;
//    KKL_i kkl;
    KKL kkl = new KKL();



    threadlistner(int c ,int d, String date, String uid) throws RemoteException {
//        this.c=c;
//        this.d=d;
//        this.date=date;
        this.uid = uid;
    }

    public void run() {
//        System.out.println("KKL server: listener started");
//        System.out.println("uid: " + uid);
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(2170);  // hardcoding PORT, instead of reading in d
            byte[] b = new byte[1000];  // b=Integer.toString(i).getBytes();  // I really don't think you need this mannn

            while(true) {

                // RECEIVING
                DatagramPacket packet = new DatagramPacket(b, b.length);
                socket.receive(packet);

                // TESTING
                String s = new String(packet.getData());
//                System.out.println("expecting to see a date here (Tuesday). in s(KKL) the date is: " + s.trim());
                this.date = s.trim();

                int c = kkl.get_count(date);
//                System.out.println("c: " + c);

                // PROCESSING: return the count from date we receive from socket. we already have uid
                int count = this.count_for_DVLS123_for_DATE1;
                byte [] reply = Integer.toString(2).getBytes();


                // SENDING
                DatagramPacket responsePacket = new DatagramPacket(reply,
                        reply.length, packet.getAddress(), packet.getPort());
                socket.send(responsePacket);
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
