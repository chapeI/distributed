package servers.one;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.rmi.Remote;

public interface S1_i extends Remote {
    public  String createroom()throws java.rmi.RemoteException, FileNotFoundException, UnsupportedEncodingException;;
}
