package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import  com.example.groupproject.appCookies.*;


public class friendlistActivity extends AppCompatActivity {

    Toolbar toolbar;

    private ListView listview;
    private ArrayList<String> list = new ArrayList<String>();

    public static final String FRIEND_NAME = "com.example.groupproject.FRIEND_NAME";

    private String username = new String();
    private String userID = new String();
    private String userEmail = new String();
    private String userGender = new String();
    private String userBirthday = new String();

    private ArrayList<String> userFriendsID = new ArrayList<String>();
    private ArrayList<Map<String,Object>> allUser = new ArrayList<>();
    private Map<String, Object> friends = new HashMap<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview = (ListView)findViewById(R.id.item);

        //storeData(username, userID, userEmail, userGender, userBirthday);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getUserInfo(user);

        listview.setAdapter(adapter);

        //turn to chat window
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position,
                                    long id) {

                String friendName = (String)parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra(FRIEND_NAME, friendName);
                intent.setClass(friendlistActivity.this,chat_nav.class);
                startActivity(intent);
            }

        });
    }

    private void getUserInfo(FirebaseUser user){
        userID = user.getUid();
        userEmail = user.getEmail();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userID);

        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            storeUserInfo(documentSnapshot);
                            storeFriendsInfo();
                        }else{
                            Toast.makeText(friendlistActivity.this, "document does not exist",Toast.LENGTH_SHORT).show();;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        //Log.w("tag", "Error getting document.",task.getException());
    }

    protected void storeUserInfo(DocumentSnapshot data){
        userGender = data.getString("gender");
        username = data.getString("username");
        userBirthday = data.getString("birthday");
        userEmail = data.getString("email");
        userFriendsID = (ArrayList<String>)data.get("friends");
    }

    protected void storeFriendsInfo(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference friendRef = db.collection("users").document(userFriendsID.get(0));
        friendRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_friendlist,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.map_check:
                intent.setClass(this, map_main.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void toFriendlist(View view){
        Intent intent = new Intent();
        intent.setClass(this, friendlistActivity.class);
        startActivity(intent);
    }
    public void toChannel(View view){
        Intent intent = new Intent();
        intent.setClass(this, channel.class);
        startActivity(intent);
    }
    public void toSetting(View view){
        Intent intent = new Intent();
        intent.setClass(this, setting_main.class);
        startActivity(intent);
    }

}
