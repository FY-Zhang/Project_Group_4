package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.groupproject.appCookies.userID;

public class new_post extends AppCompatActivity {

    ImageButton button ;
    ImageView imageView ;
    private Uri imgUri;
    private static final int PICK_IMAGE_REQUEST = 852;


    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        button = findViewById(R.id.openFile);
        imageView = findViewById(R.id.image);
        storageReference = FirebaseStorage.getInstance().getReference("post_image");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private  void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUri = data.getData();

            imageView.setAdjustViewBounds(true);
            Picasso.with(this).load(imgUri).into((imageView));
        }
    }

    public void toBack(View view){
        Intent intent = new Intent();
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    public void toSubmit(String uri){
        EditText editText1 = (EditText)findViewById(R.id.post_title);
        EditText editText2 = (EditText)findViewById(R.id.post_content);
        int like = 0;
        String title = editText1.getText().toString();
        String content = editText2.getText().toString();
        String author = appCookies.username;
        String authorId = appCookies.userID;
        String channel = getIntent().getStringExtra("id");
        Date date = new Date();
        String time = (9999-date.getTime()/100000000000.0)+"";

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference posts = db.collection("post");

        Map<String, Object> post = new HashMap<>();
        post.put("like", like);
        post.put("title", title);
        post.put("content", content);
        post.put("author", author);
        post.put("authorId", authorId);
        post.put("postId", time);
        post.put("channel", channel);
        post.put("datetime", date);
        post.put("image", uri);
        posts.document(time).set(post);

        finish();
        Intent intent = new Intent();
        intent.putExtra("id", channel);
        intent.setClass(this, post.class);
        startActivity(intent);
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    public void toSubmit(View view){
        if(imgUri != null){
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imgUri));

            UploadTask uploadTask = fileReference.putFile(imgUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String photoStringLink = uri.toString();
                            toSubmit(photoStringLink);
                        }
                    });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }else {
            toSubmit("");
        }
    }
}
