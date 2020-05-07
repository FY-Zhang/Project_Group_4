package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

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
        String id = "Cl.1";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost2(View view){
        Intent intent = new Intent();
        String id = "Cl.2";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost3(View view){
        Intent intent = new Intent();
        String id = "Cl.3";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost4(View view){
        Intent intent = new Intent();
        String id = "Cl.4";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost5(View view){
        Intent intent = new Intent();
        String id = "Cl.5";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost6(View view){
        Intent intent = new Intent();
        String id = "Cl.6";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toPost7(View view){
        Intent intent = new Intent();
        String id = "Cl.7";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }

    public void toPost8(View view){
        Intent intent = new Intent();
        String id = "Cl.8";
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }

    public void toPostLocal(View view) {
        Intent intent = new Intent();
        String id = "local"; // actually not existed in fb.db
        intent.putExtra("id", id);
        intent.setClass(this, post.class);
        startActivity(intent);
    }

    public void toFriendlist(View view){
        Intent intent = new Intent();
        intent.setClass(this, friendlistActivity.class);
        startActivity(intent);
        finish();
    }
    public void toChannel(View view){
    }

    public void toSetting(View view) {
        Intent intent = new Intent();
        intent.setClass(this, setting_main.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            Intent intent_cnl_frd = new Intent(channel.this, friendlistActivity.class);
            startActivity(intent_cnl_frd);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
