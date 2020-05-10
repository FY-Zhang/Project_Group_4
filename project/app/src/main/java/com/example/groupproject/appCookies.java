package com.example.groupproject;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Map;


public class appCookies {
    public static String username = "";
    public static String userID = "";
    public static String userEmail = "";
    public static String userGender = "";
    public static String userBirthday = "";
    public static ArrayList userFavorite = new ArrayList();
    public static String userAvatar /*initial to avoid null object, pls retain it*/
            = "https://firebasestorage.googleapis.com/v0/b/groupproject-ffdc4.appspot.com/o/user_avatar%2Favatar_default.jpg?alt=media&token=852659d8-aa5a-4022-a53f-abfb3c268aa6";
    public static String userPhone = "";
    public static boolean userDisplay;
    public static ArrayList<Map<String,Object>> userFriends = new ArrayList<>();
    public static ArrayList<String> userFriendsID = new ArrayList<>();
    public static ArrayList<Map<String,Object>> userChecked = new ArrayList<>();
    public static ArrayList<GeoPoint> userCheckedPoints = new ArrayList<>();
    public static String curUserLoc = "null-null";

    public static int frgState = 0;//frg change

    public static void storeData(String name, String ID, String email, String gender, String birthday,
                                 boolean display, ArrayList<Map<String, Object>> Friends,
                                 ArrayList<GeoPoint>checkedPoints, ArrayList<String> friendsID,
                                String avatar, ArrayList<String> favorite, String phone){
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
        userPhone = phone;

    }
    public static void storeFriendsInfo( ArrayList<Map<String, Object>> Friends){
        userFriends = new ArrayList<Map<String, Object>>(Friends);
    }
}
