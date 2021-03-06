package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Layout;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.groupproject.appCookies.storeData;
import static com.example.groupproject.appCookies.userAvatar;
import static com.example.groupproject.appCookies.userChecked;
import static com.example.groupproject.appCookies.userFavorite;
import static com.example.groupproject.appCookies.userPhone;


public class friendlistActivity extends AppCompatActivity {

    Toolbar toolbar;
    private int currentAdapter = 1;
    private boolean isNewUser = false;

    private boolean mLocationPermissionGranted;
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private ListView listview;
    private ArrayList<String> list = new ArrayList<String>();

    private String username = new String();
    private String userID = new String();
    private String userEmail = new String();
    private String userGender = new String();
    private String userBirthday = new String();
    private String userPhone = new String();
    private boolean userDisplay = false;
    private String userAvatar = new String();
    private ArrayList<String> userFavorite = new ArrayList<String>();

    private ArrayList<String> userFriendsID = new ArrayList<>();
    private ArrayList<Map<String,Object>> userFriends = new ArrayList<>();
    private ArrayAdapter<String> friendlistAdapter;

    private ArrayList<GeoPoint> userCheckedPoints = new ArrayList<>();
    private ArrayList<Map<String,Object>> userChecked = new ArrayList<>();

    ListView notificationsList;
    private ArrayList<String> dbNotifications = new ArrayList<>();
    private ArrayAdapter<String> notificationAdapter;
    private ArrayList<ArrayList<String>> userNotifications = new ArrayList<>();
    private ArrayList<String> notificationContent = new ArrayList<>();

    ListView searchList;
    private ArrayAdapter<String> searchAdapter;
    private ArrayList<String> searchResult = new ArrayList<>();
    private TextInputEditText textInputEditText;
    private ArrayList<String> searchID = new ArrayList<>();

    ListView nearbyList;
    private ArrayAdapter<String> nearbyAdapter;
    private ArrayList<String> nearbyResult = new ArrayList<>();
    private ArrayList<String> nearbyID = new ArrayList<>();

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment search = new fragment_search();
    private Fragment friendList = new fragment_friendlist();
    private Fragment nearby = new fragment_search_nearby();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview = (ListView)findViewById(R.id.item);

        textInputEditText = findViewById(R.id.searchText);


        //storeData(username, userID, userEmail, userGender, userBirthday);
        if(friendlistAdapter != null)
            friendlistAdapter.clear();
        friendlistAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);

        if(notificationAdapter != null)
            notificationAdapter.clear();
        notificationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);

        searchList = findViewById(R.id.result);
        searchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);

        nearbyList = findViewById(R.id.nearby_result);
        nearbyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);

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
                        intent.putExtra("friend_id", userFriends.get(position-1).get("UID").toString());
                        intent.setClass(friendlistActivity.this, chat_nav.class);
                        startActivity(intent);
                    }
                }else if(currentAdapter == 0){
                    System.out.println("-------notify------");
                    if (position == 0) {
                        listview.setAdapter(friendlistAdapter);
                        currentAdapter = 1;
                    }else{
                        Intent intent = new Intent();
                        intent.putExtra("UID", userNotifications.get(position-1).get(2));
                        intent.putExtra("type", "request");
                        intent.setClass(friendlistActivity.this, profile.class);
                        startActivity(intent);
                    }
                }
            }

        });

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(friendlistActivity.this, profile.class);
                intent.putExtra("UID", searchID.get(position));
                intent.putExtra("type", "unknown");
                startActivity(intent);
            }
        });
        nearbyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(friendlistActivity.this, profile.class);
                intent.putExtra("UID", nearbyID.get(position));
                intent.putExtra("type", "unknown");
                startActivity(intent);
            }
        });

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        search = fragmentManager.findFragmentById(R.id.fragment_search);
        friendList = fragmentManager.findFragmentById(R.id.fragment_friendlist);
        nearby = fragmentManager.findFragmentById(R.id.fragment_nearby);
        fragmentTransaction.hide(search);
        fragmentTransaction.hide(nearby);
        fragmentTransaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        textInputEditText
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {

                    getSearchResult();
                    //execute method for searching
                }
                return false;
            }
        });

    }

    private void getSearchResult() {
        final String input = textInputEditText.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            searchResult.clear();
                            for(QueryDocumentSnapshot document: task.getResult()){
                                if(document.getData().get("email").toString().contains(input)){
                                    searchResult.add(document.getData().get("username").toString());
                                    searchID.add(document.getData().get("UID").toString());
                                }
                            }
                            if(searchResult.size() == 0)
                                Toast.makeText(friendlistActivity.this, "Sorry, no matched user :(", Toast.LENGTH_LONG).show();

                            searchAdapter.clear();
                            searchAdapter.addAll(searchResult);
                            textInputEditText.setText(null);
                        }else {
                            Toast.makeText(friendlistActivity.this, "Error doing search", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        searchList.setAdapter(searchAdapter);
    }

    private void getUserInfo(FirebaseUser user){
        userID = user.getUid();
        userEmail = user.getEmail();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("users").document(userID);

        final Intent intent = new Intent();
        intent.putExtra("new_user", "true");
        intent.setClass(friendlistActivity.this, initial_page.class);
        documentReference
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.getString("email") == null){
                            initializeUserInfo(userID, userEmail);
                            startActivity(intent);
                            finish();
                        }
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
        isNewUser = true;

        CollectionReference users = db.collection("users");

        ArrayList<String> tempFriends = new ArrayList<>();
        tempFriends.add("aInNPneOJTPS4blXjSPPUh8iRnl2");
        ArrayList<String> tempNotifications = new ArrayList<>();
        tempNotifications.add("2-0-aInNPneOJTPS4blXjSPPUh8iRnl2-Welcome to SupLink ;)");

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("UID", userID);
        newUser.put("email", userEmail);
        newUser.put("gender", "Female");
        newUser.put("birthday", "1900/01/01");
        newUser.put("display", false);
        newUser.put("username", userEmail);
        newUser.put("phone", "");
        newUser.put("checkPoint",new ArrayList<String>());
        newUser.put("friends", tempFriends);
        newUser.put("favorite", new ArrayList<String>());
        //newUser.put("curUserLoc", "none-none");
        newUser.put("notifications",tempNotifications);
        newUser.put("avatar", "https://firebasestorage.googleapis.com/v0/b/groupproject-ffdc4.appspot.com/o/user_avatar%2Favatar_default.jpg?alt=media&token=852659d8-aa5a-4022-a53f-abfb3c268aa6");

        users.document(userID).set(newUser);
        users.document(userID).collection("points");
    }

    //function used to store user's information
    protected void storeUserInfo(DocumentSnapshot data){

        if(data.getString("email") != null){
            userEmail = data.getString("email");
        } else { /* just in order to promise no null - check again*/
            userEmail = "default@suplink.com";
        }
        if(data.getString("gender") != null){
            userGender = data.getString("gender");
        } else {
            userGender = "Female";
        }
        if(data.getString("username") != null){
            username = data.getString("username");
        } else {
            username = "User";
        }
        if(data.getString("birthday") != null){
            userBirthday = data.getString("birthday");
        } else {
            userBirthday = "1/1/1900";
        }
        if(data.getBoolean("display") != null){
            userDisplay = data.getBoolean("display");
        } else {
            userDisplay = false;
        }
        if(data.get("friends") != null){
            userFriendsID = (ArrayList<String>)data.get("friends");
        }
        if(data.getString("phone") != null){
            userPhone = data.getString("phone");
        }
        if(data.get("checkPoint") != null) {
            userCheckedPoints = (ArrayList<GeoPoint>) data.get("checkPoint");
        }
        if(data.get("notifications") != null){
            dbNotifications = (ArrayList<String>)data.get("notifications");
            for(int i=0; i<dbNotifications.size(); i++){
                String[] temp =dbNotifications.get(i).split("-");
                ArrayList<String> temp2 = new ArrayList<>();
                temp2.add(temp[0]);
                temp2.add(temp[1]);
                temp2.add(temp[2]);
                userNotifications.add(temp2);
            }
            Collections.reverse(userNotifications);
        }
        if(data.getString("avatar") != null){
            userAvatar = data.getString("avatar");
        }
        if((ArrayList<String>)data.get("favorite") != null){
            userFavorite = (ArrayList<String>)data.get("favorite");
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
                                for (int i = 0; i < userFriendsID.size(); i++) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String UID = document.getData().get("UID").toString();
                                        if ( UID != null && UID.equals(userFriendsID.get(i))) {
                                            userFriends.add(document.getData());
                                            break;
                                        }
                                    }
                                }

                                for (int i = 0; i < userNotifications.size(); i++) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String UID = document.getData().get("UID").toString();
                                        if (UID != null && UID.equals(userNotifications.get(i).get(2))) {
                                            String type = userNotifications.get(i).get(0);
                                            if(type.equals("0")){

                                                notificationContent.add("user " + document.getData().get("username").toString() + " sent you a friend request!");
                                            }else if(type.equals("1")){

                                                notificationContent.add("Your friend " + document.getData().get("username").toString() + " sent you a chat Message!");
                                            } else if (type.equals("2")) {
                                                notificationContent.add("Welcome to SupLink :)");
                                            }
                                            break;
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

                        notificationContent.add(0,"Friends List\uD83D\uDC65");
                        notificationAdapter.addAll(notificationContent);
                        storeData(username, userID, userEmail, userGender, userBirthday,
                                userDisplay, userFriends, userCheckedPoints, userFriendsID,
                                userAvatar, userFavorite, userPhone);
                    }
                });
    }

    private ArrayList<String> setListAdapter(){
        ArrayList<String> list = new ArrayList<>();
        list.add("Notifications\uD83D\uDCAC");
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
                if(isOpen(this)){
                    intent.setClass(this, map_main.class);
                    startActivity(intent);
                    return true;
                } else {
                    System.out.println("Now the open state - ");
                    Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                    Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
                    return false;
                }
            case R.id.searchByEmail:
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(nearby);
                fragmentTransaction.show(search);
                fragmentTransaction.commit();
                return true;
            case R.id.searchByPhone:
                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                fragmentTransaction2.hide(search);
                fragmentTransaction2.show(nearby);
                fragmentTransaction2.commit();
                showNearbyResult();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showNearbyResult(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            nearbyResult.clear();
                            String[] userPhoneInfo = userPhone.split("-");
                            for(QueryDocumentSnapshot document: task.getResult()){
                                if(document.getData().get("phone") != null) {
                                    String[] othersPhoneInfo = document.getData().get("phone").toString().split("-");

                                    if (othersPhoneInfo.length >1 && userPhoneInfo.length >1 && othersPhoneInfo[1].equals(userPhoneInfo[1]) &&
                                            othersPhoneInfo[0].equals(userPhoneInfo[0])) {

                                        nearbyResult.add(document.getData().get("username").toString());
                                        nearbyID.add(document.getData().get("UID").toString());
                                    }
                                }
                            }
                            if(nearbyResult.size() == 0)
                                Toast.makeText(friendlistActivity.this, "Sorry, no matched user :(", Toast.LENGTH_LONG).show();

                            nearbyAdapter.clear();
                            nearbyAdapter.addAll(nearbyResult);
                        }else {
                            Toast.makeText(friendlistActivity.this, "Error doing search", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        nearbyList.setAdapter(nearbyAdapter);
    } //by phone num
    public void toFriendlist(View view){
    }
    public void toChannel(View view){
        Intent intent = new Intent();
        intent.setClass(this, channel.class);
        startActivity(intent);
        finish();
    }
    public void toSetting(View view){
        Intent intent = new Intent();
        intent.setClass(this, setting_main.class);
        startActivity(intent);
        finish();
    }

    private long mExitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0) {

            if(System.currentTimeMillis() - mExitTime > 2000) {
                mExitTime = System.currentTimeMillis();
                Toast.makeText(friendlistActivity.this, "Press again to exit", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showFriendlist(View view){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(search);
        fragmentTransaction.hide(nearby);
        fragmentTransaction.commit();
    }

    public boolean isOpen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        System.out.println("open fun is in");
        if(getLocationPermission()){//1st - get permission
            assert locationManager != null;
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        return false;
    } //check gps open

    private boolean getLocationPermission() {
        if (ContextCompat.checkSelfPermission(friendlistActivity.this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            //Toast.makeText(map_main.this, "Get location permission success.", Toast.LENGTH_SHORT).show();
            System.out.println("get success");
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Toast.makeText(this, "Get location permission fail.", Toast.LENGTH_SHORT).show();
            System.out.println("get fail");
            return false;
        }
    }
}
