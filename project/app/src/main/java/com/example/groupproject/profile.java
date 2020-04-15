package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firestore.v1.WriteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.groupproject.appCookies.userFriends;
import static com.example.groupproject.appCookies.userFriendsID;
import static com.example.groupproject.appCookies.userID;
import static com.example.groupproject.appCookies.username;

public class profile extends AppCompatActivity {

    String UID;
    String user_name;
    String user_gender;
    String user_email;
    String type;
    boolean showGender;
    Button button1;
    Button button2;

    ArrayList<String> content = new ArrayList<>();

    ArrayAdapter<String> infoAdapter;
    ListView information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        UID = intent.getStringExtra("UID");
        type = intent.getStringExtra("type");

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        information = findViewById(R.id.information);
        infoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(UID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user_email = documentSnapshot.getString("email");
                        user_name = documentSnapshot.getString("username");
                        user_gender = documentSnapshot.getString("gender");
                        showGender = documentSnapshot.getBoolean("display");

                        setInfo();
                        infoAdapter.addAll(content);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        information.setAdapter(infoAdapter);

        if(type.equals("friends") || userFriendsID.contains(UID)){

        }else if(type.equals("request")){
            button1.setText("Accept Request");

        }else if(type.equals("unknown")) {
            button1.setText("Send Friend Request");
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case "friends":
                        Intent intent = new Intent();
                        intent.putExtra("friend_id", UID);
                        intent.putExtra("friend_name", user_name);
                        intent.setClass(profile.this, chat_nav.class);
                        startActivity(intent);
                        break;
                    case "request":
                        addAsFriend();
                        button1.setText("SEND MESSAGE");
                        break;
                    case "unknown":
                        sendRequest();
                        break;
                }

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(profile.this, post_friend.class);
                intent.putExtra("friendId", UID);
                System.out.println("+++++++++++++++++++++++++Friend id: " + UID);
                startActivity(intent);
            }
        });
    }
    private void setInfo(){

        content.add("Username: "+ user_name);
        content.add("Email: "+user_email);
        if(showGender){
            content.add("Gender: "+user_gender);
        }else {
            content.add("Gender: Secret:)");
        }
    }
    private void addAsFriend(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(userID)
                .update("friends", FieldValue.arrayUnion(UID) );

        db.collection("users")
                .document(UID)
                .update("friends", FieldValue.arrayUnion(userID));

        userFriendsID.add(UID);
    }
    private void sendRequest(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(UID)
                .update("notifications", FieldValue.arrayUnion("0-0-"+userID));
        /*db.collection("users")
                .document(UID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> notifications = (ArrayList<String>)documentSnapshot.getData().get("notifications");
                        notifications.add(0, "0-0-"+userID);
                        update(notifications);
                    }
                });*/
    }
    public void update(ArrayList<String> arr){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(UID)
                .update("notifications", arr);
    }
}
