package com.example.groupproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

public class chat_camera extends AppCompatActivity {

    private ImageView imgView;
    private static final int REQUEST_IMAGE_CAPTURE = 789;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_camera);

        imgView = findViewById(R.id.picture);

        Intent cameraUse = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(cameraUse.resolveActivity(getPackageManager()) != null){
            startActivityForResult(cameraUse, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE ){

            Bundle extras = data.getExtras();
            Bitmap imgBitmap = (Bitmap)extras.get("data");
            imgView.setImageBitmap(imgBitmap);
        }
    }
}
