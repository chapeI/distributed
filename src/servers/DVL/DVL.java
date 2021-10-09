package servers.DVL;

import servers.KKL.KKL_i;
import servers.WST.WST_i;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class DVL extends UnicastRemoteObject implements DVL_i {

    KKL_i kkl_i;
    WST_i wst_i;
    int dvl_available_count = 0;

    // 	HM<date, HM<rno, HM<time, b_id>>>
    static HashMap<String,HashMap<String, HashMap<String,String>>> a = new HashMap< String, HashMap<String,HashMap<String,String>>>();

    DVL() throws RemoteException {
        super();
        make_new_date(a, "Tuesday", "5", "6:00");
        make_new_date(a, "Wednesday", "1", "4:00");
        make_new_date(a, "Wednesday", "2", "4:00");
        make_new_date(a, "Thursday", "6", "7:00");
        System.out.println("DVL(): " + a);
    }

    @Override
    public Boolean createroom(String rno, String date, String timeslot) throws RemoteException, FileNotFoundException, UnsupportedEncodingException {
//        make_new_date(a, "Monday", "1", "3:00");
//        make_new_date(a, "Monday", "1", "4:00");
//        make_new_date(a, "Wednesday", "2", "4:00");
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

    public String bookroom2(String campusName,String rno,String date,String timeslot,String UID)
            throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {

        String bookingid;
        System.out.println("\n~~ DVL.bookroom2()");

        kkl_i = (KKL_i) Naming.lookup("rmi://localhost:35001/tag2"); // TODO: move this to the top
        // wst reference goes here

        if(campusName.equals("DVL")) {
            bookingid = UUID.randomUUID().toString();
//            System.out.println("bookingid: " + bookingid);

//            // TODO: put booking in here
//            if(a.get("Monday").get("2").get("9:00") == "Available") {
//                System.out.println("should see this message before 'booked'");
//            } else {
//                System.out.println("this shit breaks"); // TODO: handle properly
//            }

//            System.out.println(a);
            a.get("Monday").get("2").put("9:00","WORKING");
            System.out.println(a);

        } else if(campusName.equals(new String("KKL"))) {
            bookingid = kkl_i.bookroom(campusName, rno, date, timeslot, UID);
        } else if(campusName.equals(new String("WST"))) {
            System.out.println("sending request to WST.bookRoom()");
        }

        System.out.println("~~ DVL.bookroom2() done");
        // prolly need to return bookingid
        return "DEBUG";
    }

    public int getAvailableTimeSlot(String date) throws RemoteException, InterruptedException {
        this.dvl_available_count += this.get_count(date);
        System.out.println("dvl_available_count(before): " + dvl_available_count);

        try {
            kkl_i=(KKL_i)Naming.lookup("rmi://localhost:35001/tag2");
            kkl_i.listener(); // create a listener thread on kkl
            wst_i=(WST_i)Naming.lookup("rmi://localhost:35002/tag3");
            wst_i.listener();

        } catch(NotBoundException e ) {
            System.err.println(e);
        } catch (MalformedURLException e) {
            System.err.println(e);
        }

        // KKL
        DVL_sendingThread dvl_st_to_kkl = new DVL_sendingThread(date, 2170);  // sending(date) to kkl (port 2170)
        DVL_sendingThread dvl_st_to_wst = new DVL_sendingThread(date, 2171);

        Thread t1=new Thread(dvl_st_to_kkl);
        Thread t2 = new Thread(dvl_st_to_wst);
        System.out.println("reach");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        this.dvl_available_count += dvl_st_to_kkl.count;
        this.dvl_available_count += dvl_st_to_wst.count;

        System.out.println("available rooms: " + dvl_available_count);

//        System.out.println("dvl_available_count(after KKL): " + dvl_available_count);

        // WST
//        DVL_sendingThread dvl_st_to_wst = new DVL_sendingThread(date, 2171);
//        Thread t2 = new Thread(dvl_st_to_wst);
//        t2.start();
//        t2.join();
//        this.dvl_available_count += dvl_st_to_wst.count;
//        System.out.println("dvl_available_count(after WST): " + dvl_available_count);

        return 1;
    }

    public int get_count(String date) throws RemoteException {
        int count = 0;
        HashMap<String, HashMap<String, String>> day;
        day = a.get(date);
//        System.out.println(day);

        for(var r: day.entrySet()) {
//            System.out.println("rno: " + r.getKey());
            for(var t : r.getValue().entrySet()) {
//                System.out.println("t: " + t.getValue());
                String time = t.getValue();
                if(time.equals("available")) {
                    count += 1;
                }
            }
        }
//        System.out.println("count: " + count);
        return count;
    }

    public void cancelBooking(String bookingid) throws RemoteException {
        for(var d : a.entrySet()) {
//            System.out.println("1. d: " + d);
//            System.out.println("2. d: " + d.getKey());
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
