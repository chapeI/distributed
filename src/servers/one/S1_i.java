package servers.one;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.rmi.Remote;

public interface S1_i extends Remote {
    public  Boolean createroom(String rno, String date, String timeslot)throws java.rmi.RemoteException, FileNotFoundException, UnsupportedEncodingException;;
}
