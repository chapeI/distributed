package sWST;

import sKKL.KKL;

import javax.xml.ws.Endpoint;

public class start {
    public static void main(String[] args) {
        WST_listener l=new WST_listener();
        Thread t =new Thread(l);
        t.start();

        Endpoint ep = Endpoint.publish("http://localhost:8082/dalailama", new WST());
        System.out.println("endPoint published: " + ep.isPublished());

    }
}
