package servers.KKL;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class KKL_START_SERVER {
    public static void main(String args[]) throws RemoteException
    {
        // create port
        Registry re=java.rmi.registry.LocateRegistry.createRegistry(35001); // TODO: change this to the way prof binds. (w "rmi:localhost")

        // create powerful object
        KKL kkl =new KKL();

        // bind powerful object to port, so client can invoke port
        re.rebind("tag2", kkl);
        System.out.println("KKL Server running");
    }
}
