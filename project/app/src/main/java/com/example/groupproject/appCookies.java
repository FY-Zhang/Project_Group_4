package com.example.groupproject;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class appCookies {
    public static String username = new String();
    public static String userID = new String();
    public static String userEmail = new String();
    public static String userGender = new String();
    public static String userBirthday = new String();
    public static ArrayList<Map<String,Object>> userFriends = new ArrayList<>();

    public static void storeData(String name, String ID, String Email, String Gender, String Birthday,ArrayList<Map<String, Object>> Friends){
        username = new String(name);
        userID = new String(ID);
        userEmail = new  String(Email);
        userGender = new String (Gender);
        userBirthday = new String(Birthday);
        userFriends = new ArrayList<Map<String, Object>>(Friends);
    }
    public static void storeFriendsInfo( ArrayList<Map<String, Object>> Friends){
        userFriends = new ArrayList<Map<String, Object>>(Friends);
    }


}
