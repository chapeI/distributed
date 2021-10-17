package client.clientTypes;

//import servers.DVL.DVL_i;
//import servers.KKL.KKL_i;
//import servers.WST.WST_i;

import common.c;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Admin {
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

    public Admin() {
        System.out.println("Admin()");
    }

    public void run_admin(String uid, c dvl_i, c kkl_i, c wst_i) throws IOException {
        String campus = uid.substring(0,3);
        String rno;
        String date;
        String timeslot;

        System.out.println("Enter room number (1-10)");
        rno = br.readLine();
        System.out.println("Enter day (Monday to Friday)");
        date = br.readLine();
        System.out.println("Enter timeslot (8:00 to 16:00)");
        timeslot = br.readLine();

        System.out.println(campus + "admin creating [Room Number: " + rno + "] on [Day " + date + "] for [timeslot " + timeslot + "]");

        if(campus.equals("DVL")) {
            dvl_i.createroom(rno, date, timeslot);
            System.out.println("DVL created room.");
        } else if (campus.equals("KKL")) {
            kkl_i.createroom(rno, date, timeslot);
            System.out.println("KKL created room");
        } else if (campus.equals("WST")) {
            wst_i.createroom(rno, date, timeslot);
            System.out.println("WST created room");
        }
    }
}
