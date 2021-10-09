package servers.WST;

import servers.KKL.KKL_i;

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
    WST_i wst_i;

    ListenerThread() throws RemoteException, MalformedURLException, NotBoundException {
        wst_i = (WST_i) Naming.lookup("rmi://localhost:35002/tag3");
    }

    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(2171);
            byte[] b = new byte[1000];

            while(true) {

                // RECEIVE
                DatagramPacket packet = new DatagramPacket(b, b.length);
                socket.receive(packet);
                System.out.println("receiving on WST PORT 2171");

                // PROCESS
                String s = new String(packet.getData());
                this.date = s.trim();
                int c = wst_i.get_count(date);
                System.out.println("wst attempt to send count " + c);

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
                System.out.println("closing WST socket. probably should never see this");
            }
        }
    }
}