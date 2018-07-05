package Package;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    private static ServerSocket server;
    private static HashMap<String, Client> users;
    private static final Object lock1 = new Object();


    private static void StartUp() throws Exception{
        users = new HashMap<String,Client>();
        server=new ServerSocket(8080);
        Data.loadUsersData();
        Received_Request();
    }

    private static void Received_Request() throws IOException {
            while(true){
                Socket user = server.accept();
                new Thread(new Account(user)).start();
            }
    }

    public static void main(String[] args) throws Exception {
        StartUp();
    }

    static void addToHashMap(String userName, Client client) {
       synchronized (lock1) {
           users.put(userName, client);
       }
    }

    static Client getUserThreadByName(String to) {
        return users.getOrDefault(to,null);

    }

    static void removeSocket(String userName) {
        synchronized (lock1){
            users.remove(userName);
        }

    }
}
