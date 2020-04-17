package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.groupproject.appCookies.userID;

public class chat_camera extends AppCompatActivity {

    private ImageView imgView;
    private static final int REQUEST_IMAGE_CAPTURE = 789;
    private String friendName, friendID, databaseName;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Bitmap imgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_camera);

        Intent intent = getIntent();
        friendName = intent.getStringExtra("friend_name");
        friendID = intent.getStringExtra("friend_id");
        databaseName = intent.getStringExtra("database_name");

        imgView = findViewById(R.id.picture);

        Intent cameraUse = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        storageReference = FirebaseStorage.getInstance().getReference("chat_images");
        databaseReference = FirebaseDatabase.getInstance().getReference(databaseName);

        if(cameraUse.resolveActivity(getPackageManager()) != null){
            startActivityForResult(cameraUse, REQUEST_IMAGE_CAPTURE);
        }else {
            Toast.makeText(this, "Error opening camera", Toast.LENGTH_LONG);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE) {

            Bundle extras = data.getExtras();
            imgBitmap = (Bitmap) extras.get("data");
            imgView.setImageBitmap(imgBitmap);
        }

    }

    public void onDropClick(View view){
        finish();
    }
    public void onSendClick(View view){

        StorageReference fileReference = storageReference.child(System.currentTimeMillis()+".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        UploadTask uploadTask = fileReference.putBytes(bytes);
        uploadTask
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(chat_camera.this, "Upload successfully", Toast.LENGTH_SHORT).show();

                        Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String photoStringLink = uri.toString();

                                SimpleDateFormat sdf = new SimpleDateFormat();
                                sdf.applyPattern("yyyy-MM-dd_HH:mm");
                                Date date = new Date();

                                ChatMessage chatMessage = new ChatMessage(userID, sdf.format(date), 0, "",photoStringLink, "", 200, 200);
                                String uploadID = databaseReference.push().getKey();
                                databaseReference.child(uploadID).setValue(chatMessage);
                            }
                        });
                    }
                });

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd_HH:mm_ss_a");
        Date date = new Date();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(friendID)
                .update("notifications", FieldValue.arrayUnion("1-0-"+userID +"-"+sdf.format(date)));

        finish();
    }

    public void onRetakeClick(View view){
        Intent cameraUse = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(cameraUse.resolveActivity(getPackageManager()) != null){
            startActivityForResult(cameraUse, REQUEST_IMAGE_CAPTURE);
        }else {
            Toast.makeText(this, "Error opening camera", Toast.LENGTH_LONG).show();
        }
    }
}
