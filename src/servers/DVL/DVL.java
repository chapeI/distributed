package servers.DVL;

import servers.KKL.KKL_i;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class DVL extends UnicastRemoteObject implements DVL_i {

    KKL_i kkl;

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

    DVL() throws RemoteException {
        super();
    }

    @Override
    public Boolean createroom(String rno, String date, String timeslot) throws RemoteException, FileNotFoundException, UnsupportedEncodingException {
        System.out.println("DVL.createroom()");

        // TODO: FileWriter goes here..

        // JUST A CHECK
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
        // CHECK END.

        c.put(timeslot,"Available");   //  ts: "available"      < String, String >
        b.put(rno, c);   //   rn : { c }
        a.put(date, b);

        System.out.println("DVL server created a room. RoomRecords for DVL now looks like -> " + a);
        return true;
    }

    public String bookroom2(String campusName,String rno,String date,String timeslot,String UID)
            throws RemoteException, InterruptedException, MalformedURLException, NotBoundException {

        String bookingid;
        System.out.println("\n~~ DVL.bookroom2()");

        kkl = (KKL_i) Naming.lookup("rmi://localhost:35001/tag2"); // TODO: move this to the top
        // wst reference goes here

        if(campusName.equals("DVL")) {
            bookingid = UUID.randomUUID().toString();
            System.out.println("bookingid: " + bookingid);

            // TODO: configure check
            if(a.get("Monday").get("2").get("9:00") == "Available") {
                System.out.println("should see this message before 'booked'");
            } else {
                System.out.println("this shit breaks"); // TODO: handle properly
            }

            System.out.println("DVL RR (before booking): " + a);
            a.get("Monday").get("2").put("9:00","WORKING");
            System.out.println("DVL RR (after booking): " + a);

        } else if(campusName.equals(new String("KKL"))) {
            bookingid = kkl.bookroom2(campusName, rno, date, timeslot, UID);
        } else if(campusName.equals(new String("WST"))) {
            System.out.println("sending request to WST.bookRoom()");
        }

        System.out.println("~~ DVL.bookroom2() done");
        // prolly need to return bookingid
        return "DEBUG";
    }

    public int getAvailableTimeSlot(String date) throws RemoteException, InterruptedException {
        System.out.println("DVL.getAvailableTimeSlot()");

        try {
            kkl=(KKL_i)Naming.lookup("rmi://localhost:35001/tag2");
            kkl.listener(); // thread for listening
        } catch(NotBoundException e ) {
            System.err.println(e);
        } catch (MalformedURLException e) {
            System.err.println(e);
        }

        // TODO: make a way for us to generate count for that date
        DVL_sending_thread dvl_st=new DVL_sending_thread(date);  // sending(date) thread
        Thread t1=new Thread(dvl_st);
        t1.start();
        t1.join();

        int dvl_available_room_count= dvl_st.count;
        System.out.println("dvl_available_room_count: " + dvl_available_room_count);
        return 1;
    }
}
