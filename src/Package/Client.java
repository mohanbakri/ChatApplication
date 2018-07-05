package Package;

import java.io.*;
import java.net.Socket;

import com.example.pcc.chatting.Message;
import com.example.pcc.chatting.User;


public class Client implements Runnable {
    private User user;
    private volatile boolean signedIn;
    private Socket client;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String macAddress;
    private volatile boolean online;


    Client(User user, Socket client, ObjectOutputStream oos, ObjectInputStream ois) {
        this.user = user;
        this.signedIn = true;
        this.client = client;
        this.macAddress = user.getMac_Address();
        this.oos = oos;
        this.ois = ois;
        this.online = true;
    }

    Client(Socket client, String mac_Address, ObjectOutputStream oos, ObjectInputStream ois) {
        this.client = client;
        this.macAddress = mac_Address;
        this.online = true;
        this.signedIn = false;
        this.oos = oos;
        this.ois = ois;

    }

    @Override
    public void run() {
        if (!signedIn) {
            System.out.println("going to sign");
            sign();
        }
        Server.addToHashMap(user.getUserName(), this);
        TemporaryStore.getUserMessages(user.getUserName());
        chat();
    }

    private void chat() {
        while (online) {
            Message msg = null;
            try {
                msg = (Message) ois.readObject();
            } catch (EOFException EOF) {
                offline();
                try {
                    this.client.close();
                    Server.removeSocket(this.user.getUserName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("handle EOF exception");
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                this.offline();
            }

            assert msg != null;
            switch (msg.getKind()) {

                case "private_chat":
                    messaging(msg);
                    break;

                case "search_request":
                    searchRequest(msg);
                    break;

                case "sign_out":
                    Data.signOut(user.getUserName());
                    refuse();
                    break;

                case "delete_account":
                    break;

                case "deleted-message":
                    // ChatHistory.deleteMessage(msg);
                    // cleanItFromClient(msg);
                    break;

                case "change_status":
                    break;

                case "change_nickName":
                    break;
            }
        }
    }


    private void messaging(Message msg) {
        switch (msg.getType()) {
            case "TEXT_MSG":
                msg.setDate();
                ChatHistory.storeMessage(msg);
                boolean successSend = sendMessage(msg);
                if (!successSend) {
                    System.out.println("the receiver is offline .. will added to queue ");
                    TemporaryStore.addToQueue(msg);
                }
                break;

            case "video-message":
                break;
            case "audio-message":
                break;
            case "image-message":
                break;
        }

    }

    private void searchRequest(Message msg) {
        String name = msg.getMsg();
        try {
            oos.writeBoolean(Data.existUserName(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean sendMessage(Message msg) {
        if (Server.getUserThreadByName(msg.getTo()) != null) {
            ObjectOutputStream receiver = Server.getUserThreadByName(msg.getTo()).getOutputStream();
            try {
                receiver.writeObject(msg);
                receiver.flush();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("the message didn't send");
                //TODO : ensure that the message sent
            }
        } else {
            return false;
        }
        return true;
    }


    private void sign() {
        //sign-in (up)
        while (!signedIn && online) {
            try {
                System.out.println("sign up or in");
                int state = ois.readInt();
                switch (state) {
                    case 1: //sign up
                        System.out.println("sign up");
                        user = (User) ois.readObject();
                        user.printUser();
                        if (Data.checkNewAccount(user)) {
                            user.setId(Data.generateId());
                            getIn();
                            System.out.println("the user with his values is :");
                            user.printUser();
                            Data.store(user);
                        } else
                            refuse();
                        break;
                    case 2://sign in
                        System.out.println("sign in");
                        user = (User) ois.readObject();
                        user.setMac_Address(macAddress);
                        user = Data.checkClientData(user);
                        if (user != null) {
                            getIn();
                            Data.changeState(user);
                        } else
                            refuse();
                }
            } catch (EOFException EOF) {
                offline();
                try {
                    this.client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("handle EOF exception");
                break;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }

    }

    private void getIn() throws IOException {
        user.setMac_Address(macAddress);
        user.setSignedIn(true);
        this.signedIn = true;
        oos.writeBoolean(true);//he signed in
        oos.writeUTF(user.getUserName());
        oos.flush();
    }


    private void refuse() {
        try {
            oos.writeBoolean(false);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void offline() {
        online = false;
    }

    private ObjectOutputStream getOutputStream() {
        return oos;
    }


}
