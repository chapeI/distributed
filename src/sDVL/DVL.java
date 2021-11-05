package sDVL;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.*;

@WebService(endpointInterface="sDVL.DVL_i")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class DVL implements DVL_i {
    static HashMap<String, HashMap<String, HashMap<String,String>>> a = new HashMap< String, HashMap<String,HashMap<String,String>>>();  // 	HM<date, HM<rno, HM<time, b_id>>>
    public DVL() {
        super();
        make_new_date(a, "TUE", "5", "6:00");
        make_new_date(a, "WED", "1", "4:00");
        make_new_date(a, "MON", "2", "4:00");
        make_new_date(a, "THU", "6", "7:00");
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

    public String changeReservation(String studentid, String booking_id, String new_date, String new_campus_name, String new_room_no, String new_time_slot) {
        cancelBooking(booking_id, "DVL");
        bookroom(new_campus_name, new_room_no, new_date, new_time_slot, studentid);
        return "changed";
    };

    // synchronize
    public String bookroom(String campus_for_booking, String rno, String date, String timeslot, String UID) {
        String bookingid = "DVL: bookingid initial. this should change";
        switch (campus_for_booking) {
            case "DVL":
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
            case "KKL": {
                String s = serialize_("BR", campus_for_booking, rno, date, timeslot);
                DVL_sender st = new DVL_sender(s, 2170);
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
            }
            case "WST": {
                String s1 = serialize_("BR", campus_for_booking, rno, date, timeslot);
                DVL_sender st2 = new DVL_sender(s1, 2171);
                Thread t1 = new Thread(st2);
                t1.start();
                try {
                    t1.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                bookingid = st2.response;
                System.out.println("DVL.bookroom(): WST st.response: "+st2.response);
                break;
            }
        }
        return bookingid;
    }
    String serialize_(String op, String campus, String rno, String date, String timeslot) {
        String s = op.concat(campus).concat(rno).concat(date).concat(timeslot);
//        System.out.println("DVL_serialize: s => " + s);
        return s;
    }

    public String getAvailableTimeSlot(String date) throws InterruptedException {
        String date_ = "GA".concat(date);
        int dvl_available_count = 0;
        dvl_available_count += get_count(date);
        System.out.println("\nDVL: (before) just available rooms in dvl : " + dvl_available_count);

        DVL_sender st1 = new DVL_sender(date_, 2170); // KKL
        DVL_sender st2 = new DVL_sender(date_, 2171); // WST

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
        dvl_available_count += c1 + c2;

        String s = "DVL: (after) Total amount of available rooms for " + date +", across all three campuses is => " + dvl_available_count;
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

    public String cancelBooking(String bookingid, String campus) {
        String cancellation = "DVL: initial cancellation (shouldnt see this)";
        switch(campus) {
            case "DVL":
                for(Map.Entry<String, HashMap<String, HashMap<String, String>>> d : a.entrySet()) {
//                    System.out.println(d.getValue());
                    for(Map.Entry<String, HashMap<String, String>> rno : d.getValue().entrySet()) {
//                        System.out.println(rno.getValue());
                        for(Map.Entry<String, String> t : rno.getValue().entrySet()) {
//                            System.out.println(t.getValue());
                            String booking = t.getValue();
                            if(booking.equals(bookingid)) {
                                System.out.println(bookingid + " found at {" + d.getKey() + " in rno=" + rno.getKey() + " @" + t.getKey() + "} (DVL campus)");
                                a.get(d.getKey()).get(rno.getKey()).put(t.getKey(), "available");
                                System.out.println("booking_id cancelled and changed to available. check data-structure 'a' for proof");
                                System.out.println(a);
                                cancellation = "cancelled";
                                return cancellation;
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
                DVL_sender sr = new DVL_sender(request, 2171);
                Thread t = new Thread(sr);
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                cancellation = sr.response;
                break;
            case "KKL":
                String request2 = "CB".concat(bookingid);
                DVL_sender sr2 = new DVL_sender(request2, 2170);
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
    };
}
