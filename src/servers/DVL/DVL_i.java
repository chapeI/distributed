package servers.DVL;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DVL_i extends Remote {
    public  Boolean createroom(String rno, String date, String timeslot)throws Exception;

    public String bookroom(String campusName, String rno, String date, String timeslot, String UID)
            throws RemoteException, InterruptedException, MalformedURLException, NotBoundException;

    public void listener() throws RemoteException, MalformedURLException, NotBoundException;

//    public int get_count(String date) throws RemoteException;

//    public void cancelBooking(String bookingid) throws RemoteException;

    public int getAvailableTimeSlot(String Date) throws RemoteException, InterruptedException;

}
