package com.example.groupproject;

public class ChatMessage {

    private String imgUrl = new String();
    private String send_id = new String();
    private String time = new String();
    private String message = new String();
    private double latitude = 200;
    private double longitude = 200;
    private String location = new String();
    private int type = -1;

    public ChatMessage(){
        //empty constructor needed
    }
    // 0->img
    // 1->message
    public ChatMessage( String send_id, String time, int type, String message, String imgUrl, String location, double latitude, double longitude) {
        this.imgUrl = imgUrl;
        this.send_id = send_id;
        this.time = time;
        this.type = type;
        this.message = message;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLocation() {
        return location;
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
