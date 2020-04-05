package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.groupproject.functionsAndParameters.userFriends;
import static com.example.groupproject.functionsAndParameters.username;

public class friendlistActivity extends AppCompatActivity {

    Toolbar toolbar;
    private ListView listview;
    private ArrayList<String> list = new ArrayList<String>();
    public static final String FRIEND_NAME = "com.example.groupproject.FRIEND_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview = (ListView)findViewById(R.id.item);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                getData());
        listview.setAdapter(adapter);

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_friendlist,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.chat_camera:
                intent.setClass(this, chat_camera.class);
                startActivity(intent);
                return true;
            case R.id.chat_location:
                intent.setClass(this, chat_location.class);
                startActivity(intent);
                return true;
            case R.id.chat_friend_info:
                return true;
            case R.id.friend_map:
                intent.setClass(this, map.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private ArrayList<String> getData()
    {
        //list.add(functionsAndParameters.userID);

        for(int index = 0; index < functionsAndParameters.userFriends.size(); index++) {
            list.add(functionsAndParameters.userFriends.get(index));
        }

        return list;

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
