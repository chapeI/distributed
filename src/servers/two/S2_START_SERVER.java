package servers.two;

import servers.one.S1;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class S2_START_SERVER {
    public static void main(String args[]) throws RemoteException
    {
        // create port
        Registry re=java.rmi.registry.LocateRegistry.createRegistry(35001); // TODO: change this to the way prof binds. (w "rmi:localhost")

        // create powerful object
        S2 s2=new S2();

        // bind powerful object to port, so client can invoke port
        re.rebind("tag2", s2);
        System.out.println("server 2 running");
    }
}
