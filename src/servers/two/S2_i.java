package servers.two;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface S2_i extends Remote {

    public  Boolean createroom(String rno, String date, String timeslot)throws java.rmi.RemoteException, FileNotFoundException, UnsupportedEncodingException;;

    public String bookroom(String campusName,String rno,String date,String timeslot,String UID)throws RemoteException, InterruptedException;

}
