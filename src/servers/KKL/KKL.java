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
    static HashMap<String, HashMap<String, HashMap<String,String>>> a = new HashMap< String, HashMap<String,HashMap<String,String>>>();  // 	HM<date, HM<rno, HM<time, b_id>>>
    KKL() {
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
        cancelBooking(booking_id, "KKL");
        bookroom(new_campus_name, new_room_no, new_date, new_time_slot, studentid);
        return "changed";
    }

    // synchronize
    public synchronized String bookroom(String campus_for_booking, String rno, String date, String timeslot, String UID) {
        String bookingid = "kkl DEBUG";

        switch (campus_for_booking) {
            case "KKL":
                bookingid = UUID.randomUUID().toString();
                if (a.get(date).get(rno).get(timeslot) == "available") {
                    a.get(date).get(rno).put(timeslot, bookingid);
                    System.out.println(a);
                    break;
                } else {
                    String s = "\nalready booked";
                    System.out.println(s);
                    bookingid = s;
                }
                break;
            case "DVL":
                String s = serialize_("BR", campus_for_booking, rno, date, timeslot);
                KKL_send_request st = new KKL_send_request(s, 2172);
                Thread t = new Thread(st);
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                bookingid = st.response;
                System.out.println("DVL.bookroom(): DVL st.response: "+st.response);
                break;
            case "WST":
                String s1 = serialize_("BR", campus_for_booking, rno, date, timeslot);
                KKL_send_request st2 = new KKL_send_request(s1, 2171);
                Thread t1 = new Thread(st2);
                t1.start();
                try {
                    t1.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                bookingid = st2.response;
                System.out.println("KKL.bookroom(): WST st.response: "+st2.response);
                break;
        }
        return bookingid;
    }
    String serialize_(String op, String campus, String rno, String date, String timeslot) {
        String s = op.concat(campus).concat(rno).concat(date).concat(timeslot);
//        System.out.println("KKL_serialize: s => " + s);
        return s;
    }

    public synchronized String getAvailableTimeSlot(String date) throws InterruptedException {
        String date_ = "GA".concat(date);
        int kkl_available_count = 0;
        kkl_available_count += this.get_count(date);
        System.out.println("\nKKL: (before) just available rooms in kkl : " + kkl_available_count);

        KKL_send_request st1 = new KKL_send_request(date_, 2172); // DVL
        KKL_send_request st2 = new KKL_send_request(date_, 2171); // WST

        Thread t1=new Thread(st1);
        Thread t2 = new Thread(st2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        String DVL_count = st1.response;
        System.out.println("DVL: DVL_count: " + DVL_count);
        String WST_count = st2.response;
        System.out.println("WST: WST_count: " + WST_count);
        int c1 = Integer.parseInt(DVL_count);
        int c2 = Integer.parseInt(WST_count);
        kkl_available_count += c1 + c2;

        String s = "KKL: (after) Total amount of available rooms for " + date +", across all three campuses is => " + kkl_available_count;
        System.out.println(s);
        return s;
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

    public synchronized String cancelBooking(String bookingid, String campus) {
        String cancellation = "KKL: initial cancellation (shouldnt see this)";
        switch (campus) {
            case "KKL":
                for (Map.Entry<String, HashMap<String, HashMap<String, String>>> d : a.entrySet()) {
//                    System.out.println(d.getValue());
                    for (Map.Entry<String, HashMap<String, String>> rno : d.getValue().entrySet()) {
//                        System.out.println(rno.getValue());
                        for (Map.Entry<String, String> t : rno.getValue().entrySet()) {
//                            System.out.println(t.getValue());
                            String booking = t.getValue();
                            if (booking.equals(bookingid)) {
                                System.out.println(bookingid + " found at {" + d.getKey() + " in rno=" + rno.getKey() + " @" + t.getKey() + "} (KKL campus)");
                                a.get(d.getKey()).get(rno.getKey()).put(t.getKey(), "available");
                                System.out.println("booking_id cancelled and changed to available. check data-structure 'a' for proof");
                                System.out.println(a);
                                cancellation = "cancelled";
                            } else {
                                System.out.println("booking " + bookingid + " not found on " + d.getKey());
                                cancellation = "not cancelled";
                            }
                        }
                    }
                }
                break;
            case "WST":
                String request = "CB".concat(bookingid);
                KKL_send_request sr = new KKL_send_request(request, 2171);
                Thread t = new Thread(sr);
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                cancellation = sr.response;
                break;
            case "DVL":
                String request2 = "CB".concat(bookingid);
                KKL_send_request sr2 = new KKL_send_request(request2, 2172);
                Thread t2 = new Thread(sr2);
                t2.start();
                try {
                    t2.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                cancellation = sr2.response;
                break;
        }
        return cancellation;
    }

    public boolean deleteroom(String rno, String date, String timeslot) {
        return false;
    }
}
