package com.example.groupproject;

import android.util.Log;

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

    /*public static boolean getUserInfo(FirebaseUser user){
        userID = user.getUid();
        userEmail = user.getEmail();


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            Map<String,Object> temp = new HashMap<>();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                temp = document.getData();
                                storeAllUserInfo(temp);

                                if(userID.equals(document.getId())){
                                    if(temp.isEmpty()){
                                        break;
                                    }else {
                                        storeUserInfo(temp);
                                    }
                                }
                            }

                            //storeFriendsInfo();

                        }else{
                            Log.w("tag", "Error getting document.",task.getException());
                        }
                    }
                });
        return true;
    }

    protected static void storeUserInfo(Map<String, Object> data){
        userGender = data.get("gender").toString();
        username = data.get("username").toString();
        userBirthday = data.get("birthday").toString();
        //userPhone = data.get("phone").toString();
        userFriendsID = (ArrayList<String>)data.get("friends");
    }

    protected static void storeAllUserInfo(Map<String, Object> data){
        allUser.add(data);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
    }*/

    /*protected static void storeFriendsInfo(){

        for(int i=0; i<userFriendsID.size(); i++){
            for(int j=0; j<allUser.size(); j++){
                String friendID = userFriendsID.get(i);
                if(allUser.get(j).get("UID") == friendID ){
                    Map<String, Object> temp = new HashMap<>(allUser.get(j));
                    friends.put(temp.get("UID"), );
                }
            }
        }

    }*/
}
