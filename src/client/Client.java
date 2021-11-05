package client;
import client.clientTypes.Admin;
import client.clientTypes.Student;
import sDVL.DVL_i;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.*;
import java.net.URL;

public class Client {

    public static void main(String[] args) throws Exception {
        String uid;
        Admin admin;
        Student student;

        URL u1 = new URL("http://localhost:8080/cal?wsdl");
        QName q1 = new QName("http://sDVL/", "DVLService");
        Service service1 = Service.create(u1, q1);
        DVL_i dvl_i = service1.getPort(DVL_i.class);

        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter ID");
        uid = br.readLine();

        if(uid.charAt(3)=='S') {
            student = new Student();
            student.run_student(uid, dvl_i, dvl_i, dvl_i);
        } else if(uid.charAt(3)=='A') {
            admin = new Admin();
//            admin.run_admin(uid, c1, c2, c3);
        } else {
            System.out.println("invalid uid. terminating program");
            return;
        }
    }
}
