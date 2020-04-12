package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class post extends AppCompatActivity {

    Toolbar toolbar;
    private ListView listview;
    public static final String POST_NAME = "com.example.groupproject.POST_NAME";
    private ArrayAdapter<String> adapter;
    public static ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<Map<String,Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);        //把这一块移到setOnIteClickListener里面。
        listview = (ListView)findViewById(R.id.item);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        getData();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                arrayList);
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

    public void getData() {
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
                                Map<String, Object> temp = new HashMap<>(document.getData());
                                if(id.equals(channel))
                                    list.add(temp);
                            }
                        }
                        arrayList = setListAdapter();
                        adapter.addAll(arrayList);
                    }
                });
    }

    private ArrayList<String> setListAdapter() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String temp = new String(list.get(i).get("title").toString());
            arrayList.add(temp);
        }
        return arrayList;
    }

    public void toBack(View view){
        Intent intent = new Intent();
        intent.setClass(this, channel.class);
        startActivity(intent);
    }
    public void toNewPost(View view){
        Intent intent = new Intent();
        String channel = getIntent().getStringExtra("id");
        intent.putExtra("id", channel);
        intent.setClass(this, new_post.class);
        startActivity(intent);
    }
}
