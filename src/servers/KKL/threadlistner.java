package servers.KKL;

import servers.DVL.DVL_i;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class threadlistner extends Thread {

    public int count;
    String date;
    KKL_i k;

    threadlistner() throws RemoteException, MalformedURLException, NotBoundException {
        k = (KKL_i) Naming.lookup("rmi://localhost:35001/tag2");
    }

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(2170);  // listening on 2170
            byte[] b = new byte[1000];

            while(true) {

                // RECEIVE
                DatagramPacket packet = new DatagramPacket(b, b.length);
                socket.receive(packet);
                System.out.println("receiving on 2170");

                // PROCESS
                String s = new String(packet.getData());
                this.date = s.trim();
//                System.out.println(date);
//                int c = kkl.get_count(date);
                int c = k.get_count(date);
                System.out.println("kkl attempt to send count " + c + " to DVL");

                // SEND
                byte [] reply = Integer.toString(c).getBytes();
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
                System.out.println("closing KKL socket. probably should never see this");
            }
        }
    }
}
