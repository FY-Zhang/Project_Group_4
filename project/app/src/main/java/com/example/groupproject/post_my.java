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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class post_my extends AppCompatActivity {

    private List<Map<String, Object>> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_my);

        final String[] from = new String[]{"title"};
        final int[] to = new int[]{R.id.tv_title_my};
        final List<String> docId = new ArrayList<>();// array for id of documents

        System.out.println("My id:" + appCookies.userID);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        System.out.println("To get coll");           //user own post
        db.collection("post")
                .whereEqualTo("authorId", appCookies.userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            //System.out.println("succ to in the l");
                            for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Map<String, Object> post= new HashMap<>();
                                post.put("title", document.getString("title"));
                                postList.add(post);
                                docId.add(document.getId());
                                System.out.println("Now we.. " + postList);
                            }

                            final SimpleAdapter simpleAdapter = new SimpleAdapter(post_my.this, postList, R.layout.post_my_item, from, to);
                            final ListView listView = findViewById(R.id.lv_post_my);
                            listView.setAdapter(simpleAdapter);
                            
                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(post_my.this);
                                    dialog.setTitle("Delete");
                                    dialog.setMessage("Do you want to delete this post?");
                                    dialog.setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    FirebaseFirestore ff = FirebaseFirestore.getInstance();
                                                    ff.collection("post")
                                                            .document(docId.get(position))
                                                            .delete();

                                                    Toast.makeText(post_my.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                                    postList.remove(position);
                                                    simpleAdapter.notifyDataSetChanged();
                                                    listView.invalidate();
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
                                    Intent intent_to_display = new Intent(post_my.this, post_display.class);
                                    System.out.println("The post id is: " + docId.get(position));
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
