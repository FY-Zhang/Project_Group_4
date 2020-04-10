package com.example.groupproject;

public class ChatMessage {

    private String imgUrl;
    private String send_id;
    private String time;
    private String message;
    private int type;

    public ChatMessage(){
        //empty constructor needed
    }
    // 0->img
    // 1->message
    public ChatMessage(String imgUrl, String send_id, String time, int type, String message) {
        this.imgUrl = imgUrl;
        this.message = message;
        this.send_id = send_id;
        this.time = time;
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getSend_id() {
        return send_id;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setSend_id(String send_id) {
        this.send_id = send_id;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
