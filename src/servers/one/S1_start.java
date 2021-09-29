package servers.one;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class S1_start {
    public static void main(String args[]) throws RemoteException
    {
        S1 s1=new S1();
        Registry re=java.rmi.registry.LocateRegistry.createRegistry(35000);
        re.rebind("tag1", s1);
        System.out.println("server 1 running");
    }
}
