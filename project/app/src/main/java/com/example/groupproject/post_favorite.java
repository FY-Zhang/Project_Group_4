package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class post_favorite extends AppCompatActivity {

    private List<Map<String, Object>> postList = new ArrayList<>();
    public static ArrayList<String> userFavPost = new ArrayList<>();
    private int j = 0;

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
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userFavPost = (ArrayList<String>) documentSnapshot.get("favorite");
                        System.out.println("The list for:-- " + userFavPost);

                        if (userFavPost.size() == 0) {
                            SimpleAdapter simpleAdapter_t = new SimpleAdapter(post_favorite.this, postList, R.layout.post_fav_item, form, to);
                            ListView listView_t = findViewById(R.id.lv_post_fav);
                            listView_t.setAdapter(simpleAdapter_t);
                        } else {
                            for (int i = 0; i < userFavPost.size(); i++) {
                                String favPostID = userFavPost.get(i);
                                System.out.println("My fav: " + favPostID);
                                FirebaseFirestore ff = FirebaseFirestore.getInstance();//get post by id
                                ff.collection("post")
                                        .document(favPostID)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                Map<String, Object> post = new HashMap<>();

                                                post.put("title", documentSnapshot.getString("title") + " by: " + documentSnapshot.getString("author"));
                                                post.put("content", documentSnapshot.getString("content"));
                                                //Date date = documentSnapshot.getDate("datetime");
                                                post.put("dtc", documentSnapshot.getString("channel") + " / " + documentSnapshot.getDate("datetime"));
                                                System.out.println("The post item: " + post);
                                                if (post.get("content") != null) {
                                                    postList.add(post);
                                                    docId.add(documentSnapshot.getId());
                                                }
                                                //System.out.println("1: " + postList);
                                                j++; //System.out.println("J val: " + j);
                                                if (j == userFavPost.size()) { //after final step
                                                    final SimpleAdapter simpleAdapter = new SimpleAdapter(post_favorite.this, postList, R.layout.post_fav_item, form, to);
                                                    final ListView listView = findViewById(R.id.lv_post_fav);
                                                    listView.setAdapter(simpleAdapter);

                                                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                        @Override
                                                        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                                            AlertDialog.Builder dialog = new AlertDialog.Builder(post_favorite.this);
                                                            dialog.setTitle("Delete");
                                                            dialog.setMessage("Do you want to delete this favorite?");
                                                            dialog.setPositiveButton("Yes",
                                                                    new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            FirebaseFirestore ff = FirebaseFirestore.getInstance();
                                                                            ff.collection("users")
                                                                                    .document(appCookies.userID)
                                                                                    .update("favorite", FieldValue.arrayRemove(docId.get(position)));

                                                                            Toast.makeText(post_favorite.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                                                            postList.remove(position);
                                                                            simpleAdapter.notifyDataSetChanged();
                                                                            listView.invalidate();
                                                                        }
                                                                    });
                                                            dialog.setNegativeButton("No",
                                                                    new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                        }
                                                                    });
                                                            dialog.show();
                                                            return true;
                                                        }
                                                    });
                                                }
                                            }
                                        });
                            }
                    }
                    }
                });
    }
}
