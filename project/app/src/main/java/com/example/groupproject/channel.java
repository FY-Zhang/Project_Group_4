package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class channel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

    }

    public void toPost1(View view){
        Intent intent = new Intent();
        String id = "channel1";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost2(View view){
        Intent intent = new Intent();
        String id = "channel2";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost3(View view){
        Intent intent = new Intent();
        String id = "channel3";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost4(View view){
        Intent intent = new Intent();
        String id = "channel4";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost5(View view){
        Intent intent = new Intent();
        String id = "channel5";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost6(View view){
        Intent intent = new Intent();
        String id = "channel6";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost7(View view){
        Intent intent = new Intent();
        String id = "channel7";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost8(View view){
        Intent intent = new Intent();
        String id = "channel8";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost9(View view){
        Intent intent = new Intent();
        String id = "channel9";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost10(View view){
        Intent intent = new Intent();
        String id = "channel10";
        intent.putExtra("id", id);
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
