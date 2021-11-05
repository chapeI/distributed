package sDVL;

import javax.xml.ws.Endpoint;

public class start {
    public static void main(String args[]) {
//        DVL_listener listening_thread = new DVL_listener();
//        Thread t =new Thread(listening_thread);
//        t.start();

        Endpoint ep = Endpoint.publish("http://localhost:8080/cal", new DVL());
        System.out.println("endPoint published: " + ep.isPublished());

//        try{
//            ORB orb = ORB.init(args, null);
//            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
//            rootpoa.the_POAManager().activate();
//            DVL helloImpl = new DVL();  // <-- CHANGE CODE
//            helloImpl.setORB(orb);
//            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
//            c href = cHelper.narrow(ref);
//            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
//            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
//            String name = "Hello1";  // <-- CHANGE CODE
//            NameComponent path[] = ncRef.to_name( name );
//            ncRef.rebind(path, href);
//            System.out.println("DVL Server ready and waiting ...");  // <-- CHANGE CODE
//            orb.run();
//        }
//        catch (Exception e) {
//            System.err.println("ERROR: " + e);
//            e.printStackTrace(System.out);
//        }
//        System.out.println("DVL Server Exiting ...");  // <-- CHANGE CODE
    }
}
