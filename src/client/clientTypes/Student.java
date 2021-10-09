package client.clientTypes;

import servers.DVL.DVL_i;
import servers.KKL.KKL_i;
import servers.WST.WST_i;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Student {
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    public Student() throws MalformedURLException, NotBoundException, RemoteException {}

    public void run_student(String uid, DVL_i dvl_i, KKL_i kkl_i, WST_i wst_i) throws IOException, InterruptedException, NotBoundException {
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
                    String booking_id = dvl_i.bookroom(booking_campus, rno, date, timeslot, uid);
                } else if (campus.equals("KKL")) {
                    String booking_id = kkl_i.bookroom(booking_campus, rno, date, timeslot, uid);
                } else if (campus.equals("WST")) {
                    String booking_id = kkl_i.bookroom(booking_campus, rno, date, timeslot, uid);
                }

                break;
            } case 2: {
                String date;
                System.out.println("enter date");
                date=br.readLine();

                if(campus.equals("DVL")) {
                    dvl_i.getAvailableTimeSlot(date);
                } else if (campus.equals("KKL")) {
                    kkl_i.getAvailableTimeSlot(date);
                } else if (campus.equals("WST")) {
                    wst_i.getAvailableTimeSlot(date);
                }

                break;
            } case 3: {
                String booking_id;
                System.out.println("enter booking_id");
                booking_id=br.readLine();

                if(campus.equals("DVL")) {
                    dvl_i.cancelBooking(booking_id);
                } else if (campus.equals("KKL")) {
                    kkl_i.cancelBooking(booking_id);
                } else if (campus.equals("WST")) {
                    wst_i.cancelBooking(booking_id);
                }

                break;
            }
        }
    }
}
