package client;
import client.clientTypes.Admin;
import client.clientTypes.Student;
import servers.one.S1_i;
import servers.two.S2_i;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class Client {

    String ADMIN = "DVLA123";
    String STUDENT = "DVLS123";


    // start_client starts the client. can access remote objects from here
    void start_client() throws Exception {
        String uid;
//        uid = STUDENT;  // STUDENT OR ADMIN

        Admin admin;
        Student student;

        S1_i s1_i = (S1_i) Naming.lookup("rmi://localhost:35000/tag1");
        S2_i s2_i = (S2_i) Naming.lookup("rmi://localhost:35001/tag2");


        // determine whether client is a student or an admin
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter ID");
        uid = br.readLine();


        if(uid.charAt(3)=='S') {
            student = new Student();
            student.setup(uid, s1_i, s2_i);
        } else if(uid.charAt(3)=='A') {
            admin = new Admin();
            admin.setup(uid, s1_i, s2_i);
        } else {
            System.out.println("invalid. program terminated");
            return;
        }
    }
}
