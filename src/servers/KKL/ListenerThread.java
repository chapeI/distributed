package servers.KKL;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ListenerThread extends Thread {

    String date;
    KKL_i kkl_i;

    ListenerThread() throws RemoteException, MalformedURLException, NotBoundException {
        kkl_i = (KKL_i) Naming.lookup("rmi://localhost:35001/tag2");
    }

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(2170);
            byte[] b = new byte[1000];

            while(true) {

                // RECEIVE
                DatagramPacket packet = new DatagramPacket(b, b.length);
                socket.receive(packet);
                System.out.println("receiving on KKL PORT 2170");

                // PROCESS
                String s = new String(packet.getData());
                this.date = s.trim();
                int c = kkl_i.get_count(date);
                System.out.println("kkl attempt to send count " + c);

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
