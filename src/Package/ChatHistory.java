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

    static void storeMessage(Message msg) {
        String FileName = getFileName(msg.getFrom(), msg.getTo());
        String path = "   ChatHistory\\" + FileName + ".json";

        if (FileName != null) {
            boolean sort = isSmallerBigger(msg.getFrom(), msg.getTo());


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

    private static void writeMessagesOnFile(JSONObject jsonObject, String path) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(path);
        pw.write(jsonObject.toJSONString());

        pw.flush();
        pw.close();
    }
}
