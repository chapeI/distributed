package client.clientTypes;

import servers.one.S1_i;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Admin {
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

    public Admin() {
        System.out.println("admin authorized");
    }

    public void setup(String uid, S1_i s1_i)throws IOException {
        String campus = uid.substring(0,3);  // TODO: do this in Client
        System.out.println("admin from campus => " + campus);

        System.out.println("Enter room number (1-10), day (Monday to Friday), timeslot (8:00 to 16:00)");
        String rno;
        String date;
        String timeslot;
//        rno = br.readLine();
//        date = br.readLine();
//        timeslot = br.readLine();

        // for testing
        campus = "DVL";
        rno = "2";
        date = "Monday";
        timeslot = "9:00";

        // testing end

        System.out.println("admin wants to book [Room Number: " + rno + "] on [Day " + date + "] for [timeslot " + timeslot + "]");

        if(campus.equals("DVL")) {
            System.out.println("'campus.equals = DVL' check");
            boolean response = s1_i.createroom(rno, date, timeslot);  // TODO: rename s1_i to DVI_i
            System.out.println("response: " + response);
            if(response) {
                System.out.println("room created");
            } else {
                System.out.println("room not created");
            }
        }
    }
}
