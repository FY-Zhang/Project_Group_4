package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class post_my extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_my);

        List<Map<String, Object>> listData = post_my_data();
        String[] form = new String[]{"title","content","dtc"};
        int[] to = new int[]{R.id.tv_title_my,R.id.tv_content_my,R.id.tv_dtc_my};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listData, R.layout.activity_post_my, form, to);

        ListView listView = findViewById(R.id.lv_post_my);
        listView.setAdapter(simpleAdapter);

        //setContentView(R.layout.activity_post_my);
    }

    private List<Map<String,Object>> post_my_data(){
        final List<Map<String, Object>> postList= new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("post").whereEqualTo("UID", appCookies.userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                final Map<String, Object> post= new HashMap<>();
                                post.put("title", document.getString("title"));
                                post.put("content", document.getString("content"));
                                post.put("dtc", document.getString("channel") + " / " + document.getDate("datetime"));
                                postList.add(post);
                            }
                        }
                    }
                });

        return postList;
    }

}
