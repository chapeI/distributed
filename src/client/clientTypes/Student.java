package client.clientTypes;

import servers.DVL.DVL_i;
import servers.KKL.KKL_i;
import servers.WST.WST_i;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Student {

    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

    public Student() throws MalformedURLException, NotBoundException, RemoteException {
//        System.out.println("Student login");

    }

    public void run_student(String uid, DVL_i dvl_i, KKL_i kkl_i, WST_i wst_i) throws IOException, InterruptedException, NotBoundException {
        String campus = uid.substring(0,3);  // TODO: do this in Client
        System.out.println("Enter room number (1-10), day (Monday to Friday), timeslot (8:00 to 16:00)");

        String campus_booking;
        String rno;
        String date;
        String timeslot;

//        campus = br.readLine();
//        rno = br.readLine();
//        date = br.readLine();
//        timeslot = br.readLine();

        // testing values
//        campus_booking = "KKL";
//        rno = "2";
//        date = "Monday";
//        timeslot = "9:00";

        if(campus.equals("DVL")) {
//            String test = dvl_i.bookroom2(campus_booking, rno, date, timeslot, uid);
            dvl_i.getAvailableTimeSlot("Wednesday");
        } else if (campus.equals("KKL")) {
//            String test = kkl_i.bookroom(campus_booking, rno, date, timeslot, uid);
            kkl_i.cancelBooking("BOOKINGID_1");
        } else if (campus.equals("WST")) {
            wst_i.getAvailableTimeSlot("Wednesday");
        }

    }
}
