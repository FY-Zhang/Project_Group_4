package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class friendlistActivity extends AppCompatActivity {

    Toolbar toolbar;
    private ListView lv;
    private ArrayList<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv = (ListView)findViewById(R.id.item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                getData());
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent();
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
    private ArrayList<String> getData()
    {
        list.add("friend_1");
        list.add("friend_2");
        list.add("friend_3");
        list.add("friend_4");
        list.add("friend_5");
        list.add("friend_1");
        list.add("friend_2");
        list.add("friend_3");
        list.add("friend_4");
        list.add("friend_5");
        list.add("friend_1");
        list.add("friend_2");
        list.add("friend_3");
        list.add("friend_4");
        list.add("friend_5");
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
