package client.clientTypes;

import sDVL.DVL_i;
import sKKL.KKL_i;
import sWST.WST_i;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Student {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String response = "Student: BAD RESPONSE";
    public Student() {}

    public void run_student(String uid, DVL_i dvl_i, KKL_i kkl_i, WST_i wst_i) throws IOException, InterruptedException {
        String campus = uid.substring(0,3);

        System.out.println("1) book room 2) get available times  3) cancel booking  4) changeReservation");
        int n = Integer.parseInt(br.readLine());
        switch(n) {
            case 1: {
                String campus_for_booking;
                String rno;
                String date;
                String timeslot;

                System.out.println("Enter booking campus (KKL, WST, DVL)");
                campus_for_booking = br.readLine();
                System.out.println("Enter day (MON, TUE, WED, THU, FRI)");
                date = br.readLine();
                System.out.println("Enter room number (1-9)");
                rno = br.readLine();
                System.out.println("Enter timeslot (1:00 to 9:00)");
                timeslot = br.readLine();

                if(campus.equals("DVL")) {
                    response = dvl_i.bookroom(campus_for_booking, rno, date, timeslot, uid);
                } else if (campus.equals("KKL")) {
                    response = kkl_i.bookroom(campus_for_booking, rno, date, timeslot, uid);
                } else if (campus.equals("WST")) {
                    response = wst_i.bookroom(campus_for_booking, rno, date, timeslot, uid);
                }
                break;
            }
            case 2: {
                String date;
                System.out.println("enter date");
                date=br.readLine();

                if(campus.equals("DVL")) {
                    response = dvl_i.getAvailableTimeSlot(date);
                } else if (campus.equals("KKL")) {
                    response = kkl_i.getAvailableTimeSlot(date);
                } else if (campus.equals("WST")) {
                    response = wst_i.getAvailableTimeSlot(date);
                }
                break;
            }
            case 3: {
                String booking_id;
                String campus_for_cancelling;
                System.out.println("enter booking_id");
                booking_id=br.readLine();
                System.out.println("which campus is the booking_id in?");
                campus_for_cancelling=br.readLine();

                if(campus.equals("DVL")) {
                    response = dvl_i.cancelBooking(booking_id, campus_for_cancelling);
                } else if (campus.equals("KKL")) {
                    response = kkl_i.cancelBooking(booking_id, campus_for_cancelling);
                } else if (campus.equals("WST")) {
                    response = wst_i.cancelBooking(booking_id, campus_for_cancelling);
                }
                break;
            }
            case 4:
                String booking_id;
                String campus_for_cancelling;
                System.out.println("enter booking_id");
                booking_id=br.readLine();
                System.out.println("which campus contains the booking-id?");
                campus_for_cancelling=br.readLine();

                String campus_for_booking;
                String rno;
                String date;
                String timeslot;
                System.out.println("Enter new booking campus (KKL, WST, DVL)");
                campus_for_booking = br.readLine();
                System.out.println("Enter new day (MON, TUE, WED, THU, FRI)");
                date = br.readLine();
                System.out.println("Enter new room number (1-9)");
                rno = br.readLine();
                System.out.println("Enter new timeslot (1:00 to 9:00)");
                timeslot = br.readLine();

                if(campus.equals("DVL")) {
                    response = dvl_i.changeReservation(campus_for_cancelling, booking_id,date, campus_for_booking, rno, timeslot);
                } else if (campus.equals("KKL")) {
                    response = kkl_i.changeReservation(campus_for_cancelling, booking_id,date, campus_for_booking, rno, timeslot);
                } else if (campus.equals("WST")) {
                    response = wst_i.changeReservation(campus_for_cancelling, booking_id,date, campus_for_booking, rno, timeslot);
                }
                break;
        }
        System.out.println("response: " + response);
    }
}
