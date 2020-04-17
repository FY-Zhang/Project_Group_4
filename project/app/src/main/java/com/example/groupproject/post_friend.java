package com.example.groupproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class post_friend extends AppCompatActivity {

    private List<Map<String, Object>> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_friend);

        Intent intent = getIntent();
        String friendId;
        if (intent != null) {
            friendId = intent.getStringExtra("friendId");
            System.out.println(friendId);
        } else {
            friendId = "FKuYZ5lRhYb0bGTT4bVtefiVoSs2";
        }

        final String[] form = new String[]{"title"};
        final int[] to = new int[]{R.id.tv_title_frd};
        final List<String> docId = new ArrayList<>();// array for id of documents

        System.out.println("-------------------------------------Friend post");

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("post")
                .whereEqualTo("authorId", friendId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Map<String, Object> post= new HashMap<>();
                                post.put("title", document.getString("title"));
                                postList.add(post);
                                docId.add(document.getId());
                                System.out.println("Post: " + post);
                            }

                            System.out.println("postlist: " + postList);

                            SimpleAdapter simpleAdapter = new SimpleAdapter(post_friend.this, postList, R.layout.post_friend_item, form, to);
                            final ListView listView = findViewById(R.id.lv_post_friend);
                            listView.setAdapter(simpleAdapter);

                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                    System.out.println("--------The index: " + position);
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(post_friend.this);
                                    dialog.setTitle("Favorites");
                                    dialog.setMessage("Do you want to favorite this post?");
                                    dialog.setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    FirebaseFirestore ff = FirebaseFirestore.getInstance();
                                                    ff.collection("users")
                                                            .document(appCookies.userID)//store into own
                                                            .update("favorite", FieldValue.arrayUnion(docId.get(position)));//store post id
                                                    Toast.makeText(post_friend.this, "Favorites successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    dialog.setNegativeButton("No",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) { }
                                            });
                                    dialog.show();
                                    return true;
                                }
                            });

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent_to_display = new Intent(post_friend.this, post_display.class);
                                    System.out.println("Frd post id is: " + docId.get(position));
                                    intent_to_display.putExtra("post_id", docId.get(position));
                                    startActivity(intent_to_display);
                                }
                            });

                        }
                    }
                });
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
