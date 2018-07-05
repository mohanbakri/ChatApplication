package com.example.pcc.chatting;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    private String msg;
    private String from;
    private String to;
    private String date;
    private long id;
    private String type;
    private String kind;
    private static final long serialVersionUID = 2L;

    public Message(String msg, String from, String to, String date, long id, String type) {
        this.msg = msg;
        this.from = from;
        this.to = to;
        this.date = date;
        this.id = id;
        this.type = type;
    }


    public String getMsg() {
        return msg;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setDate() {
        System.out.println("Message.setDate");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime sentDate = LocalDateTime.now();
        date = dtf.format(sentDate);
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getKind() {
        return kind;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public void printMessage() {
        System.out.println("from : " + from +
                "\n to : " + to +
                "\n id : " + id +
                "\n date : " + date +
                "\n type : " + type +
                "\n kind : " + kind +
                "\n message " + msg
        );
    }
}
