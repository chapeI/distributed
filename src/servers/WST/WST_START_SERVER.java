package servers.WST;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class WST_START_SERVER {
    public static void main(String[] args) throws RemoteException {
        Registry re=java.rmi.registry.LocateRegistry.createRegistry(35002);
        WST wst = new WST();

        re.rebind("tag3", wst);
//        System.out.println("WST server running");

    }
}
