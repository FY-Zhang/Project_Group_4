package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class channel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
    }

    public void toPost(View view){
        Intent intent = new Intent();
        intent.setClass(this, post.class);
        startActivity(intent);
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
    public void toSetting(View view) {
        Intent intent = new Intent();
        intent.setClass(this, setting_main.class);
        startActivity(intent);
    }
}
