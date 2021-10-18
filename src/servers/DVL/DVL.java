package servers.DVL;

import org.omg.CORBA.ORB;
import common.*;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

public class DVL extends cPOA {
//    KKL_i kkl_i;
//    WST_i wst_i;
    int dvl_available_count = 0;  // available rooms in DVL
    static HashMap<String,
            HashMap<String,
                    HashMap<String,String>>> a = new HashMap< String, HashMap<String,HashMap<String,String>>>();  // 	HM<date, HM<rno, HM<time, b_id>>>
    DVL() throws RemoteException {
        super();
        make_new_date(a, "Tuesday", "5", "6:00");
        make_new_date(a, "Wednesday", "1", "4:00");
        make_new_date(a, "Monday", "2", "4:00");
        make_new_date(a, "Thursday", "6", "7:00");
        System.out.println("DVL(): " + a);
    }

    private ORB orb;
    public void setORB(ORB orb_val) {
        orb = orb_val;
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

    public void listener() throws RemoteException, MalformedURLException, NotBoundException {
        ListenerThread lt=new ListenerThread();
        Thread t =new Thread(lt);
        t.start();
    }

    public String changeReservation (String studentid, String booking_id, String new_date, String new_campus_name, String new_room_no, String new_time_slot) {
        return "DEBUG";
    };

    // things to synchronize
    public String bookroom(String campusName, String rno, String date, String timeslot, String UID) {
        String bookingid;
//        kkl_i = (KKL_i) Naming.lookup("rmi://localhost:35001/tag2");
//        wst_i = (WST_i) Naming.lookup("rmi://localhost:35002/tag3");

        if(campusName.equals("DVL")) {
            bookingid = UUID.randomUUID().toString();
            if(a.get(date).get(rno).get(timeslot) == "available") {
                a.get(date).get(rno).put(timeslot,"WORKING");
                System.out.println(a);
            } else {
                System.out.println("CRASH");
            }
        } else if(campusName.equals("KKL")) {
//            bookingid = kkl_i.bookroom(campusName, rno, date, timeslot, UID);
        } else if(campusName.equals("WST")) {
//            bookingid = wst_i.bookroom(campusName, rno, date, timeslot, UID);
        }
        return "WORKING";
    }

    public String getAvailableTimeSlot(String date) {
        this.dvl_available_count += this.get_count(date);
        System.out.println("dvl_available_count(before): " + dvl_available_count);

//        try {
//            kkl_i=(KKL_i)Naming.lookup("rmi://localhost:35001/tag2");
//            kkl_i.listener(); // create a listener thread on kkl
//            wst_i=(WST_i)Naming.lookup("rmi://localhost:35002/tag3");
//            wst_i.listener();
//        } catch(NotBoundException e ) {
//            System.err.println(e);
//        } catch (MalformedURLException e) {
//            System.err.println(e);
//        }

        // KKL
        DVL_sendingThread dvl_st_to_kkl = new DVL_sendingThread(date, 2170);  // sending(date) to kkl (port 2170)
        DVL_sendingThread dvl_st_to_wst = new DVL_sendingThread(date, 2171);

        Thread t1=new Thread(dvl_st_to_kkl);
        Thread t2 = new Thread(dvl_st_to_wst);

        t1.start();
        t2.start();

//        t1.join();
//        t2.join();

        this.dvl_available_count += dvl_st_to_kkl.count;
        this.dvl_available_count += dvl_st_to_wst.count;

        System.out.println("available rooms: " + dvl_available_count);

//        return dvl_available_count;
        return "fix_dvl_available_count";
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
