package Package;

import com.example.pcc.chatting.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


class Data {
    private static JSONArray jsonArray = new JSONArray();
    private static long id;
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();
    private static final String filePath = "C:\\project\\DataBase\\users.json";

    static User getUserByMac(String mac_address) {
       /* Iterator<JSONObject> itr2 = jsonArray.iterator();
        JSONObject user;
        while (itr2.hasNext()) {
            user = itr2.next();
            if (user.get("macAddress").toString().equals(mac_address)) {
                return new User(user.get("userName").toString(),
                        user.get("password").toString(),
                        user.get("email").toString(),
                        (long) user.get("id"),
                        user.get("macAddress").toString(),
                        (boolean) user.get("signedIn"));
            }
        }
        System.out.println("not founded");
        return null;*/

        //Iterator<JSONObject> itr2 = jsonArray.iterator();
        JSONObject user;
      // while (itr2.hasNext()) {
        for (Object object : jsonArray) {
            user = (JSONObject) object;
            if (user.get("macAddress").toString().equals(mac_address)) {
                return new User(user.get("userName").toString(),
                        user.get("password").toString(),
                        user.get("email").toString(),
                        (long) user.get("id"),
                        user.get("macAddress").toString(),
                        (boolean) user.get("signedIn"));
            }
        }
        System.out.println("not founded");
        return null;

    }

    private static User checkPasswordByEmail(User user) {
      /*  System.out.println("check password by email");
        Iterator<JSONObject> itr2 = jsonArray.iterator();
        JSONObject user1;
        User fullUser = null;
        while (itr2.hasNext()) {
            user1 = itr2.next();
            if (user1.get("email").toString().equals(user.getEmail())) {
                if (user1.get("password").toString().equals(user.getPassword())) {
                    fullUser = new User(user1.get("userName").toString(),
                            "0", user.getEmail(),
                            Long.parseLong(user1.get("id").toString()),
                            user.getMac_Address(),
                            true);
                    break;
                }
            }
        }
        return fullUser;*/
        System.out.println("check password by email");
        JSONObject user1;
        User fullUser = null;
        for(Object object : jsonArray){
            user1 = (JSONObject) object;
            if (user1.get("email").toString().equals(user.getEmail())) {
                if (user1.get("password").toString().equals(user.getPassword())) {
                    fullUser = new User(user1.get("userName").toString(),
                            "0", user.getEmail(),
                            Long.parseLong(user1.get("id").toString()),
                            user.getMac_Address(),
                            true);
                    break;
                }
            }
        }
        return fullUser;
    }

    private static User checkPasswordByName(User user) {
       /* System.out.println("check password by name");
        Iterator<JSONObject> itr2 = jsonArray.iterator();
        JSONObject user1;
        User fullUser = null;
        while (itr2.hasNext()) {
            user1 = itr2.next();
            if (user1.get("userName").toString().equals(user.getUserName())) {
                if (user1.get("password").toString().equals(user.getPassword())) {
                    fullUser = new User(user.getUserName(),
                            "0", user1.get("email").toString(),
                            Long.parseLong(user1.get("id").toString()),
                            user.getMac_Address(),
                            true);
                    break;
                }
            }
        }
        return fullUser;*/

        System.out.println("check password by name");
        JSONObject user1;
        User fullUser = null;
        for(Object object :jsonArray){
            user1 = (JSONObject) object;
            if (user1.get("userName").toString().equals(user.getUserName())) {
                if (user1.get("password").toString().equals(user.getPassword())) {
                    fullUser = new User(user.getUserName(),
                            "0", user1.get("email").toString(),
                            Long.parseLong(user1.get("id").toString()),
                            user.getMac_Address(),
                            true);
                    break;
                }
            }
        }
        return fullUser;

    }

    static boolean checkNewAccount(User newUser) {
        return checkNewEmail(newUser.getEmail())
                && checkNewUserName(newUser.getUserName());

    }

    private static boolean checkNewUserName(String userName) {
     /*   Iterator<JSONObject> itr2 = jsonArray.iterator();
        JSONObject user;
        while (itr2.hasNext()) {
            user = itr2.next();
            if (user.get("userName").toString().equals(userName)) {
                return false;
            }
        }
        return true;*/

        JSONObject user;
        for(Object object : jsonArray){
            user = (JSONObject) object;
            if (user.get("userName").toString().equals(userName)) {
                return false;
            }
        }
        return true;


    }

    private static boolean checkNewEmail(String email) {
       /* Iterator<JSONObject> itr2 = jsonArray.iterator();
        JSONObject user;
        while (itr2.hasNext()) {
            user = itr2.next();
            if (user.get("email").toString().equals(email)) {
                return false;
            }
        }
        return true;*/

        JSONObject user;
        for(Object object : jsonArray){
            user = (JSONObject) object;
            if (user.get("email").toString().equals(email)) {
                return false;
            }
        }
        return true;


    }

    static void store(User user) throws FileNotFoundException {

        Map m = new LinkedHashMap(6);
        m.put("id", user.getId());
        m.put("userName", user.getUserName());
        m.put("password", user.getPassword());
        m.put("email", user.getEmail());
        m.put("macAddress", user.getMac_Address());
        m.put("signedIn", true);

        addToJsonArrayAndFile(m);
        System.out.println("the user is stored");

    }

    private static void addToJsonArrayAndFile(Map m) throws FileNotFoundException {
        synchronized (lock1) {
            jsonArray.add(m);
            writeArrayData();
        }
        System.out.println("new node added");

    }

    private static void writeArrayData() throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(jsonArray.toJSONString());

        pw.flush();
        pw.close();
    }

    static void loadUsersData() throws IOException, ParseException {

        jsonArray = get_Users();
        JSONObject lastUser = (JSONObject) jsonArray.get(jsonArray.size() - 1);
        id = (long) lastUser.get("id");
        System.out.println("CURRENT ID : " + id);


    }

    private static JSONArray get_Users() throws IOException, ParseException {
        Object obj = new JSONParser().parse(new FileReader(filePath));
        return (JSONArray) obj;
    }

    //FIXME : you should return after the lazy deletion account
    static long generateId() {
        synchronized (lock2) {
            return ++id;
        }
    }

    static void changeState(User user) {
       /* System.out.println("changing state");
        Iterator<JSONObject> itr2 = jsonArray.iterator();
        JSONObject itr1;
        Map addedNode = new HashMap(6);
        while (itr2.hasNext()) {
            itr1 = itr2.next();
            if (itr1.get("userName").toString().equals(user.getUserName())) {
                addedNode = itr1;
                removeFromJsonArray(itr1);
                itr1.put("macAddress", user.getMac_Address());
                itr1.put("signedIn", true);
                try {
                    addToJsonArrayAndFile(addedNode);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }*/

        System.out.println("changing state");
        JSONObject user1;
        Map addedNode = new HashMap(6);
        for(Object object : jsonArray){
            user1 = (JSONObject) object;
            if (user1.get("userName").toString().equals(user.getUserName())) {
                addedNode = user1;
                removeFromJsonArray(user1);
                user1.put("macAddress", user.getMac_Address());
                user1.put("signedIn", true);
                try {
                    addToJsonArrayAndFile(addedNode);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }




    }

    private static void removeFromJsonArray(JSONObject itr1) {
        synchronized (lock1) {
            jsonArray.remove(itr1);
        }
        System.out.println("old node removed");
    }

    static long getIdByUserName(String userName) {
        /*Iterator<JSONObject> itr2 = jsonArray.iterator();
        JSONObject user1;
        while (itr2.hasNext()) {
            user1 = itr2.next();
            if (user1.get("userName").toString().equals(userName))
                return Long.parseLong(user1.get("id").toString());
        }
        return -1;*/

        JSONObject user1;
        for(Object object : jsonArray){
            user1 = (JSONObject) object;
            if (user1.get("userName").toString().equals(userName))
                return Long.parseLong(user1.get("id").toString());
        }
        return -1;
    }

    static boolean existUserName(String Username) {
       /* Iterator<JSONObject> itr2 = jsonArray.iterator();
        JSONObject user1;
        while (itr2.hasNext()) {
            user1 = itr2.next();
            if (user1.get("userName").toString().equals(Username))
                return true;
        }
        return false;*/
        JSONObject user1;
            for(Object object : jsonArray){
            user1 = (JSONObject) object;
            if (user1.get("userName").toString().equals(Username))
                return true;
        }
        return false;


    }

    static User checkClientData(User user) {
        if (!user.getUserName().equals(""))
            return checkPasswordByName(user);
        return checkPasswordByEmail(user);
    }
}
