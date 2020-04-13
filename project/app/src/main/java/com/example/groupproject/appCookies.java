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
    public static ArrayList userFavorite = new ArrayList();
    public static String userAvatar = new String();
    public static boolean userDisplay;
    public static ArrayList<Map<String,Object>> userFriends = new ArrayList<>();
    public static ArrayList<String> userFriendsID = new ArrayList<>();
    public static ArrayList<Map<String,Object>> userChecked = new ArrayList<>();
    public static ArrayList<GeoPoint> userCheckedPoints = new ArrayList<>();

    public static void storeData(String name, String ID, String email, String gender, String birthday,
                                 boolean display, ArrayList<Map<String, Object>> Friends,
                                 ArrayList<GeoPoint>checkedPoints, ArrayList<String> friendsID,
                                String avatar, ArrayList<String> favorite){
        username = name;
        userID = ID;
        userEmail = email;
        userGender = gender;
        userBirthday = birthday;
        userDisplay = display;
        userFriends = new ArrayList<>(Friends);
        userCheckedPoints = new ArrayList<>(checkedPoints);
        userFriendsID = friendsID;
        userAvatar = avatar;
        userFavorite = favorite;
    }
    public static void storeFriendsInfo( ArrayList<Map<String, Object>> Friends){
        userFriends = new ArrayList<Map<String, Object>>(Friends);
    }
}
