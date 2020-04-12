package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class post_favorite extends AppCompatActivity {

    private List<Map<String, Object>> postList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_favorite);

        final String[] form = new String[]{"title","content","dtc"};
        final int[] to = new int[]{R.id.tv_title_fav,R.id.tv_content_fav,R.id.tv_dtc_fav};
        final List<String> docId = new ArrayList<>();// array for id of documents

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(appCookies.userID)
                .get();//get all favorite posts
                 /**  do not change - IMPORTANT **/
                 //appcookie & friendlist & fb_array
/*                                  ---------      @@ do not change - IMPORTANT
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> post= new HashMap<>();

                                post.put("title", document.getString("title"));
                                post.put("content", document.getString("content"));
                                Date date = document.getDate("datetime");
                                post.put("dtc", document.getString("channel") + " / " + date);
                                postList.add(post);
                                docId.add(document.getId());
                                //System.out.println("Now we.. " + postList);
                            }

                            SimpleAdapter simpleAdapter = new SimpleAdapter(post_favorite.this, postList, R.layout.post_my_item, form, to);
                            final ListView listView = findViewById(R.id.lv_post_fav);
                            listView.setAdapter(simpleAdapter);

                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(post_favorite.this);
                                    dialog.setTitle("Delete");
                                    dialog.setMessage("Do you want to unfavorite this post?");
                                    dialog.setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    FirebaseFirestore ff = FirebaseFirestore.getInstance();
                                                    ff.collection("users")
                                                            .document(appCookies.userID)//store into own
                                                            .update("favorite", FieldValue.arrayRemove(docId.get(position)));
                                                    Toast.makeText(post_favorite.this, "Unfavorite successfully!", Toast.LENGTH_SHORT).show();
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
                        }
                    }
                });*/
    }
}
