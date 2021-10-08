package servers.KKL;

import servers.DVL.DVL_i;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class KKL extends UnicastRemoteObject implements KKL_i {

//    DVL_i dvl;

    public KKL() throws RemoteException {
        super();
        make_new_date(a, "Monday", "1", "3:00");
        make_new_date(a, "Monday", "1", "4:00");
        make_new_date(a, "Wednesday", "2", "4:00");
        System.out.println("KKL(): " + a);
        this.cancelBooking("BOOKINGID_1");
    }

    String bookingid = "bookingID_debug";

    // 	room_record = Hashmap < date , Hashmap< rno , ts >>
    static HashMap<String,HashMap<String, HashMap<String,String>>> a = new HashMap< String, HashMap<String,HashMap<String,String>>>();

    //   room_numbers -> { time_slots }
    static HashMap<String,HashMap<String,String>> b = new HashMap<String,HashMap<String,String>>();

    //  time_slots -> "available"
    static HashMap<String,String> c = new HashMap<String,String>();

    // HashMap < uid ,  [ bookingId1, bookingId2.. ] >
//    static HashMap<String, ArrayList<String>> d = new HashMap<String,ArrayList<String>>();

    //  can add things like "UID", and what else?
//    static ArrayList<String> e = new ArrayList<String>();

    int cou = 0;  // why.

    @Override
    public Boolean createroom(String rno, String date, String timeslot) throws RemoteException, FileNotFoundException, UnsupportedEncodingException {
//        make_new_date(a, "Monday", "1", "3:00");
//        make_new_date(a, "Monday", "1", "4:00");
//        make_new_date(a, "Wednesday", "2", "4:00");
        System.out.println("initializing in constructor. testing");
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

    public String bookroom2(String campusName,String rno,String date,String timeslot,String UID) throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {

//        dvl = (DVL_i) Naming.lookup("rmi://localhost:35000/tag1"); // TODO: move to top
        // add wst

        if(campusName.equals("KKL")) {
            bookingid = UUID.randomUUID().toString();
//            System.out.println("bookingid: " + bookingid);

            // TODO: put the actual booking lines into the if
//            if(a.get("Monday").get("1").get("9:00") == "Available") {
//                System.out.println("should see this message before 'booked'");
//            } else {
//                System.out.println("shit breaks"); // TODO: handle properly
//            }

//            System.out.println("before booking: " + a);
            a.get("Monday").get("1").put("3:00","BOOKINGID_1");
            System.out.println(a);

        } else if(campusName.equals(new String("DVL"))) {
//            bookingid = dvl.bookroom2(campusName, rno, date, timeslot, UID);
        } else if(campusName.equals(new String("WST"))) {
            System.out.println("sending request to WST.bookRoom()");
        }

        return "debug";
    }

    public void listener(int a, int b, String date, String uid) throws RemoteException, MalformedURLException, NotBoundException {
        threadlistner tl1=new threadlistner(cou,a,date, uid);
        Thread t3=new Thread(tl1);
        t3.start();

        //Thread t4=new Thread(tl2);
        //threadlistner tl2=new threadlistner(cou,b);
        //t4.start();
    }

    public int get_count(String date) throws RemoteException {
        // TODO: figure out how to get_count from a
        return 5;
    }

    public void cancelBooking(String bookingid) throws RemoteException {

        for(var d : a.entrySet()) {
//            System.out.println("1. e: " + e);
//            System.out.println("2. e: " + d.getKey());
            for(var rno : d.getValue().entrySet()) {
//                System.out.println("3. rno: " + rno.getKey());
                for(var t : rno.getValue().entrySet()) {
//                    System.out.println("4. t: " + t);
//                    System.out.println("5. t: " + t.getKey());
                    String booking = t.getValue();
                    if(booking.equals(bookingid)) {
                        System.out.println(bookingid + " found at {" + d.getKey() + " in rno=" + rno.getKey() + " @" + t.getKey() + "} (KKL campus)");
                        a.get(d.getKey()).get(rno.getKey()).put(t.getKey(), "available");  // cancelling and changing b_id to available
                        System.out.println("booking_id cancelled and changed to available. check a datastructure for proof");
                        return;
                    } else {
                        System.out.println("no bookings found for " + bookingid);
                    }
                }
            }
        }
    }
}
