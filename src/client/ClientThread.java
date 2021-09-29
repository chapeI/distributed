package client;

public class ClientThread implements  Runnable {

    @Override
    public void run() {
        try {
            Client c = new Client();
            c.start_client();
        } catch (Exception e) {

        }

    }
}
