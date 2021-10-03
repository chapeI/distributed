package client;

public class User {
    public static void main(String[] args) {
        ClientThread clientThread = new ClientThread();
        Thread ct = new Thread(clientThread);
        ct.start();
    }
}
