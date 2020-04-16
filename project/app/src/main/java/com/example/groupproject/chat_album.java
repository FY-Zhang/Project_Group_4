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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.groupproject.appCookies.userID;

public class chat_album extends AppCompatActivity {

    private Uri imgUri;
    private static final int PICK_IMAGE_REQUEST = 852;
    private String friendName, friendID, databaseName;

    private ImageView imgView;
    private ProgressBar progressBar;


    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_album);

        Intent intent = getIntent();
        friendName = intent.getStringExtra("friend_name");
        friendID = intent.getStringExtra("friend_id");
        databaseName = intent.getStringExtra("database_name");


        imgView = findViewById(R.id.picture);
        progressBar = findViewById(R.id.progressBar);

        storageReference = FirebaseStorage.getInstance().getReference("chat_images");
        databaseReference = FirebaseDatabase.getInstance().getReference(databaseName);

        openFileChooser();
    }


    public void onDropClick(View view){
        Intent intent = new Intent();
        intent.putExtra("friend_name", friendName);
        intent.putExtra("friend_id", friendID);
        intent.setClass(this, chat_nav.class);
        startActivity(intent);
    }
    public void onSendClick(View view){
        uploadFile();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(friendID)
                .update("notifications", FieldValue.arrayUnion("1-0-"+userID));

        Intent intent = new Intent();
        intent.putExtra("friend_name", friendName);
        intent.putExtra("friend_id", friendID);
        intent.setClass(this, chat_nav.class);
        startActivity(intent);
    }
    public void onRepickClick(View view){
        openFileChooser();
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

            Picasso.with(this).load(imgUri).into((imgView));
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private  void uploadFile(){
        if(imgUri != null){
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imgUri));

            UploadTask uploadTask = fileReference.putFile(imgUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 50000);


                            Toast.makeText(chat_album.this, "Upload successfully", Toast.LENGTH_SHORT).show();

                            Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String photoStringLink = uri.toString();

                                    SimpleDateFormat sdf = new SimpleDateFormat();
                                    sdf.applyPattern("yyyy-MM-dd_HH:mm_ss_a");
                                    Date date = new Date();

                                    ChatMessage chatMessage = new ChatMessage(userID, sdf.format(date), 0, "",photoStringLink, "", 200, 200);
                                    String uploadID = databaseReference.push().getKey();
                                    databaseReference.child(uploadID).setValue(chatMessage);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(chat_album.this, "Error uploading files", Toast.LENGTH_LONG).show();;
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int)progress);
                        }
                    });

        }else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
}
