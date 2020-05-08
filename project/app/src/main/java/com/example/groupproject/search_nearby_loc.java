package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.YuvImage;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class search_nearby_loc extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private String cur_lat = "null";
    private String cur_lng = "null";

    Button mapButton;
    ProgressBar searchPrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //只有当用户在本页面时, firebase中user的curLoc值才不为 "null-null"
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_nearby_loc);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        searchPrg = findViewById(R.id.nearby_prg);
        searchPrg.setVisibility(View.INVISIBLE);
        mapButton = findViewById(R.id.nearby_btn);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapButton.setBackgroundColor(getResources().getColor(R.color.fui_transparent));//set tra, display the prg
                searchPrg.setVisibility(View.VISIBLE);
                openGPSLocator();
                v.setAlpha(1);
                System.out.println("Curren your loc is: " + cur_lat + cur_lng);
               // searchNearbyUsers();
            }
        });
    }

    @Override
    protected void onDestroy() {
        updateFBLoc();
        finish();
        System.out.println("finish des");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        updateFBLoc();
        System.out.println("stop");
        super.onStop();
    }

    public void updateFBLoc() {
        appCookies.curUserLoc = "null_null";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(appCookies.userID).update("curUserLoc", appCookies.curUserLoc); //auto-update
        System.out.println("update fb db loc");
    }

    //location gps

    private void openGPSLocator() {
        View view = findViewById(R.id.nearby_btn);
        if(isOpen(view.getContext())) {     // get service
            if(getLocationPermission()) {   // get permission
                getDeviceLocation();
                System.out.println("current: +++ " + cur_lat + " " + cur_lng);
            }
        } else {
            System.out.println("Please turn on GPS.");
            Toast.makeText(search_nearby_loc.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
        }

        System.out.println("current: +++ " + cur_lat + " " + cur_lng + "---");
    }

    /* about map fun*/
    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        System.out.println("Now the get device: " + mFusedLocationProviderClient);

        View view = findViewById(R.id.nearby_btn);
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
                                if(currentLocation != null) {
                                    cur_lat = String.valueOf(currentLocation.getLatitude());
                                    cur_lng = String.valueOf(currentLocation.getLongitude());
                                    System.out.println("latlng: " + cur_lat + " --" + cur_lng);

                                    searchNearbyUsers();
                                } else
                                    Toast.makeText(search_nearby_loc.this, "Please try again", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(search_nearby_loc.this, "Unable to get the current location", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (SecurityException e) {
                Log.e("Location", "Error getting device location: " + e.getMessage());
            }
        }
        else{
            Toast.makeText(search_nearby_loc.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(search_nearby_loc.this, "Get location permission fail.", Toast.LENGTH_SHORT).show();
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

    public void searchNearbyUsers() {
        appCookies.curUserLoc = cur_lat + "_" + cur_lng; //once ues the fun, use the loc
        System.out.println(appCookies.curUserLoc);

        final String[] from = new String[]{"nearbyName", "nearbyDistance"};
        final int[] to = new int[]{R.id.tv_nearby_name, R.id.tv_nearby_distance};
        final List<String> nearbyId = new ArrayList<>();// array for id of documents


        System.out.println("My id:" + appCookies.userID);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        System.out.println("To get coll");           //user own post
        db.collection("users").document(appCookies.userID).update("curUserLoc", appCookies.curUserLoc); //动态更新用户的地理位置
        db.collection("users")
                .whereEqualTo("UID", appCookies.userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            //System.out.println("succ to in the l");
                            for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                String curLoc = document.getString("curUserLoc"); //获取本设备用户的位置信息
                                System.out.println("My Location:" + document.getString("curUserLoc"));
                                String[] curlatLng = curLoc.split("_", 2);
                                double curLat = Double.parseDouble(curlatLng[0]);
                                double curLng = Double.parseDouble(curlatLng[1]);
                                final LatLng cur = new LatLng(curLat, curLng);

                                final List<Map<String, Object>> nearList = new ArrayList<>();
                                FirebaseFirestore fb = FirebaseFirestore.getInstance();
                                fb.collection("users")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                if(task1.isSuccessful()) {
                                                    for(QueryDocumentSnapshot doc : Objects.requireNonNull(task1.getResult())) {
                                                        Map<String, Object> nearUsers = new HashMap<>();
                                                        double nearDis = 100000; //default distance

                                                        String nearLoc = doc.getString("curUserLoc"); //get other's loc and transfer to double
                                                        if(nearLoc != null) {//ensure this field existed in doc
                                                            String[] nearLatLng = nearLoc.split("_", 2);
                                                            if(nearLatLng[0] != null && nearLatLng[1] != null) {  //promise not null, ie online
                                                                if(!nearLatLng[0].equals("null") || !nearLatLng[1].equals("null")) {
                                                                    double nearLat = Double.parseDouble(nearLatLng[0]);
                                                                    double nearLng = Double.parseDouble(nearLatLng[1]);

                                                                    LatLng near = new LatLng(nearLat, nearLng);
                                                                    nearDis = SphericalUtil.computeDistanceBetween(near, cur);
                                                                    System.out.println("the dis: " + nearDis);

                                                                    String nearName = doc.getString("username");
                                                                    if(nearDis <= 5000 && !doc.getId().equals(appCookies.userID)) { //nice dis, put it into list
                                                                        nearUsers.put("nearbyName", nearName);
                                                                        nearUsers.put("nearbyDistance", (int) nearDis + "m");
                                                                        nearList.add(nearUsers);//store this user

                                                                        nearbyId.add(doc.getId());//store id, return intent store
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    Map<String, Object> nearUsers = new HashMap<>();
                                                    if(nearbyId.size() == 0) {
                                                        AlertDialog.Builder dialog = new AlertDialog.Builder(search_nearby_loc.this);
                                                        dialog.setTitle("No nearby users");
                                                        dialog.setMessage("Please try again later :)");
                                                        dialog.setPositiveButton("OK",null);
                                                        dialog.show();
                                                    }
                                                    final SimpleAdapter simpleAdapter = new SimpleAdapter(search_nearby_loc.this, nearList, R.layout.item_prg_nearby, from, to);
                                                    final ListView listView = findViewById(R.id.lv_nearby);
                                                    listView.setAdapter(simpleAdapter);

                                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                            Intent intent_to_profile = new Intent(search_nearby_loc.this, profile.class);
                                                            intent_to_profile.putExtra("UID", nearbyId.get(position));
                                                            intent_to_profile.putExtra("type", "unknown");
                                                                startActivity(intent_to_profile);
                                                            System.out.println("click " + position);
                                                        }
                                                    });

                                                    searchPrg.setVisibility(View.INVISIBLE);
                                                    mapButton.setBackgroundColor(getResources().getColor(R.color.fui_bgFacebook));
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
            //if(Objects.equals(getIntent().getStringExtra("action"), "did"))
            {
                updateFBLoc();
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
