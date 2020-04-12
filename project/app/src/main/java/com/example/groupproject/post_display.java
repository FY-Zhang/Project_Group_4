package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;
import java.util.Map;

public class post_display extends AppCompatActivity {

    public static String postId = new String();
    public String post_title = "11";
    public String post_author = "22";
    public String post_content = "33";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_display);

        Intent intent = getIntent();
        postId = intent.getStringExtra("post_id");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("post")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(postId.equals(document.get("postId"))) {
                                    post_title = document.get("title").toString();
                                    post_author = document.get("author").toString();
                                    post_content = document.get("content").toString();
                                    break;
                                }
                            }
                        }
                        TextView postTitle = findViewById(R.id.post_title);
                        TextView postAuthor = findViewById(R.id.post_author);
                        TextView postContent = findViewById(R.id.post_content);
                        postTitle.setText(post_title);
                        postAuthor.setText(post_author);
                        postContent.setText(post_content);
                    }
                });
        TextView postContent = (TextView)findViewById(R.id.post_content);
        postContent.setMovementMethod(ScrollingMovementMethod.getInstance());

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("like")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int flag = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(appCookies.userID.equals(document.get("userId")) && postId.equals(document.get("postId"))) {
                                    flag = 1;
                                    break;
                                }
                            }
                            if(flag == 1) {
                                Button button = (Button)findViewById(R.id.post_like);
                                button.setVisibility(View.GONE);
                            }
                            else {
                                Button button = (Button)findViewById(R.id.post_dislike);
                                button.setVisibility(View.GONE);
                            }

                        }
                    }
                });
    }

    public void toLike(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference messages = db.collection("like");
        Map<String, Object> message = new HashMap<>();
        message.put("userId", appCookies.userID);
        message.put("postId", postId);
        messages.document(appCookies.userID+postId).set(message);
        Intent intent = new Intent(post_display.this, channel.class);
        startActivity(intent);
    }
    public void toDislike(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("like").document(appCookies.userID+postId).delete();
        Intent intent = new Intent(post_display.this, channel.class);
        startActivity(intent);

    }
}
