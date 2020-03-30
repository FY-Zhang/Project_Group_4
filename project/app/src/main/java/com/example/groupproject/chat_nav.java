package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class chat_nav extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_nav);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String friendName = intent.getStringExtra(friendlistActivity.FRIEND_NAME);

        toolbar.setTitle(friendName);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_chat,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.chat_camera:
                intent.setClass(chat_nav.this, chat_camera.class);
                startActivity(intent);
                return true;
            case R.id.chat_location:
                intent.setClass(chat_nav.this, chat_location.class);
                startActivity(intent);
                return true;
            case R.id.chat_friend_info:
                return true;
            case R.id.friend_map:
                intent.setClass(chat_nav.this, map.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
