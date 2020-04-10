package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.groupproject.appCookies.userID;

public class chat_location extends AppCompatActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 159;

    private String friendName, friendID, databaseName;
    private DatabaseReference dbMessage;

    @SuppressWarnings("static-access")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_location);


        Intent intent = getIntent();
        friendName = intent.getStringExtra("friend_name");
        friendID = intent.getStringExtra("friend_id");
        databaseName = intent.getStringExtra("database_name");

        dbMessage = FirebaseDatabase.getInstance().getReference(databaseName);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new  String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude()+" "+currentLocation.getLongitude(), Toast.LENGTH_LONG).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_chat_location);
                    supportMapFragment.getMapAsync(chat_location.this);
                }
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I'm here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.addMarker(markerOptions);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
            default:
                break;
        }
    }

    public void onBackClick(View view){
        Intent intent = new Intent();
        intent.putExtra("friend_name", friendName);
        intent.putExtra("friend_id", friendID);
        intent.setClass(this, chat_nav.class);
        startActivity(intent);
    }
    public void onSendClick(View view){
        String message = ""+currentLocation.getLatitude()+" "+currentLocation.getLongitude();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd_HH:mm_ss_a");
        Date date = new Date();

        if(!TextUtils.isEmpty(message)){

            String id = dbMessage.push().getKey();

            ChatMessage messageInfo = new ChatMessage("", userID, sdf.format(date), 1, message);

            dbMessage.child(id).setValue(messageInfo);

        }else{
            Toast.makeText(this, "Message cant't be null", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent();
        intent.putExtra("friend_name", friendName);
        intent.putExtra("friend_id", friendID);
        intent.setClass(this, chat_nav.class);
        startActivity(intent);

    }
}