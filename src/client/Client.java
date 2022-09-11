package client;
import client.clientTypes.Admin;
import client.clientTypes.Student;
import sDVL.DVL_i;
import sKKL.KKL_i;
import sWST.WST_i;

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

        URL u1 = new URL("http://localhost:8080/dalailama?wsdl");
        QName q1 = new QName("http://sDVL/", "DVLService");  // has to be implementation (so in this case, DVL, + 'Service' exactly. so ->   DVLService
        Service service1 = Service.create(u1, q1);
        DVL_i dvl_i = service1.getPort(DVL_i.class);

        URL u2 = new URL("http://localhost:8081/dalailama?wsdl");
        QName q2 = new QName("http://sKKL/", "KKLService");
        Service service2 = Service.create(u2, q2);
        KKL_i kkl_i = service2.getPort(KKL_i.class);

        URL u3 = new URL("http://localhost:8082/dalailama?wsdl");
        QName q3 = new QName("http://sWST/", "WSTService");
        Service service3 = Service.create(u3, q3);
        WST_i wst_i = service3.getPort(WST_i.class);


        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter ID");
        uid = br.readLine();

        if(uid.charAt(3)=='S') {
            student = new Student();
            student.run_student(uid, dvl_i, kkl_i, wst_i);
        } else if(uid.charAt(3)=='A') {
            admin = new Admin();
//            admin.run_admin(uid, c1, c2, c3);
        } else {
            System.out.println("invalid uid. terminating program");
            return;
        }
    }
}
