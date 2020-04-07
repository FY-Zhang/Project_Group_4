package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class storeUserInfo {
    public static void storeInfo(FirebaseUser user){
        String id = user.getUid();
        String email = user.getEmail();
        ArrayList<String> friends = new ArrayList<String>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(db.collection("users").document(id) == null){
            CollectionReference users = db.collection("users");

            Map<String, Object> newUser = new HashMap<>();
            newUser.put("username", id);
            newUser.put("gender", "Male");
            newUser.put("email", email);
            newUser.put("UID", id);
            newUser.put("friends", friends);
            newUser.put("birthday", "1990/10/1");

            users.document(id).set(newUser);

        }else{
            return;
        }
    }
}
