package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
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

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String cur_lat = "none";
    private String cur_lng = "none";
    private ImageButton mapButton;
    
    ImageButton imgButton;
    ImageView imageView ;
    private Uri imgUri;
    private static final int PICK_IMAGE_REQUEST = 852;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        System.out.println("now create fuse: " + mFusedLocationProviderClient);
        setContentView(R.layout.activity_new_post);

        imgButton = findViewById(R.id.openFile);
        mapButton = findViewById(R.id.openMap);
        imageView = findViewById(R.id.image);
        mapButton.setAlpha((float) 0.3);
        storageReference = FirebaseStorage.getInstance().getReference("post_image");

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGPSLocator();
                //v.setAlpha(1);
            }
        });
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openGPSLocator() {
        View view = findViewById(R.id.openMap);
        if(isOpen(view.getContext())) {     // get service
            if(getLocationPermission()) {   // get permission
                Intent intentToMap = new Intent(new_post.this, map_main.class);
                intentToMap.putExtra("sitePost", "src");
                startActivity(intentToMap);
                Toast.makeText(new_post.this, "Access location successfully", Toast.LENGTH_SHORT).show();
                getDeviceLocation();
                System.out.println("current: +++ " + cur_lat + " " + cur_lng);
                mapButton.setAlpha((float) 1.0);
            }
        } else {
            System.out.println("Please turn on GPS.");
            //Toast.makeText(new_post.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            //Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            //startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
        }

        getDeviceLocation();
        System.out.println("current: +++ " + cur_lat + " " + cur_lng + "---");
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
        post.put("latitude", cur_lat);
        post.put("longitude", cur_lng);
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

    /* about map fun*/
    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        System.out.println("Now the get device: " + mFusedLocationProviderClient);

        View view = findViewById(R.id.openMap);
        if(isOpen(view.getContext())) {
            try {
                if (mLocationPermissionGranted) {
                    //Toast.makeText(this, "location permission granted", Toast.LENGTH_LONG).show();
                    Task location = mFusedLocationProviderClient.getLastLocation();
                    System.out.println("The task loc: " + location);
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Location currentLocation = (Location) task.getResult();
                                if(currentLocation != null){
                                    cur_lat = String.valueOf(currentLocation.getLatitude());
                                    cur_lng = String.valueOf(currentLocation.getLongitude());
                                    System.out.println("latlng: " + cur_lat + " --" + cur_lng);
                                }else
                                    Toast.makeText(new_post.this, "Please try later", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(new_post.this, "Unable to get the current location.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            } catch (SecurityException e) {
                Log.e("Location", "Error getting device location: " + e.getMessage());
            }
        }
        else{
            Toast.makeText(new_post.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
        }
    }

    //permission
    private boolean getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            //Toast.makeText(map_main.this, "Get location permission success.", Toast.LENGTH_SHORT).show();
            System.out.println("get success");
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Toast.makeText(new_post.this, "Get location permission fail.", Toast.LENGTH_SHORT).show();
            System.out.println("get fail");
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Toast.makeText(map_main.this, "in the on request location.", Toast.LENGTH_SHORT).show();
        System.out.println("in the request location");
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    public static boolean isOpen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    } //check gps open


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
