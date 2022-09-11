package sDVL;

import javax.xml.ws.Endpoint;

public class start {
    public static void main(String args[]) {
        DVL_listener listening_thread = new DVL_listener();
        Thread t =new Thread(listening_thread);
        t.start();

        Endpoint ep = Endpoint.publish("http://localhost:8080/dalailama", new DVL());
        System.out.println("endPoint published: " + ep.isPublished());

    }
}
