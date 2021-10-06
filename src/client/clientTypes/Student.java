package client.clientTypes;

import servers.DVL.DVL_i;
import servers.KKL.KKL_i;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;

public class Student {

//    S1_i s1_i;
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));


    public Student() {
        System.out.println("Student login");
    }

    public void run_student(String uid, DVL_i dvl_i, KKL_i kkl_i) throws IOException, InterruptedException, NotBoundException {
        System.out.println("student.run_student()");

        // TODO: distinguish between campus_student_belongs_to and campus_student_wants_to_book

        String campus = uid.substring(0,3);  // TODO: do this in Client
//        System.out.println("campus: " + campus);

        // prompt Student for room details. (campus, room number, day, timeslot)
//        System.out.println("Enter room number (1-10), day (Monday to Friday), timeslot (8:00 to 16:00)");

        String campus_booking;
        String rno;
        String date;
        String timeslot;

//        campus = br.readLine();
//        rno = br.readLine();
//        date = br.readLine();
//        timeslot = br.readLine();

        // testing
        campus_booking = "KKL";
        rno = "2";
        date = "Monday";
        timeslot = "9:00";

        if(campus.equals("DVL")) {
            System.out.println("DVL?");
//            String test = dvl_i.bookroom2(campus_booking, rno, date, timeslot, uid);
//            System.out.println("setup() done. should return DEBUG => " + test);
            dvl_i.getAvailableTimeSlot("Monday");
        } else if (campus.equals("KKL")) {
            System.out.println("KKL?");
            String test = kkl_i.bookroom2(campus_booking, rno, date, timeslot, uid);
            System.out.println("setup() done. should return DEBUG => " + test);
        }

    }
}
