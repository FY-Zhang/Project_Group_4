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

import java.util.ArrayList;

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
            case R.id.map_check:
                intent.setClass(this, map_main.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private ArrayList<String> getData()
    {
        for(int index = 0; index < appCookies.userFriends.size(); index++) {
            list.add(appCookies.userFriends.get(index));
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
