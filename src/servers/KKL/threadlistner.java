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

    threadlistner(int c ,int d, String date) {
        this.c=c;
        this.d=d;
        this.date=date;
    }

    public void run() {
        System.out.println("KKL server: listener started");
        DatagramSocket dSocket = null;
        try
        {
//            int i= DVL_i.roomcount;

            dSocket = new DatagramSocket(2170);  // hardcoding port, instead of reading from d
            byte[] buffer4 = new byte[1000];
//            buffer4=Integer.toString(i).getBytes();  // I really don't think you need this mannn
            while(true)
            {
                {
//                    DatagramPacket request2 = new DatagramPacket(buffer4, buffer4.length);
                    DatagramPacket request4 = new DatagramPacket(buffer4, buffer4.length);
                    System.out.println("yo am I really listening");

                    dSocket.receive(request4);

                    System.out.println("ok try to reach this.");

                    DatagramPacket reply = new DatagramPacket(request4.getData(),
                            request4.getLength(), request4.getAddress(), request4.getPort());

                    String s = new String(reply.getData());
                    System.out.println("s(KKL): " + s.trim());


                    dSocket.send(reply);
                }
            }
        }
        catch (SocketException e)
        {
            System.out.println("Socket: " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("IO: " + e.getMessage());

        }
        finally
        {
            if(dSocket != null)
                dSocket.close();
        }
    }
}
