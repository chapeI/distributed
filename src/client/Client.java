package client;
import client.clientTypes.Admin;
import client.clientTypes.Student;
import servers.DVL.DVL_i;
import servers.KKL.KKL_i;
import servers.WST.WST_i;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class Client {

//    String ADMIN = "DVLA123";
//    String STUDENT = "DVLS123";

    // start_client starts the client. can access remote objects from here
    void start_client() throws Exception {
        String uid;
//        uid = STUDENT;  // STUDENT OR ADMIN test

        Admin admin;
        Student student;

        DVL_i dvl_i = (DVL_i) Naming.lookup("rmi://localhost:35000/tag1");
        KKL_i kkl_i = (KKL_i) Naming.lookup("rmi://localhost:35001/tag2");
        WST_i wst_i = (WST_i) Naming.lookup("rmi://localhost:35002/tag3");

        // determine whether client is a student or an admin
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter ID");
        uid = br.readLine();

        if(uid.charAt(3)=='S') {
            student = new Student();
            student.run_student(uid, dvl_i, kkl_i, wst_i);
        } else if(uid.charAt(3)=='A') {
            admin = new Admin();
            admin.run_admin(uid, dvl_i, kkl_i);
        } else {
            System.out.println("invalid uid. terminating program");
            return;
        }
    }
}
