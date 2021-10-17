import java.util.*;

public class testing {
    public static void main(String[] args) {
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                for(int i=1; i<=4;i++) {
                    System.out.println("i: " + i);
                }
            };
        };
        t.scheduleAtFixedRate(tt,0, 2*6*1000);
    }
}

