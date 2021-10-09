package servers.WST;

import servers.DVL.DVL_i;
import servers.KKL.KKL_i;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.UUID;

public class WST extends UnicastRemoteObject implements WST_i {
    DVL_i dvl_i;
    KKL_i kkl_i;
    int wst_available_count = 0;

    // 	HM<date, HM<rno, HM<time, b_id>>>
    static HashMap<String,HashMap<String, HashMap<String,String>>> a = new HashMap< String, HashMap<String,HashMap<String,String>>>();
    String bookingid = "bookingID_debug";

    public WST() throws RemoteException {
        super();
        make_new_date(a, "Thursday", "4", "1:00");
        make_new_date(a, "Wednesday", "3", "6:00");
        make_new_date(a, "Tuesday", "4", "6:00");
        make_new_date(a, "Monday", "5", "6:00");
        make_new_date(a, "Wednesday", "6", "6:00");
        System.out.println("WST(): " + a);
    }

    @Override
    public Boolean createroom(String rno, String date, String timeslot) throws RemoteException, FileNotFoundException, UnsupportedEncodingException {
        // muted while testing. these are being initialized in the const'r
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

    public String bookroom(String campusName, String rno, String date, String timeslot, String UID) throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {
        dvl_i = (DVL_i) Naming.lookup("rmi://localhost:35000/tag1"); // TODO: move to top
        // add kkl

        if(campusName.equals("WST")) {
            bookingid = UUID.randomUUID().toString();
//            System.out.println("bookingid: " + bookingid);

            // TODO: put the actual booking lines into the if
//            if(a.get("Monday").get("1").get("9:00") == "Available") {
//                System.out.println("should see this message before 'booked'");
//            } else {
//                System.out.println("shit breaks"); // TODO: handle properly
//            }

            a.get("Monday").get("1").put("3:00","BOOKINGID_1");
            System.out.println(a);

        } else if(campusName.equals(new String("DVL"))) {
            bookingid = dvl_i.bookroom2(campusName, rno, date, timeslot, UID);
        } else if(campusName.equals(new String("KKL"))) {
            System.out.println("sending request to KKL.bookRoom()");
        }

        return "debug";
    }

    public void listener() throws RemoteException, MalformedURLException, NotBoundException {
        ListenerThread lt=new ListenerThread();
        Thread t =new Thread(lt);
        t.start();
    }

    public int get_count(String date) throws RemoteException {
        int count = 0;
        HashMap<String, HashMap<String, String>> day;
        day = a.get(date);
        System.out.println(day);

        for(var r: day.entrySet()) {
            System.out.println("rno: " + r.getKey());
            for(var t : r.getValue().entrySet()) {
                System.out.println("t: " + t.getValue());
                String time = t.getValue();
                if(time.equals("available")) {
                    count += 1;
                }
            }
        }
        System.out.println("count: " + count);
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

    public int getAvailableTimeSlot(String date) throws RemoteException, InterruptedException {
        this.wst_available_count += this.get_count(date);
        System.out.println("wst_available_count(before): " + wst_available_count);

        try {
            dvl_i=(DVL_i) Naming.lookup("rmi://localhost:35000/tag1");
            dvl_i.listener(); // create a listener thread on dvl
            kkl_i=(KKL_i) Naming.lookup("rmi://localhost:35001/tag2");
            kkl_i.listener();
        } catch(NotBoundException e ) {
            System.err.println(e);
        } catch (MalformedURLException e) {
            System.err.println(e);
        }


        WST_sendingThread wst_dvl = new WST_sendingThread(date, 2172);
        WST_sendingThread wst_kkl = new WST_sendingThread(date, 2170);

        Thread t1 = new Thread(wst_dvl);
        Thread t2 = new Thread(wst_kkl);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        this.wst_available_count += wst_dvl.count;
        this.wst_available_count += wst_kkl.count;

        System.out.println("available rooms: " + wst_available_count);

        return wst_available_count;
    }

}
