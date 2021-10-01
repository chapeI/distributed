package client;

import servers.one.S1_i;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Admin {
    BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

    Admin() {
        System.out.println("admin authorized");
    }

    public void setup(String uid, S1_i s1_i)throws IOException {
        String campus = uid.substring(0,3);  // TODO: do this in Client
        System.out.println("admin from campus => " + campus);

        System.out.println("Enter room number, day (1-10), slot (1-10)");
        String rno = br.readLine();
        String date = br.readLine();
        String slot = br.readLine();
        System.out.println("admin wants to book [Room Number: " + rno + "] on [Day " + date + "] for [slot " + slot + "]");

        if(campus.equals("DVL")) {
            System.out.println("'campus.equals = DVL' check");
            boolean response = s1_i.createroom(rno, date, slot);  // TODO: rename s1_i to DVI_i
            System.out.println("response: " + response);
            if(response) {
                System.out.println("room created");
            } else {
                System.out.println("room not created");
            }
        }
    }
}
