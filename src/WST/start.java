package WST;

public class start {
    public static void main(String[] args) {
        WST_listening_for_requests l=new WST_listening_for_requests();
        Thread t =new Thread(l);
        t.start();

//        try{
//            ORB orb = ORB.init(args, null);
//            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
//            rootpoa.the_POAManager().activate();
//            WST helloImpl = new WST();
//            helloImpl.setORB(orb);
//            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
//            c href = cHelper.narrow(ref);
//            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
//            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
//            String name = "Hello3";  // <-- CHANGE THIS
//            NameComponent path[] = ncRef.to_name( name );
//            ncRef.rebind(path, href);
//            System.out.println("WST Server ready and waiting ...");
//            orb.run();
//        }
//        catch (Exception e) {
//            System.err.println("ERROR: " + e);
//            e.printStackTrace(System.out);
//        }
        System.out.println("WST Server Exiting ...");
    }
}
