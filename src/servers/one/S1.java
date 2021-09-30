package servers.one;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class S1 extends UnicastRemoteObject implements S1_i {

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


    S1() throws RemoteException {
        super();
    }

    @Override
    public Boolean createroom(String rno, String date, String timeslot) throws RemoteException, FileNotFoundException, UnsupportedEncodingException {
        System.out.println("entered S1.createroom()");

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

        System.out.println("server created room");
        return true;
    }
}
