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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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

        final String[] form = new String[]{"title", "author"};
        final int[] to = new int[]{R.id.tv_title_fav, R.id.tv_auth_fav};
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

                        if (userFavPost.size() != 0) /*{
                            SimpleAdapter simpleAdapter_t = new SimpleAdapter(post_favorite.this, postList, R.layout.post_fav_item, form, to);
                            ListView listView_t = findViewById(R.id.lv_post_fav);
                            listView_t.setAdapter(simpleAdapter_t);
                        } else*/ {
                            System.out.println("The fav size: " + userFavPost.size());
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
                                                post.put("title", documentSnapshot.getString("title"));
                                                post.put("author", "by: " + documentSnapshot.getString("author"));
                                                System.out.println("The post item: " + post);
                                                postList.add(post);
                                                docId.add(documentSnapshot.getId());
                                                //System.out.println("1: " + postList);
                                                j++;
                                                System.out.println("J val: " + j);

                                                if (j == userFavPost.size()) { //after final step
                                                    System.out.println("final step ! ! !");
                                                    System.out.println("The postlist:" + postList);
                                                    final SimpleAdapter simpleAdapter_fav = new SimpleAdapter(post_favorite.this, postList, R.layout.post_fav_item, form, to);
                                                    final ListView listView_fav = findViewById(R.id.lv_post_fav);
                                                    listView_fav.setAdapter(simpleAdapter_fav);

                                                    listView_fav.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                        @Override
                                                        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                                            AlertDialog.Builder dialog = new AlertDialog.Builder(post_favorite.this);
                                                            dialog.setTitle("Delete");
                                                            //dialog.setMessage("Current i is: " + position);
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
                                                                            simpleAdapter_fav.notifyDataSetChanged();
                                                                            listView_fav.invalidate();
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

                                                    listView_fav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                            Intent intent_to_display = new Intent(post_favorite.this, post_display.class);
                                                            System.out.println("fav post id is: " + docId.get(position));
                                                            intent_to_display.putExtra("post_id", docId.get(position));
                                                            System.out.println("on click");
                                                            startActivity(intent_to_display);
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
