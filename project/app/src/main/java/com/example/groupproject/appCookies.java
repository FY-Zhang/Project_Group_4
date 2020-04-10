package com.example.groupproject;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Map;


public class appCookies {
    public static String username = new String();
    public static String userID = new String();
    public static String userEmail = new String();
    public static String userGender = new String();
    public static String userBirthday = new String();
    public static ArrayList<Map<String,Object>> userFriends = new ArrayList<>();
    public static ArrayList<Map<String,Object>> userChecked = new ArrayList<>();
    public static ArrayList<GeoPoint> userCheckedPoints = new ArrayList<>();

    public static void storeData(String name, String ID, String Email, String Gender,
                                 String Birthday,ArrayList<Map<String, Object>> Friends, ArrayList<GeoPoint>checkedPoints){
        username = name;
        userID = ID;
        userEmail = Email;
        userGender = Gender;
        userBirthday = Birthday;
        userFriends = new ArrayList<>(Friends);
        userCheckedPoints = new ArrayList<>(checkedPoints);
    }
    public static void storeFriendsInfo( ArrayList<Map<String, Object>> Friends){
        userFriends = new ArrayList<Map<String, Object>>(Friends);
    }

}
