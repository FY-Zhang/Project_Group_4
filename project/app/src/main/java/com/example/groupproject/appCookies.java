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
    //public static String userPhone = new String();
    public static ArrayList<String> userFriends = new ArrayList<String>();

    public static boolean getUserInfo(FirebaseUser user){
        Map<String,Object> data = new HashMap<>();
        userID = user.getUid();
        userEmail = user.getEmail();


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Map<String,Object> data = new HashMap<>();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                if(userID.equals(document.getId())){
                                    data = document.getData();
                                    if(data.isEmpty()){
                                        break;
                                    }else {
                                        getDataFromFirestore(data);
                                    }
                                }
                            }

                        }else{
                            Log.w("tag", "Error getting document.",task.getException());
                        }
                    }
                });
        return true;
    }

    public static void getDataFromFirestore(Map<String, Object> data){
        userGender = data.get("gender").toString();
        username = data.get("username").toString();
        userBirthday = data.get("birthday").toString();
        //userPhone = data.get("phone").toString();
        userFriends = (ArrayList<String>)data.get("friends");
    }
}
