package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class post_display extends AppCompatActivity {

    public static String postId = new String();
    public int post_like;
    public Date post_date;
    public String post_title;
    public String post_author;
    public String post_content;
    public String post_channel;

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
                                    post_like = Integer.parseInt(document.get("like").toString());
                                    post_date = document.getDate("datetime");
                                    post_title = document.get("title").toString();
                                    post_author = document.get("author").toString() + "\n" + post_date;
                                    post_content = document.get("content").toString();
                                    post_channel = document.get("channel").toString();
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
                        Button postLike = (Button)findViewById(R.id.post_like);
                        Button postDislike = (Button)findViewById(R.id.post_dislike);
                        postLike.setText("Like ("+post_like+")");
                        postDislike.setText("Dislike ("+post_like+")");
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

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("post").document(postId).update("like", post_like + 1);

        Intent intent = new Intent();
        intent.putExtra("post_id", postId);
        intent.setClass(this, post_display.class);
        startActivity(intent);
    }
    public void toDislike(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("like").document(appCookies.userID+postId).delete();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("post").document(postId).update("like", post_like - 1);

        Intent intent = new Intent();
        intent.putExtra("post_id", postId);
        intent.setClass(this, post_display.class);
        startActivity(intent);
    }
}
