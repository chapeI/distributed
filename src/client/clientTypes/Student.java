package client.clientTypes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Student {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String response = "Student: BAD RESPONSE";
    public Student() {}

//    public void run_student(String uid, c DVL, c KKL, c WST) throws IOException, InterruptedException {
//        String campus = uid.substring(0,3);
//
//        System.out.println("1) book room 2) get available times  3) cancel booking  4) changeReservation");
//        int n = Integer.parseInt(br.readLine());
//        switch(n) {
//            case 1: {
//                String campus_for_booking;
//                String rno;
//                String date;
//                String timeslot;
//
//                System.out.println("Enter booking campus (KKL, WST, DVL)");
//                campus_for_booking = br.readLine();
//                System.out.println("Enter day (MON, TUE, WED, THU, FRI)");
//                date = br.readLine();
//                System.out.println("Enter room number (1-9)");
//                rno = br.readLine();
//                System.out.println("Enter timeslot (1:00 to 9:00)");
//                timeslot = br.readLine();
//
//                if(campus.equals("DVL")) {
//                    response = DVL.bookroom(campus_for_booking, rno, date, timeslot, uid);
//                } else if (campus.equals("KKL")) {
//                    response = KKL.bookroom(campus_for_booking, rno, date, timeslot, uid);
//                } else if (campus.equals("WST")) {
//                    response = WST.bookroom(campus_for_booking, rno, date, timeslot, uid);
//                }
//                break;
//            }
//            case 2: {
//                String date;
//                System.out.println("enter date");
//                date=br.readLine();
//
//                if(campus.equals("DVL")) {
//                    response = DVL.getAvailableTimeSlot(date);
//                } else if (campus.equals("KKL")) {
//                    response = KKL.getAvailableTimeSlot(date);
//                } else if (campus.equals("WST")) {
//                    response = WST.getAvailableTimeSlot(date);
//                }
//                break;
//            }
//            case 3: {
//                String booking_id;
//                String campus_for_cancelling;
//                System.out.println("enter booking_id");
//                booking_id=br.readLine();
//                System.out.println("enter campus");
//                campus_for_cancelling=br.readLine();
//
//                if(campus.equals("DVL")) {
//                    response = DVL.cancelBooking(booking_id, campus_for_cancelling);
//                } else if (campus.equals("KKL")) {
//                    response = KKL.cancelBooking(booking_id, campus_for_cancelling);
//                } else if (campus.equals("WST")) {
//                    response = WST.cancelBooking(booking_id, campus_for_cancelling);
//                }
//                break;
//            }
//            case 4:
//                String booking_id;
//                String campus_for_cancelling;
//                System.out.println("enter booking_id");
//                booking_id=br.readLine();
//                System.out.println("which campus contains the booking-id?");
//                campus_for_cancelling=br.readLine();
//
//                String campus_for_booking;
//                String rno;
//                String date;
//                String timeslot;
//                System.out.println("Enter new booking campus (KKL, WST, DVL)");
//                campus_for_booking = br.readLine();
//                System.out.println("Enter new day (MON, TUE, WED, THU, FRI)");
//                date = br.readLine();
//                System.out.println("Enter new room number (1-9)");
//                rno = br.readLine();
//                System.out.println("Enter new timeslot (1:00 to 9:00)");
//                timeslot = br.readLine();
//
//                if(campus.equals("DVL")) {
//                    response = DVL.changeReservation(campus_for_cancelling, booking_id,date, campus_for_booking, rno, timeslot);
//                } else if (campus.equals("KKL")) {
//                    response = KKL.changeReservation(campus_for_cancelling, booking_id,date, campus_for_booking, rno, timeslot);
//                } else if (campus.equals("WST")) {
//                    response = WST.changeReservation(campus_for_cancelling, booking_id,date, campus_for_booking, rno, timeslot);
//                }
//                break;
//        }
//        System.out.println("response: " + response);
//    }
}
