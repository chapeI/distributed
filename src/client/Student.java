package client;

import servers.one.S1_i;

import java.io.IOException;

public class Student {

//    S1_i s1_i;

    Student() {
        System.out.println("Student login");
    }

    public void setup(String uid, S1_i s1_i) throws IOException, InterruptedException {
        System.out.println("student.setup()");
        String test = s1_i.bookroom("DVL", "2", "Monday", "9:00", "DVLS1234");
        System.out.println("setup() done. should return DEBUG => " + test);
    }
}
