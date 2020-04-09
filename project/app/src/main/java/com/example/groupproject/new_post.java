package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class new_post extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
    }

    public void toBack(View view){
        Intent intent = new Intent();
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toSubmit(View view){
        EditText editText1 = (EditText)findViewById(R.id.post_title);
        EditText editText2 = (EditText)findViewById(R.id.post_content);
        String title = editText1.getText().toString();
        String content = editText2.getText().toString();
        String author = appCookies.username;
        String channel = getIntent().getStringExtra("id");
        Date date = new Date();
        String time = (9999-date.getTime()/100000000000.0)+"";

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference posts = db.collection("post");

        Map<String, Object> post = new HashMap<>();
        post.put("title", title);
        post.put("content", content);
        post.put("author", author);
        post.put("channel", channel);
        post.put("datetime", date);
        posts.document(time).set(post);

        Intent intent = new Intent();
        intent.setClass(this, post.class);
        startActivity(intent);
    }
}
