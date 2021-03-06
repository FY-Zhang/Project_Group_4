package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.groupproject.appCookies.userAvatar;
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
    String user_avatar;
    boolean showGender;
    ImageView avatar;
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
        avatar = findViewById(R.id.avatar);

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
                        user_avatar = documentSnapshot.getString("avatar");

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

        if(userFriendsID.contains(UID) || type.equals("friends") ){
            type = "friends";
        }else if(type.equals("request")){
            button1.setText(R.string.ar);

        }else if(type.equals("unknown")) {
            button1.setText(R.string.sfr);
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
                        finish();
                        break;
                    case "request":
                        addAsFriend();
                        button1.setText(R.string.sm);
                        Intent intentToFrd = new Intent();
                        intentToFrd.setClass(profile.this, friendlistActivity.class);
                        startActivity(intentToFrd);
                        break;
                    case "unknown":
                        sendRequest();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(profile.this);
                        dialog.setTitle("");
                        dialog.setMessage("Send Successfully :) ");
                        dialog.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        dialog.show();
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
        Picasso.with(avatar.getContext())
                .load(user_avatar)
                .into(avatar);
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

        button1.setText("SEND MESSAGE");
        type = "friends";
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
}
