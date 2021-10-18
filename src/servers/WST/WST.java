package servers.WST;

import common.cPOA;
import org.omg.CORBA.ORB;

import java.util.HashMap;
import java.util.Map;

public class WST extends cPOA {
    private ORB orb;
    public void setORB(ORB orb_val) {
        orb = orb_val;
    }
    int wst_available_count = 0;
    static HashMap<String, HashMap<String, HashMap<String,String>>> a = new HashMap< String, HashMap<String,HashMap<String,String>>>();  // 	HM<date, HM<rno, HM<time, b_id>>>
    WST () {
        super();
        make_new_date(a, "Thursday", "4", "1:00");
        make_new_date(a, "Wednesday", "3", "6:00");
        make_new_date(a, "Tuesday", "4", "6:00");
        make_new_date(a, "Monday", "5", "6:00");
        make_new_date(a, "Wednesday", "6", "6:00");
        System.out.println("WST(): " + a);
    }

    public boolean createroom(String rno, String date, String timeslot) {
        return false;
    }
    static void make_new_date(HashMap<String, HashMap<String, HashMap<String, String>>> dates, String date, String rno, String time) {
        if(dates.containsKey(date)) {
            make_room_available(dates.get(date), rno, time);
        } else {
            HashMap<String, HashMap<String, String>> rooms = new HashMap<>();
            make_room_available(rooms, rno, time);
            dates.put(date, rooms);
        }
    }
    static void make_room_available(HashMap<String, HashMap<String, String>> rooms, String rno, String time) {
        if(rooms.containsKey(rno)) {  // room exists. just get and put
            rooms.get(rno).put(time, "available");
        } else {  // make a room
            HashMap available = new HashMap();
            available.put(time, "available");
            rooms.put(rno, available);
        }
    }

    public String changeReservation(String studentid, String booking_id, String new_date, String new_campus_name, String new_room_no, String new_time_slot) {
        return null;
    }

    // following methods need to be synchronized
    public String bookroom(String campusName, String rno, String date, String timeslot, String UID) {
        return null;
    }

    public String getAvailableTimeSlot(String date) throws InterruptedException {
        this.wst_available_count = 0;
        this.wst_available_count += this.get_count(date);
        System.out.println("\nWST: (before) just available rooms in wst : " + wst_available_count);

        WST_Sender sender = new WST_Sender(date, 2172);
        System.out.println("WST: sending request to DVL-Listener for number of available rooms for " + date);

        Thread t1=new Thread(sender);
        t1.start();
        t1.join();

        System.out.println("WST: request processed from DVL-Listener. count stored in sender");
        System.out.println("WST: dvl has: " + sender.count + " available room(s)");
        this.wst_available_count += sender.count;
        System.out.println("WST: (after) Total amount of available rooms for " + date +", across all three campuses is => " + wst_available_count);

        return "fix_kkl_available_count";
    }
    public int get_count(String date) {
        int count = 0;
        HashMap<String, HashMap<String, String>> day;
        day = a.get(date);
//        System.out.println(day);

        for(Map.Entry<String, HashMap<String, String>> rno : day.entrySet()) {
            // System.out.println("rno: " + rno.getKey());
            for(Map.Entry<String, String> t : rno.getValue().entrySet()) {
                // System.out.println("t: " + t.getValue());
                String time = t.getValue();
                if(time.equals("available")) {
                    count += 1;
                }
            }
        }
//        System.out.println("count: " + count);
        return count;
    }


    public String cancelBooking(String bookingID, String userid) {
        return null;
    }

    public boolean deleteroom(String rno, String date, String timeslot) {
        return false;
    }
}
