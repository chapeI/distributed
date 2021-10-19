package servers.KKL;

import common.c;
import common.cHelper;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class start {
    public static void main(String args[]) {
        KKL_listening_for_requests lt=new KKL_listening_for_requests();
        Thread t =new Thread(lt);
        t.start();

        try{
            ORB orb = ORB.init(args, null);
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            KKL helloImpl = new KKL();
            helloImpl.setORB(orb);
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
            c href = cHelper.narrow(ref);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            String name = "Hello2";  // <-- CHANGE THIS
            NameComponent path[] = ncRef.to_name( name );
            ncRef.rebind(path, href);
            System.out.println("KKL Server ready and waiting ...");
            orb.run();
        }
        catch (Exception e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.out);
        }
        System.out.println("KKL Server Exiting ...");
    }

}
