package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.groupproject.appCookies.userID;

public class chat_location extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = chat_location.class.getSimpleName();
    private static final int REQUEST_CODE = 159;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 337;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean locationPermissionGranted;
    private GoogleMap mMap;
    private double latitude, longitude;
    FusedLocationProviderClient fusedLocationProviderClient;

    private String friendName, friendID, databaseName;
    private DatabaseReference dbMessage;
    private String locationTitle = new String("CURRENT LOCATION");

    private  EditText searchText;

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

        searchText = (EditText)findViewById(R.id.input);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermissions();

    }

    private void getCurrentLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        View view = findViewById(R.id.fragment_chat_location);
        if(gpsTurnedOn(view.getContext())) {
            try {
                if (locationPermissionGranted) {
                    Toast.makeText(this, "location permission granted", Toast.LENGTH_LONG).show();
                    Task location = fusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Location currentLocation = (Location) task.getResult();

                                latitude = currentLocation.getLatitude();
                                longitude = currentLocation.getLongitude();

                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM,
                                        "My Location");
                            } else {
                                Toast.makeText(chat_location.this, "Unable to get the current location.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            } catch (SecurityException e) {
                Log.e("Location", "Error getting device location: " + e.getMessage());
            }
        }
        else{
            Toast.makeText(chat_location.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
        }

    }

    public static boolean gpsTurnedOn(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
    }

    private void initMap(){
        SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_chat_location);

        supportMapFragment.getMapAsync(chat_location.this);
    }
    private void init(){
        Log.d("Location", "init: initializing");

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                    searchText.setText("");
                    //execute method for searching
                }
                return false;
            }
        });
    }

    private void geoLocate() {
        Log.d("Location","geoLocation: geolocating");

        String searchString = searchText.getText().toString();

        Geocoder geocoder = new Geocoder(chat_location.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e("Location", e.getMessage());
        }
        if(list.size() > 0){
            Address address = list.get(0);

            latitude = address.getLatitude();
            longitude = address.getLongitude();

            Log.d("Location", "geoLocate: found a location"+address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_LONG).show();

            locationTitle = address.getAddressLine(0);
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }

    private void getLocationPermissions() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(this, "reach function getLocationPermission", Toast.LENGTH_LONG).show();
                locationPermissionGranted = true;
                initMap();
            }else {
                ActivityCompat.requestPermissions(this,
                        permission,
                        REQUEST_CODE);
            }
        }else {
            ActivityCompat.requestPermissions(this,
                    permission,
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    locationPermissionGranted = true;
                    initMap();
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(locationPermissionGranted){
            getCurrentLocation();

            mMap.setMyLocationEnabled(true);
        }
        init();
    }


    public void onBackClick(View view){
        Intent intent = new Intent();
        intent.putExtra("friend_name", friendName);
        intent.putExtra("friend_id", friendID);
        intent.setClass(this, chat_nav.class);
        startActivity(intent);
    }
    public void onSendClick(View view){
        String message = ""+latitude+" "+longitude;
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd_HH:mm_ss_a");
        Date date = new Date();

        if(!TextUtils.isEmpty(message)){

            String id = dbMessage.push().getKey();

            ChatMessage chatMessage = new ChatMessage(userID, sdf.format(date), 2, "","", locationTitle, latitude, longitude);

            dbMessage.child(id).setValue(chatMessage);

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