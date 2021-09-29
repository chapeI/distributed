package client;
import servers.one.S1_i;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;

public class Client {

    // start_client starts the client. can access remote objects from here
    void start_client() throws Exception {
        String uid;
        Admin admin;
        Student student;

        S1_i s1i;
        s1i = (S1_i) Naming.lookup("rmi://localhost:35000/tag1");

        // determine whether client is a student or an admin
        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the User ID");
        uid = br.readLine();

        if(uid.charAt(3)=='S') {
            student = new Student();
            student.setup(uid, s1i);
        } else if(uid.charAt(3)=='A') {
            admin = new Admin();
            admin.setup(uid, s1i);
        } else {
            System.out.println("invalid. Client program terminated");
            return;
        }
    }
}
