package com.example.docportal.Doctor;

import java.util.List;
import java.util.Map;

public class MessageModel {
    public MessageModel() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public MessageModel(String msgId, String senderId, String messages, String times, String dates, String names) {
        this.msgId = msgId;
        this.senderId = senderId;
        message = messages;
        this.time = times;
        this.date = dates;
        this.name = names;
    }


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String msgId;
    private  String senderId;
    private String message;
}
