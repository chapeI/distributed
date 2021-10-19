package servers.WST;

import common.cPOA;
import org.omg.CORBA.ORB;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WST extends cPOA {
    private ORB orb;
    public void setORB(ORB orb_val) {
        orb = orb_val;
    }
    int wst_available_count = 0;
    static HashMap<String, HashMap<String, HashMap<String,String>>> a = new HashMap< String, HashMap<String,HashMap<String,String>>>();  // 	HM<date, HM<rno, HM<time, b_id>>>
    WST () {
        super();
        make_new_date(a, "THU", "4", "1:00");
        make_new_date(a, "WED", "3", "6:00");
        make_new_date(a, "TUE", "4", "6:00");
        make_new_date(a, "MON", "5", "6:00");
        make_new_date(a, "WED", "6", "6:00");
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
    public String bookroom(String campus_for_booking, String rno, String date, String timeslot, String UID) {
        String bookingid;
        switch (campus_for_booking) {
            case "WST":
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
                WST_sender bookroom_in_dvl = new WST_sender(s, 2172);
                Thread t = new Thread(bookroom_in_dvl);
                t.start();
                break;
            case "KKL":
                String s1 = serialize_("BR", campus_for_booking, rno, date, timeslot);
                WST_sender bookroom_in_kkl = new WST_sender(s1, 2170);
                Thread t1 = new Thread(bookroom_in_kkl);
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
        this.wst_available_count = 0;
        this.wst_available_count += this.get_count(date);
        System.out.println("\nWST: (before) just available rooms in wst : " + wst_available_count);

        // append GA (get-available) before each date
        String date_ = "GA".concat(date);

        WST_sender s = new WST_sender(date_, 2172);
        System.out.println("WST: sending request to DVL-Listener for number of available rooms for " + date);

        WST_sender s2 = new WST_sender(date_, 2170);
        System.out.println("WST: sending request to KKL-Listener for number of available rooms for " + date);

        Thread t1=new Thread(s);
        Thread t2 = new Thread(s2);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("WST: request processed from DVL-Listener. count stored in thread");
        System.out.println("WST: dvl has: " + s.count + " available room(s)");
        this.wst_available_count += s.count;

        System.out.println("WST: request processed from KKL-Listener. count stored in thread");
        System.out.println("WST: kkl has: " + s2.count + " available room(s)");
        this.wst_available_count += s2.count;

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


    public String cancelBooking(String bookingid, String userid) {
        for(Map.Entry<String, HashMap<String, HashMap<String, String>>> d : a.entrySet()) {
            System.out.println(d.getValue());
            for(Map.Entry<String, HashMap<String, String>> rno : d.getValue().entrySet()) {
                System.out.println(rno.getValue());
                for(Map.Entry<String, String> t : rno.getValue().entrySet()) {
                    System.out.println(t.getValue());
                    String booking = t.getValue();
                    if(booking.equals(bookingid)) {
                        System.out.println(bookingid + " found at {" + d.getKey() + " in rno=" + rno.getKey() + " @" + t.getKey() + "} (DVL campus)");
                        a.get(d.getKey()).get(rno.getKey()).put(t.getKey(), "available");
                        System.out.println("booking_id cancelled and changed to available. check data-structure 'a' for proof");
                        System.out.println(a);
                        return "canceled and made available";
                    } else {
                        System.out.println("no bookings found for " + bookingid);
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
