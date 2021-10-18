package servers.DVL;

import org.omg.CORBA.ORB;
import common.*;

import java.util.*;

public class DVL extends cPOA {
    private ORB orb;
    public void setORB(ORB orb_val) {
        orb = orb_val;
    }
    int dvl_available_count = 0;
    static HashMap<String, HashMap<String, HashMap<String,String>>> a = new HashMap< String, HashMap<String,HashMap<String,String>>>();  // 	HM<date, HM<rno, HM<time, b_id>>>
    DVL() {
        super();
        make_new_date(a, "Tuesday", "5", "6:00");
        make_new_date(a, "Wednesday", "1", "4:00");
        make_new_date(a, "Monday", "2", "4:00");
        make_new_date(a, "Thursday", "6", "7:00");
        System.out.println("DVL(): " + a);
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

    public String changeReservation (String studentid, String booking_id, String new_date, String new_campus_name, String new_room_no, String new_time_slot) {
        return "DEBUG";
    };

    // synchronize
    public String bookroom(String campus_for_booking, String rno, String date, String timeslot, String UID) {
        String bookingid;

        if(campus_for_booking.equals("DVL")) {
            System.out.println("reached-5");
            bookingid = UUID.randomUUID().toString();
            if(a.get(date).get(rno).get(timeslot) == "available") {
                a.get(date).get(rno).put(timeslot,"BOOKED");
                System.out.println(a);
            } else {
                System.out.println("CRASH");
            }
        } else if(campus_for_booking.equals("KKL")) {
//            bookingid = kkl_i.bookroom(campus_for_booking, rno, date, timeslot, UID);
        } else if(campus_for_booking.equals("WST")) {
//            bookingid = wst_i.bookroom(campus_for_booking, rno, date, timeslot, UID);
        }
        return "WORKING";
    }

    public String getAvailableTimeSlot(String date) throws InterruptedException {
        this.dvl_available_count = 0;
        this.dvl_available_count += this.get_count(date);
        System.out.println("\nDVL: (before) just available rooms in dvl : " + dvl_available_count);

        DVL_sender s1 = new DVL_sender(date, 2170);  // sending(date) to kkl (thread opened on port 2170)
        System.out.println("DVL: sending request to KKL-Listener for number of available rooms for " + date);

        DVL_sender s2 = new DVL_sender(date, 2171);
        System.out.println("DVL: sending request to WST-Listener for number of available rooms for " + date);

        Thread t1=new Thread(s1);
        Thread t2 = new Thread(s2);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("DVL: request processed from KKL-Listener. count stored in thread");
        System.out.println("DVL: kkl has: " + s1.count + " available room(s)");
        this.dvl_available_count += s1.count;

        System.out.println("DVL: request processed from WST-Listener. count stored in thread");
        System.out.println("DVL: wst has: " + s2.count + " available room(s)");
        this.dvl_available_count += s2.count;

        System.out.println("DVL: (after) Total amount of available rooms for " + date +", across all three campuses is => " + dvl_available_count);

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

    public boolean deleteroom (String rno, String date, String timeslot) {
        return false;
    };
}
