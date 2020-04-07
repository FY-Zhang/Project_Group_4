package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class completeLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_login);
    }
    public void toFriendlist(View view)
    {
        Intent intent = new Intent();
        intent.setClass(this, friendlistActivity.class);
        startActivity(intent);
    }

}
