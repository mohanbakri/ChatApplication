package Package;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.example.pcc.chatting.User;

public class Account implements Runnable {
    private ObjectInputStream oin;
    private ObjectOutputStream oos;
    private Socket client;

    Account(Socket client) throws IOException {
        oin = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
        oos = new ObjectOutputStream(new BufferedOutputStream(client.getOutputStream()));
        this.client = client;
    }

    @Override
    public void run() {
        try {
            String mac_Address = (String) oin.readObject();
            System.out.println(mac_Address);

            User user = Data.getUserByMac(mac_Address);
            if (user != null && user.isSignedIn()) {
                //FIXME : we can change it if we add online
                oos.writeBoolean(true);
                oos.flush();
                new Thread(new Client(user, client, oos, oin)).start();
            } else {
                oos.writeBoolean(false);
                oos.flush();
                new Thread(new Client(client, mac_Address, oos, oin)).start();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
