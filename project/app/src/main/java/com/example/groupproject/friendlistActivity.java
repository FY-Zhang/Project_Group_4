package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.groupproject.appCookies.storeData;
import static com.example.groupproject.appCookies.userChecked;


public class friendlistActivity extends AppCompatActivity {

    Toolbar toolbar;
    private int currentAdapter = 1;

    private ListView listview;
    private ArrayList<String> list = new ArrayList<String>();

    private String username = new String();
    private String userID = new String();
    private String userEmail = new String();
    private String userGender = new String();
    private String userBirthday = new String();
    private boolean userDisplay = false;

    private ArrayList<String> userFriendsID = new ArrayList<>();
    private ArrayList<Map<String,Object>> userFriends = new ArrayList<>();
    private ArrayAdapter<String> friendlistAdapter;

    private ArrayList<GeoPoint> userCheckedPoints = new ArrayList<>();
    private ArrayList<Map<String,Object>> userChecked = new ArrayList<>();

    ListView notificationsList;
    private ArrayList<String> userNotifications = new ArrayList<>();
    private ArrayAdapter<String> notificationAdapter;
    private ArrayList<String > notificationContent = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview = (ListView)findViewById(R.id.item);
        notificationsList = (ListView)findViewById(R.id.notification_list);


        //storeData(username, userID, userEmail, userGender, userBirthday);
        if(friendlistAdapter != null)
            friendlistAdapter.clear();
        friendlistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);

        if(notificationAdapter != null)
            notificationAdapter.clear();
        notificationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getUserInfo(user);

        listview.setAdapter(friendlistAdapter);

        //turn to chat window
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position,
                                    long id) {
                if(currentAdapter == 1) {
                    if (position == 0) {
                        listview.setAdapter(notificationAdapter);
                        currentAdapter = 0;
                    } else {
                        String friendName = (String) parent.getItemAtPosition(position);

                        Intent intent = new Intent();
                        intent.putExtra("friend_name", friendName);
                        intent.putExtra("friend_id", userFriends.get(position).get("UID").toString());
                        intent.setClass(friendlistActivity.this, chat_nav.class);
                        startActivity(intent);
                    }
                }else if(currentAdapter == 0){
                    if (position == 0) {
                        listview.setAdapter(friendlistAdapter);
                        currentAdapter = 1;
                    }
                }
            }

        });

    }

    private void getUserInfo(FirebaseUser user){
        userID = user.getUid();
        userEmail = user.getEmail();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        storeUserInfo(documentSnapshot);
                        storeFriendsInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error getting document.", e);
                    }
                });
    }

    private void initializeUserInfo(String userID, String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference users = db.collection("users");

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("UID", userID);
        newUser.put("email", userEmail);
        newUser.put("gender", "");
        newUser.put("birthday", "");
        newUser.put("display", (boolean)false);
        newUser.put("username", userEmail);
        newUser.put("password","");
        newUser.put("checkPoint",new ArrayList<String>());
        newUser.put("friends",new ArrayList<String>());
        newUser.put("notifications",new ArrayList<String>());

        users.document(userID).set(newUser);
    }

    //function used to store user's information
    protected void storeUserInfo(DocumentSnapshot data){

        if(data.getString("email") == null){
                initializeUserInfo(userID, userEmail);
        }
        if(data.getString("email") != null){
            userEmail = data.getString("email");
        }
        if(data.getString("gender") != null){
            userGender = data.getString("gender");
        }
        if(data.getString("username") != null){
            username = data.getString("username");
        }
        if(data.getString("birthday") != null){
            userBirthday = data.getString("birthday");
        }
        if(data.getBoolean("display") != null){
            userDisplay = data.getBoolean("display");
        }
        if((ArrayList<String>)data.get("friends") != null){
            userFriendsID = (ArrayList<String>)data.get("friends");
        }
        if((ArrayList<GeoPoint>) data.get("checkPoint") != null) {
            userCheckedPoints = (ArrayList<GeoPoint>) data.get("checkPoint");
        }
        if(data.get("notifications") != null){
            userNotifications = (ArrayList<String>)data.get("notifications");
        }
    }

    //function used to store all information of all user's friend;
    protected void storeFriendsInfo(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(userFriendsID != null) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> temp = new HashMap<>(document.getData());
                                    if (temp.get("UID") != null) {
                                        String UID = temp.get("UID").toString();

                                        for (int i = 0; i < userFriendsID.size(); i++) {
                                            if (UID != null && UID.equals(userFriendsID.get(i))) {
                                                userFriends.add(temp);
                                                break;
                                            }
                                        }

                                        for (int i = 0; i < userNotifications.size(); i++) {
                                            if (UID != null && UID.equals(userNotifications.get(i))) {
                                                notificationContent.add(0,
                                                        "user " + temp.get("username").toString() + " sent you a friend request!");
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            Log.w("tag", "Error getting document.", task.getException());
                        }

                        ArrayList<String> list = setListAdapter();
                        friendlistAdapter.addAll(list);

                        notificationContent.add(0,"<-Friends List");
                        notificationAdapter.addAll(notificationContent);
                        storeData(username, userID, userEmail, userGender, userBirthday, userDisplay, userFriends, userCheckedPoints);
                    }
                });
    }

    private ArrayList<String> setListAdapter(){
        ArrayList<String> list = new ArrayList<>();
        list.add("Notifications->");
        for(int i=0; i<userFriends.size(); i++){
            System.out.println(" "+i);
            String temp = new String(userFriends.get(i).get("username").toString());
            list.add(temp);
        }
        return list;
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
