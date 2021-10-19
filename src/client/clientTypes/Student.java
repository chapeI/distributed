package client.clientTypes;

import common.c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Student {
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    public Student() {}

    public void run_student(String uid, c DVL, c KKL, c WST) throws IOException, InterruptedException {
        String campus = uid.substring(0,3);

        System.out.println("1) book room 2) get available times  3) cancel booking");
        int n = Integer.parseInt(br.readLine());
        switch(n) {
            case 1: {
                String campus_for_booking;
                String rno;
                String date;
                String timeslot;
//                String rno = "1";
//                String date = "WED";
//                String timeslot = "4:00";

                System.out.println("Enter booking campus (KKL, WST, DVL)");
                campus_for_booking = br.readLine();
                System.out.println("Enter day (MON, TUE, WED, THU, FRI)");
                date = br.readLine();
                System.out.println("Enter room number (1-9)");
                rno = br.readLine();
                System.out.println("Enter timeslot (1:00 to 9:00)");
                timeslot = br.readLine();

                if(campus.equals("DVL")) {
                    String booking_id = DVL.bookroom(campus_for_booking, rno, date, timeslot, uid);
                } else if (campus.equals("KKL")) {
                    String booking_id = KKL.bookroom(campus_for_booking, rno, date, timeslot, uid);
                } else if (campus.equals("WST")) {
                    String booking_id = WST.bookroom(campus_for_booking, rno, date, timeslot, uid);
                }
                break;
            }
            case 2: {
                String date;
                System.out.println("enter date");
                date=br.readLine();

                if(campus.equals("DVL")) {
                    DVL.getAvailableTimeSlot(date);
                } else if (campus.equals("KKL")) {
                    KKL.getAvailableTimeSlot(date);
                } else if (campus.equals("WST")) {
                    WST.getAvailableTimeSlot(date);
                }

                break;
            }
            case 3: {
                String booking_id;
                System.out.println("enter booking_id");
                booking_id=br.readLine();

                if(campus.equals("DVL")) {
                    DVL.cancelBooking(booking_id, "don't need");
                } else if (campus.equals("KKL")) {
                    KKL.cancelBooking(booking_id, "don't need");
                } else if (campus.equals("WST")) {
                    WST.cancelBooking(booking_id, "don't need");
                }

                break;
            }
        }
    }
}
