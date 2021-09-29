package client;
import servers.one.S1_i;
import java.rmi.Naming;

public class Client {

    // start_client starts the client. can access remote objects from here
    void start_client() throws Exception {
        S1_i s1i;
        s1i=(S1_i) Naming.lookup("rmi://localhost:35000/tag1");

        String s = s1i.createroom();
        System.out.println(s);
    }
}
