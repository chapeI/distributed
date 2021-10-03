package client.clientTypes;

import servers.DVL.DVL_i;
import servers.KKL.KKL_i;

import java.io.IOException;

public class Student {

//    S1_i s1_i;

    public Student() {
        System.out.println("Student login");
    }

    public void setup(String uid, DVL_i dvl_i, KKL_i kkl_i) throws IOException, InterruptedException {
        System.out.println("student.setup()");
        String test = dvl_i.bookroom("DVL", "2", "Monday", "9:00", "DVLS1234");
        System.out.println("setup() done. should return DEBUG => " + test);
    }
}
