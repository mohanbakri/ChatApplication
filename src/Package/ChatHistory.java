package Package;

import com.example.pcc.chatting.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

class ChatHistory {
    private static final Object lock1 = new Object();

    static void storeMessage(Message msg) {
        String FileName = getFileName(msg.getFrom(), msg.getTo());
        String path = "c:\\project\\DataBase\\ChatHistory\\"+FileName+".json";

        if (FileName != null) {


            JSONObject message = new JSONObject();
            message.put("from", msg.getFrom());
            message.put("to", msg.getTo());
            message.put("date", msg.getDate());
            message.put("type", msg.getType());
            message.put("msg", msg.getMsg());

            try {
                System.out.println(FileName);
                JSONObject fileContain = (JSONObject) new JSONParser().parse(new FileReader(path));
                System.out.println("ChatHistory.storeMessage");
                JSONArray messages = (JSONArray) fileContain.get("messages");
                JSONObject lastMessage = (JSONObject) messages.get(messages.size() - 1);
                long lastId = (long) lastMessage.get("id");
                lastId++;
                msg.setId(lastId);

                message.put("id", lastId);
                //send this id to the user

                messages.add(message);
                fileContain.put("messages", messages);
                writeMessagesOnFile(fileContain, path);

            } catch (IOException | ParseException e) {
                //if it's the first message
                JSONObject fileForm = new JSONObject();
                boolean sort = isSmallerBigger(msg.getFrom(), msg.getTo());


                if (sort) {
                    fileForm.put("client1", msg.getFrom());
                    fileForm.put("client2", msg.getTo());
                } else {
                    fileForm.put("client1", msg.getTo());
                    fileForm.put("client2", msg.getFrom());
                }
                fileForm.put("chatId", FileName);

                JSONArray messages = new JSONArray();
                message.put("id", 1);
                messages.add(message);
                fileForm.put("messages", messages);
                try {
                    writeMessagesOnFile(fileForm, path);
                } catch (FileNotFoundException ignored) {
                }
            }
        }
    }

    private static boolean isSmallerBigger(String from, String to) {
        long id1 = Data.getIdByUserName(from);
        long id2 = Data.getIdByUserName(to);
        return id1 < id2;
    }

    private static String getFileName(String from, String to) {
        long id1 = Data.getIdByUserName(from);
        long id2 = Data.getIdByUserName(to);
        if (id1 != 0 && id2 != 0)
            if (id1 < id2)
                return id1 + "," + id2;
            else
                return id2 + "," + id1;
        else
            return null;
    }


    static Message deleteMessage(Message msg) {
        String FileName = getFileName(msg.getFrom(), msg.getTo());
        String path = "   ChatHistory\\" + FileName + ".json";
        try {
            JSONObject fileContent = (JSONObject) new JSONParser().parse(new FileReader(path));
            JSONArray jsonArray = (JSONArray) fileContent.get("messages");
            jsonArray = Data.sortJSONArray(jsonArray);
            jsonArray = removeFromJsonArray((JSONObject) jsonArray.get((int) (msg.getId() - 1)), jsonArray);
            JSONObject addedMessage = new JSONObject();
            addedMessage.put("from", msg.getFrom());
            addedMessage.put("to", msg.getTo());
            addedMessage.put("date", msg.getDate());
            addedMessage.put("type", "");
            addedMessage.put("msg", "");
            addedMessage.put("id",msg.getId());
            jsonArray = addToJsonArray(addedMessage, jsonArray);
            jsonArray = Data.sortJSONArray(jsonArray);
            fileContent.put("messages",jsonArray);
            writeMessagesOnFile(fileContent,path);
            Message cleanOrder;//= new Message("",msg.getFrom(),msg.getTo(),msg.getDate(),msg.getId(),"deleted-message");
            cleanOrder = msg;
            //TODO clean it up from client devise
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return msg;
    }

    private static JSONArray removeFromJsonArray(JSONObject jsonObject, JSONArray jsonArray) {
        synchronized (lock1) {
            jsonArray.remove(jsonObject);
        }
        System.out.println("old node removed");
        return jsonArray;
    }

    private static JSONArray addToJsonArray(JSONObject jsonObject, JSONArray jsonArray) throws FileNotFoundException {
        synchronized (lock1) {
            jsonArray.add(jsonObject);
        }
        System.out.println("new node added");
        return jsonArray;

    }


    private static void writeMessagesOnFile(JSONObject jsonObject, String path) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(path);
        pw.write(jsonObject.toJSONString());

        pw.flush();
        pw.close();
    }
}
