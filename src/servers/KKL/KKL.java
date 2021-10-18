package servers.KKL;

import common.cPOA;
import org.omg.CORBA.ORB;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KKL extends cPOA {
    private ORB orb;
    public void setORB(ORB orb_val) {
        orb = orb_val;
    }
    int kkl_available_count = 0;
    static HashMap<String, HashMap<String, HashMap<String,String>>> a = new HashMap< String, HashMap<String,HashMap<String,String>>>();  // 	HM<date, HM<rno, HM<time, b_id>>>
    KKL () {
        super();
        make_new_date(a, "MON", "1", "3:00");
        make_new_date(a, "TUE", "1", "4:00");
        make_new_date(a, "WED", "2", "8:00");
        make_new_date(a, "THU", "2", "4:00");
        System.out.println("KKL(): " + a);
    }
    public boolean createroom(String rno, String date, String timeslot) {
        make_new_date(a, date, rno, timeslot);
        System.out.println(a);
        return true;
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

    // synchronize
    public String bookroom(String campus_for_booking, String rno, String date, String timeslot, String UID) {
        String bookingid;

        switch (campus_for_booking) {
            case "KKL":
                bookingid = UUID.randomUUID().toString();
                if (a.get(date).get(rno).get(timeslot) == "available") {
                    a.get(date).get(rno).put(timeslot, "BOOKED");
                    System.out.println(a);
                } else {
                    System.out.println("CRASH");
                }
                break;
            case "DVL":
                String s = serialize_("BR", campus_for_booking, rno, date, timeslot);
                KKL_sender bookroom_in_dvl = new KKL_sender(s, 2172);
                Thread t = new Thread(bookroom_in_dvl);
                t.start();
                break;
            case "WST":
                String s1 = serialize_("BR", campus_for_booking, rno, date, timeslot);
                KKL_sender bookroom_in_wst = new KKL_sender(s1, 2171);
                Thread t1 = new Thread(bookroom_in_wst);
                t1.start();
                break;
        }
        return "WORKING";
    }
    String serialize_(String op, String campus, String rno, String date, String timeslot) {
        String s = op.concat(campus).concat(rno).concat(date).concat(timeslot);
        System.out.println("s: " + s);
        return s;
    }

    public String getAvailableTimeSlot(String date) throws InterruptedException {
        this.kkl_available_count = 0;
        this.kkl_available_count += this.get_count(date);
        System.out.println("\nKKL: (before) just available rooms in kkl : " + kkl_available_count);

        // append GA (get-available) before each date
        String date_ = "GA".concat(date);

        KKL_sender GA_dvl = new KKL_sender(date_, 2172);
        System.out.println("KKL: sending request to DVL-Listener for number of available rooms for " + date);

        KKL_sender GA_wst = new KKL_sender(date_, 2171);
        System.out.println("KKL: sending request to WST-Listener for number of available rooms for " + date);

        Thread t1=new Thread(GA_dvl);
        Thread t2 = new Thread(GA_wst);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("KKL: request processed from DVL-Listener. count stored in thread");
        System.out.println("KKL: dvl has: " + GA_dvl.count + " available room(s)");
        this.kkl_available_count += GA_dvl.count;

        System.out.println("KKL: request processed from WST-Listener. count stored in thread");
        System.out.println("KKL: wst has: " + GA_wst.count + " available room(s)");
        this.kkl_available_count += GA_wst.count;

        System.out.println("KKL: (after) Total amount of available rooms for " + date +", across all three campuses is => " + kkl_available_count);
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

    public String cancelBooking(String bookingid, String userid) {
        for(Map.Entry<String, HashMap<String, HashMap<String, String>>> d : a.entrySet()) {
            for(Map.Entry<String, HashMap<String, String>> rno : d.getValue().entrySet()) {
                for(Map.Entry<String, String> t : rno.getValue().entrySet()) {
                    String booking = t.getValue();
                    if(booking.equals(bookingid)) {
                        System.out.println(bookingid + " found at {" + d.getKey() + " in rno=" + rno.getKey() + " @" + t.getKey() + "} (DVL campus)");
                        a.get(d.getKey()).get(rno.getKey()).put(t.getKey(), "available");  // cancelling and changing b_id to available
                        System.out.println("booking_id cancelled and changed to available. check data-structure 'a' for proof");
                        System.out.println(a);
                        return "canceled and made available";
                    } else {
                        System.out.println("no bookings found for " + bookingid);
                        return "ugh. debug";
                    }
                }
            }
        }
        return "debug_cancel_booking";
    }

    public boolean deleteroom(String rno, String date, String timeslot) {
        return false;
    }
}
