package client;
import client.clientTypes.Admin;
import client.clientTypes.Student;
import org.omg.CORBA.ORBPackage.InvalidName;
//import servers.DVL.DVL_i;
//import servers.KKL.KKL_i;
//import servers.WST.WST_i;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.lang.*;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import common.c;
import common.cHelper;

public class Client {
    static c c1, c2, c3;  // add impl tag?

    public static void main(String[] args) throws Exception {
        String uid;
        Admin admin;
        Student student;

//        DVL_i dvl_i = (DVL_i) Naming.lookup("rmi://localhost:35000/tag1");
//        KKL_i kkl_i = (KKL_i) Naming.lookup("rmi://localhost:35001/tag2");
//        WST_i wst_i = (WST_i) Naming.lookup("rmi://localhost:35002/tag3");

        ORB orb = ORB.init(args, null);
        org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
        c1 = cHelper.narrow(ncRef.resolve_str("Hello1"));
        c2 = cHelper.narrow(ncRef.resolve_str("Hello2"));
        c3 = cHelper.narrow(ncRef.resolve_str("Hello3"));

        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter ID");
        uid = br.readLine();


        if(uid.charAt(3)=='S') {
            student = new Student();
//            student.run_student(uid, c1, c2, c3);
        } else if(uid.charAt(3)=='A') {
            admin = new Admin();
//            admin.run_admin(uid, c1, c2, c3);
        } else {
            System.out.println("invalid uid. terminating program");
            return;
        }



    }

    void start_client() throws Exception {





    }
}
