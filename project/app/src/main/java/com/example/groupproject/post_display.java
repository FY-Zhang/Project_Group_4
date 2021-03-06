package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.groupproject.appCookies.userAvatar;

public class post_display extends AppCompatActivity {

    public static String postId = new String();
    public int post_like;
    public Date post_date;
    public String post_title;
    public String post_author;
    public String post_content;
    public String post_channel;
    private String imageUri;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_display);

        Intent intent = getIntent();
        postId = intent.getStringExtra("post_id");

        imageView = findViewById(R.id.image);

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
                                    imageUri = document.get("image").toString();
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
                        TextView like_display = findViewById(R.id.like_display);
                        like_display.setText("Like ("+post_like+")");

                        if (imageUri!= "") {

                            imageView.setAdjustViewBounds(true);
                            Picasso.with(post_display.this)
                                    .load(imageUri)
                                    .into(imageView);
                        }
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
                                ImageButton button = (ImageButton)findViewById(R.id.post_like);
                                button.setVisibility(View.GONE);
                            }
                            else {
                                ImageButton button = (ImageButton)findViewById(R.id.post_dislike);
                                button.setVisibility(View.GONE);
                            }

                        }
                    }
                });

        Picasso.with(this)
                .load(imageUri)
                .into(imageView);
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

        finish();
        Intent intent = new Intent();
        intent.putExtra("post_id", postId);
        intent.setClass(this, post_display.class);
        startActivity(intent);
        finish();
    }
    public void toDislike(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("like").document(appCookies.userID+postId).delete();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("post").document(postId).update("like", post_like - 1);

        finish();
        Intent intent = new Intent();
        intent.putExtra("post_id", postId);
        intent.setClass(this, post_display.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            /*if(Objects.equals(getIntent().getStringExtra("action"), "did"))*/ {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
