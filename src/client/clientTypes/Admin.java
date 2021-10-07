package client.clientTypes;

import servers.DVL.DVL_i;
import servers.KKL.KKL_i;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Admin {
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

    public Admin() {
        System.out.println("Admin()");
    }

    public void run_admin(String uid, DVL_i dvl_i, KKL_i kkl_i) throws IOException {
        String campus = uid.substring(0,3);  // TODO: do this in Client
//        System.out.println("admin from campus => " + campus);

        System.out.println("Enter room number (1-10), day (Monday to Friday), timeslot (8:00 to 16:00)");
        String rno;
        String date;
        String timeslot;
        rno = br.readLine();
        date = br.readLine();
        timeslot = br.readLine();

        // for testing
//        rno = "2";
//        date = "Monday";
//        timeslot = "9:00";

        // testing end

        System.out.println(campus + " admin creates following RoomRecord =>  [Room Number: " + rno + "] on [Day " + date + "] for [timeslot " + timeslot + "]");

        if(campus.equals("DVL")) {
            System.out.println("DVL server attempting to create room");
            boolean response = dvl_i.createroom(rno, date, timeslot);  // TODO: rename s1_i to DVI_i
//            System.out.println("response: " + response);
            if(response) {
                System.out.println("DVL created room.");
            } else {
                System.out.println("error. room not created");
            }
        } else if (campus.equals("KKL")) {
            System.out.println("KKL server attempting to create room");
            boolean response = kkl_i.createroom(rno, date, timeslot);  // TODO: rename s1_i to DVI_i
            if(response) {
                System.out.println("KKL created room");
            } else {
                System.out.println("room not created");
            }
        }
    }
}
