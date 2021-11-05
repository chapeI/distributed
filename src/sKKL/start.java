package sKKL;

import javax.xml.ws.Endpoint;

public class start {
    public static void main(String args[]) {
        KKL_listener lt=new KKL_listener();
        Thread t =new Thread(lt);
        t.start();

        Endpoint ep = Endpoint.publish("http://localhost:8081/cal", new KKL());
        System.out.println("endPoint published: " + ep.isPublished());
    }
}
