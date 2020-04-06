package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class post extends AppCompatActivity {

    Toolbar toolbar;
    private ListView listview;
    public ArrayList<String> list = new ArrayList<String>();
    public static final String POST_NAME = "com.example.groupproject.POST_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

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

                String postName = (String)parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra(POST_NAME, postName);
                intent.setClass(post.this,post_display.class);
                startActivity(intent);
            }

        });
    }

    private ArrayList<String> getData()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("post")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String id = getIntent().getStringExtra("id");
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document: task.getResult()) {
                                String channel = document.get("channel").toString();
                                String title = document.get("title").toString();
                                String content = document.get("content").toString();
                                String author = document.get("author").toString();

                                if(id.equals(channel)) {
                                    list.add(title);
                                    list.add("12");
                                }
                            }
                        }
                    }
                });

//        if(id.equals("channel1"))
//            list.add("Post_1");
        list.add("AAAAAAAAAA");
        return list;
    }
    public void toBack(View view){
        Intent intent = new Intent();
        intent.setClass(this, channel.class);
        startActivity(intent);
    }
    public void toNewPost(View view){
        Intent intent = new Intent();
        intent.setClass(this, new_post.class);
        startActivity(intent);
    }
}
