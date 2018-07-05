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
import java.nio.file.Files;
import java.nio.file.Paths;

class TemporaryStore {
    static void addToQueue(Message msg) {
        Long userId = Data.getIdByUserName(msg.getTo());

        String path = "c:\\project\\DataBase\\queue\\" + userId + ".json";
        JSONObject jsonObject = new JSONObject();
        if(msg.getType()==null)
            msg.setType("");
        if(msg.getKind()==null)
            msg.setKind("");
        jsonObject.put("msg", msg.getMsg());
        jsonObject.put("from", msg.getFrom());
        jsonObject.put("to", msg.getTo());
        jsonObject.put("date", msg.getDate());
        jsonObject.put("type", msg.getType());
        jsonObject.put("id", msg.getId());
        jsonObject.put("kind", msg.getKind());
        JSONArray jsonArray;
        synchronized (userId) {
            try {
                jsonArray = (JSONArray) new JSONParser().parse(new FileReader(path));
                jsonArray.add(jsonObject);
            } catch (IOException | ParseException ignored) {
                jsonArray = new JSONArray();
                jsonArray.add(jsonObject);
            }
                writeArrayData(jsonArray, path);


        }
    }

    static void getUserMessages(String userName) {
        Long userId = Data.getIdByUserName(userName);
        String path = "c:\\project\\DataBase\\queue\\" + userId + ".json";
        Message message;
        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonArray = (JSONArray) new JSONParser().parse(new FileReader(path));
            Thread.sleep(4000);

            for (Object object : jsonArray) {
                System.out.println("out msg");
                jsonObject = (JSONObject) object;
                message = new Message(jsonObject.get("msg").toString(),
                        jsonObject.get("from").toString(),
                        jsonObject.get("to").toString(),
                        jsonObject.get("date").toString(),
                        Long.parseLong(jsonObject.get("id").toString()),
                        jsonObject.get("type").toString(),
                        jsonObject.get("kind").toString());
                message.printMessage();
                Client.sendMessage(message);
            }
            String empty = "[]";
            jsonArray = (JSONArray) new JSONParser().parse(empty);
            writeArrayData(jsonArray, path);
        } catch (IOException | ParseException ignored) {
            System.out.println("there is no queued message for  " + userName);
        } catch (InterruptedException ignored) {
        }


    }


    private static void writeArrayData(JSONArray jsonArray, String filePath)  {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(filePath);
        } catch (FileNotFoundException e) {
            try {
                Files.createDirectories(Paths.get("../DataBase/queue"));
                pw = new PrintWriter(filePath);
            } catch (IOException ignored) {
            }

        }
        if (jsonArray != null)
            pw.write(jsonArray.toJSONString());
        else
            pw.write("[]");

        pw.flush();
        pw.close();
    }
}
