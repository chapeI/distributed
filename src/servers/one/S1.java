package servers.one;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class S1 extends UnicastRemoteObject implements S1_i {

    S1() throws RemoteException {
        super();
    }

    @Override
    public String createroom() throws RemoteException, FileNotFoundException, UnsupportedEncodingException {
        System.out.println("creating room in server");
        return "creating room in client";
    }
}
