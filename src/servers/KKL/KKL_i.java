package servers.KKL;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface KKL_i extends Remote {

    public  Boolean createroom(String rno, String date, String timeslot)throws java.rmi.RemoteException, FileNotFoundException, UnsupportedEncodingException;;

    public String bookroom(String campusName,String rno,String date,String timeslot,String UID)throws RemoteException, InterruptedException;

    public String bookroom2(String campusName,String rno,String date,String timeslot,String UID)
            throws RemoteException, InterruptedException, MalformedURLException, NotBoundException;

    public void listener(int a,int b,String date, String uid) throws RemoteException;


}
