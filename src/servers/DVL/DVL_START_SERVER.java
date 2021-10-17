package servers.DVL;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

//import java.rmi.RemoteException;
//import java.rmi.registry.Registry;

//import common.c;
//import common.cHelper;
import common.*;

// all we're doing w s1_binding is saying,
// hey we have a powerful object.
// you can use it at this port

// TODO: look at slide 17 and see if we can copy code from there. like the way we're porting

public class DVL_START_SERVER {
    public static void main(String args[]) throws Exception {
        // why do we need a listening thread here
        ListenerThread lt = new ListenerThread();
        Thread t1 = new Thread(lt);
        t1.start();

        // timer code

        // RMI
//        Registry re=java.rmi.registry.LocateRegistry.createRegistry(35000); // TODO: change this to the way prof binds. (w "rmi:localhost")
//        DVL dvl =new DVL();
//        // bind powerful object to port, so client can invoke port
//        re.rebind("tag1", dvl);
////        System.out.println("DVL Server running");

        // orb code
        try{
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            DVL helloImpl = new DVL();
            helloImpl.setORB(orb);

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
            c href = cHelper.narrow(ref);

            // get the root naming context
            // NameService invokes the name service
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            // Use NamingContextExt which is part of the Interoperable Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            String name = "Hello1";
            NameComponent path[] = ncRef.to_name( name );
            ncRef.rebind(path, href);
            System.out.println("HelloServer ready and waiting ...");
            // wait for invocations from clients
            orb.run();

        }
        catch (Exception e)
        {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }
        System.out.println("HelloServer Exiting ...");
    }
}
