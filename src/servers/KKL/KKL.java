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

    DVL_i dvl;

    public KKL() throws RemoteException {
        super();
    }

    String bookingid = "bookingID_debug";

    // 	room_record = Hashmap < date , Hashmap< rno , ts >>
    static HashMap<String,HashMap<String, HashMap<String,String>>> a = new HashMap< String, HashMap<String,HashMap<String,String>>>();

    //   room_numbers -> { time_slots }
    static HashMap<String,HashMap<String,String>> b = new HashMap<String,HashMap<String,String>>();

    //  time_slots -> "available"
    static HashMap<String,String> c = new HashMap<String,String>();

    // HashMap < uid ,  [ bookingId1, bookingId2.. ] >
    static HashMap<String, ArrayList<String>> d = new HashMap<String,ArrayList<String>>();

    //  can add things like "UID", and what else?
    static ArrayList<String> e = new ArrayList<String>();

    int cou = 0;  // why.


    @Override
    public Boolean createroom(String rno, String date, String timeslot) throws RemoteException, FileNotFoundException, UnsupportedEncodingException {
        System.out.println("KKL.createroom()");

        // TODO: FileWriter goes here..

        // time_slot check?
        Set<String> setr = b.keySet();  // set of room_numbers
        Set<String> sett = c.keySet();  // set of time_slots
        Iterator ir = setr.iterator();  // iterating on the set of room_numbers
        Iterator it = sett.iterator();  // iterating on the set of time_slots

        // check if room exists
        while(ir.hasNext())
        {
            String s = (String)ir.next();  // s is room_number
            System.out.println("s: " + s);

            // if room exists, check if timeslot exists
            if(s.equals(rno))
            {
                while(it.hasNext())
                {
                    String s1 = (String)it.next();
                    if(s1.equals(timeslot))
                    {
                        System.out.println("can't book, timeslot Already Exists");
                        return false;
                    }
                }
            }
        }

        c.put(timeslot,"Available");   //  ts: "available"      < String, String >
        b.put(rno, c);   //   rn : { c }
        a.put(date, b);

        System.out.println("KKL server created room. RoomRecords data structure stored in KKL now looks like -> " + a);
        return true;
    }

    public String bookroom(String campusName,String rno,String date,String timeslot,String UID) throws RemoteException, InterruptedException {
        System.out.println("\n~~ KKL.bookroom()");
//        System.out.println("UID: " + UID);
        int i = 0;

        try {
            if(d.isEmpty()) {
//                System.out.println("d = 0, no uid's in d . putting (" + UID + ", 'UID') in d)");
                e.add("UID");
                d.put(UID, e);
//                System.out.println("d: " + d);
            }

            Set<String> set = d.keySet();
            Iterator it = set.iterator();

            while(it.hasNext()) {

                String s = (String)it.next();
//                System.out.println("s: " + s);
//                System.out.println("UID: " + UID);
//                System.out.println("does " + s + " == " + UID);
                if(UID == s) {
                    System.out.println("HIT");
                    i++;
                }
            }

//            System.out.println("expecting i = 1. i = " + i);

            if(i == 0) {
                System.out.println("i = 0");
                e.add("UID");
                d.put(UID, e);
                System.out.println("e: " + e);
                System.out.println("d " + d);
                i = 0;
            }

            if(d.get(UID).size() > 3) {
                System.out.println("You have reached the booking limit already");
                return "limit reached";
            } else {
//                System.out.println("booking room");
//                si1 = (s1_i)Naming.lookup("rmi://localhost:25011/tag1");
//                si3 = (server3interface)Naming.lookup("rmi://localhost:25013/tag3");
                if(campusName.equals(new String("KKL"))) {
                    bookingid = UUID.randomUUID().toString();
                    System.out.println("bookingid: " + bookingid);

                    if(a.get("Monday").get("2").get("9:00") == "Available") {
                        System.out.println("should see this message before 'booked'"); // TODO: use this as a check
                    } else {
                        System.out.println("this shit breaks"); // TODO: handle this properly
                    }

                    System.out.println("KKL RRs -> a (before booking): " + a);
                    a.get("Monday").get("2").put("9:00","WORKING");
                    System.out.println("KKL RRs -> a (after booking): " + a);
//                    a.get(date).get(rno).put(timeslot, "sdf");
//                    String sdf = a.get(date).get(rno).get(timeslot);
//                    System.out.println("sdf: " + sdf);
//                    String get_bookingID = a.get(date).get(rno).get(timeslot);
//                    System.out.println("get_bookingID: " + get_bookingID);
//                    roomcount--;
                    System.out.println("booked");
                } else if(campusName.equals(new String("DVL"))) {
                    System.out.println("sending request to DVL.bookRoom()");
//                    bookingid = si1.bookroom(campusName,rno,date,timeslot,UID);
                } else if(campusName.equals(new String("WST"))) {
                    System.out.println("sending request to WST.bookRoom()");
//                    bookingid = si3.bookroom(campusName,rno,date,timeslot,UID);
                }
            }
//            d.get(UID).add((bookingid));
        } catch (Exception e) { }

        return "DEBUG";

//        catch(NotBoundException e ) { }
//        catch (MalformedURLException e) { }

//        System.out.println("before return");
//        return bookingid;
    }

    public String bookroom2(String campusName,String rno,String date,String timeslot,String UID) throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {

        dvl = (DVL_i) Naming.lookup("rmi://localhost:35000/tag1"); // TODO: move this to the top
        // wst reference goes here

        if(campusName.equals("KKL")) {
            bookingid = UUID.randomUUID().toString();
            System.out.println("bookingid: " + bookingid);

            // TODO: configure check
            if(a.get("Monday").get("2").get("9:00") == "Available") {
                System.out.println("should see this message before 'booked'");
            } else {
                System.out.println("this shit breaks"); // TODO: handle properly
            }

            System.out.println("KKL RR (before booking): " + a);
            a.get("Monday").get("2").put("9:00","WORKING");
            System.out.println("KKL RR (after booking): " + a);

        } else if(campusName.equals(new String("DVL"))) {
            bookingid = dvl.bookroom2(campusName, rno, date, timeslot, UID);
        } else if(campusName.equals(new String("WST"))) {
            System.out.println("sending request to WST.bookRoom()");
        }

        return "debug";
    }

    public void listener(int a, int b, String date, String uid) throws RemoteException
    {
        threadlistner tl1=new threadlistner(cou,a,date, uid);
        //threadlistner tl2=new threadlistner(cou,b);
        Thread t3=new Thread(tl1);
        //Thread t4=new Thread(tl2);
        t3.start();
        //t4.start();

    }

    public int get_count(String date) {
        System.out.println("date: " + date);
        return 4;
    }

}
