package com.example.groupproject;

import com.google.firebase.database.DatabaseReference;

public class ChatMessage {

    String message;
    String send_id;
    String time;

    public ChatMessage(){

    }

    public ChatMessage(String message, String send_id, String time) {
        this.message = message;
        this.send_id = send_id;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getSend_id() {
        return send_id;
    }


    public String getTime() {
        return time;
    }
}
