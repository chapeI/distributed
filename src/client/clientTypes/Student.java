package client.clientTypes;

import common.c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Student {
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    public Student() {}

    public void run_student(String uid, c c1, c c2, c c3) throws IOException, InterruptedException {
        String campus = uid.substring(0,3);

        System.out.println("1) book room 2) get available times  3) cancel booking");
        int n = Integer.parseInt(br.readLine());
        switch(n) {
            case 1: {
                String booking_campus;
                String rno;
                String date;
                String timeslot;

                System.out.println("Enter booking campus (KKL, WST, DVL)");
                booking_campus = br.readLine();
                System.out.println("Enter day (Monday to Friday)");
                date = br.readLine();
                System.out.println("Enter room number (1-10)");
                rno = br.readLine();
                System.out.println("Enter timeslot (8:00 to 16:00)");
                timeslot = br.readLine();

                if(campus.equals("DVL")) {
                    String booking_id = c1.bookroom(booking_campus, rno, date, timeslot, uid);
                } else if (campus.equals("KKL")) {
                    System.out.println("reached-0");
                    String booking_id = c2.bookroom(booking_campus, rno, date, timeslot, uid);
                    System.out.println(booking_id);
                } else if (campus.equals("WST")) {
                    String booking_id = c2.bookroom(booking_campus, rno, date, timeslot, uid);
                }
                break;
            } case 2: {
                String date;
                System.out.println("enter date");
                date=br.readLine();

                if(campus.equals("DVL")) {
                    c1.getAvailableTimeSlot(date);
                } else if (campus.equals("KKL")) {
                    c2.getAvailableTimeSlot(date);
                } else if (campus.equals("WST")) {
                    c3.getAvailableTimeSlot(date);
                }

                break;
            } case 3: {
                String booking_id;
                System.out.println("enter booking_id");
                booking_id=br.readLine();

                if(campus.equals("DVL")) {
//                    c1.cancelBooking(booking_id);
                } else if (campus.equals("KKL")) {
//                    c2.cancelBooking(booking_id);
                } else if (campus.equals("WST")) {
//                    c3.cancelBooking(booking_id);
                }

                break;
            }
        }
    }
}
