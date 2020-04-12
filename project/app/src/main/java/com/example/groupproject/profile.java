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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.groupproject.appCookies.userFriends;
import static com.example.groupproject.appCookies.userFriendsID;

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

        if(type.equals("friends")){
            for(int i=0; i<userFriends.size(); i++){
                if(userFriends.get(i).get("UID").equals(UID)){
                    user_email = userFriends.get(i).get("email").toString();
                    user_name = userFriends.get(i).get("username").toString();
                    user_gender = userFriends.get(i).get("gender").toString();
                    showGender = (boolean)userFriends.get(i).get("display");
                    break;
                }
            }
            setInfo();
            infoAdapter.addAll(content);
            information.setAdapter(infoAdapter);

        }else if(type.equals("request") || type.equals("unknown")){
            if(type.equals("unknown")){
                button1.setText("Send Friend Request");
            }else {
                button1.setText("Accept Request");
            }

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
        }


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
    public void showPost(View view){

    }
    public void button1Function(View view){

    }
}
