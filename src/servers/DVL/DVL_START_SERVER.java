package servers.DVL;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

// all we're doing w s1_binding is saying,
// hey we have a powerful object.
// you can use it at this port

// TODO: look at slide 17 and see if we can copy code from there. like the way we're porting

public class DVL_START_SERVER {
    public static void main(String args[]) throws RemoteException
    {
        // create port
        Registry re=java.rmi.registry.LocateRegistry.createRegistry(35000); // TODO: change this to the way prof binds. (w "rmi:localhost")

        // create powerful object
        DVL dvl =new DVL();

        // bind powerful object to port, so client can invoke port
        re.rebind("tag1", dvl);
        System.out.println("DVL Server running");
    }
}
