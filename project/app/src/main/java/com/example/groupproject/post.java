package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class post extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private double cur_lat = 180;
    private double cur_lng = 180;
    private LatLng cur_latLng = new LatLng(cur_lat, cur_lng);


    Toolbar toolbar;
    private ListView listview;
    public static final String POST_NAME = "com.example.groupproject.POST_NAME";
    private ArrayAdapter<String> adapter;
    public static ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<Map<String,Object>> list = new ArrayList<>();
   private List<String> docId = new ArrayList<>();// array for id of documents

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listview = (ListView)findViewById(R.id.item);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);

        if(getIntent().getStringExtra("id").equals("local")) { //local post request
            if(isOpen(findViewById(R.id.newPost).getContext())) {
                if(getLocationPermission()) {
                    System.out.println("Now the lat ln is: " + cur_lat + " , " + cur_lng);
                    cur_latLng = new LatLng(cur_lat, cur_lng);
                }
            }
            getDeviceLocation();
            System.out.println("Now the device is: " + cur_lat + " , " + cur_lng);
        } else {
            getData();
        }

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position,
                                    long id) {
                Intent intent = new Intent();
                intent.putExtra("post_id", list.get(position).get("postId").toString());
                intent.setClass(post.this, post_display.class);
                startActivity(intent);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                System.out.println("--------The index: " + position);
                AlertDialog.Builder dialog = new AlertDialog.Builder(post.this);
                dialog.setTitle("Favorites");
                dialog.setMessage("Do you want to favorite this post?");
                dialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseFirestore ff = FirebaseFirestore.getInstance();
                                ff.collection("users")
                                        .document(appCookies.userID)//store into own
                                        .update("favorite", FieldValue.arrayUnion(docId.get(position)));//store post id
                                Toast.makeText(post.this, "Favorites successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { }
                        });
                dialog.show();
                return true;
            }
        });



    }

    public void getData() {
        final String id = getIntent().getStringExtra("id");
        if(id.equals("local")) {
            ImageButton button = (ImageButton)findViewById(R.id.newPost);
            button.setVisibility(View.GONE);
        }
        System.out.println("ID: ------- " + id);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("post")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document: task.getResult()) {
                                String channel = document.get("channel").toString();
                                String title = document.get("title").toString();
                                Map<String, Object> temp = new HashMap<>(document.getData());
                                if(id.equals(channel)){ // !!! higher priority to normal channel
                                    list.add(temp);
                                    docId.add(document.getId());
                                } else if(id.equals("local") && document.get("latitude") != null) { // local post
                                    System.out.println("the post loc: " + document.get("latitude") + document.get("longitude"));

                                    String post_lat = document.get("latitude").toString();
                                    String post_lng = document.get("longitude").toString();
                                    if(!post_lat.equals("none") && !post_lng.equals("none")) {
                                        double p_lat = Double.parseDouble(post_lat);
                                        double p_lng = Double.parseDouble(post_lng);
                                        LatLng cur_loc = new LatLng(cur_lat, cur_lng);
                                        if(isInBounds(p_lat, p_lng, cur_loc)) {
                                            System.out.println("cur_loc: " + cur_loc);
                                            list.add(temp);
                                            docId.add(document.getId());
                                        }
                                    }
                                }
                            }
                        }
                        arrayList = setListAdapter();
                        adapter.addAll(arrayList);
                        System.out.println("Doc is: " + docId);

                    }
                });
    }

    private ArrayList<String> setListAdapter() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String temp = new String(list.get(i).get("title").toString());
            arrayList.add(temp);
        }
        return arrayList;
    }

    public void toBack(View view){
        Intent intent = new Intent();
        intent.setClass(this, channel.class);
        startActivity(intent);
    }
    public void toNewPost(View view){
        finish();
        Intent intent = new Intent();
        String channel = getIntent().getStringExtra("id");
        intent.putExtra("id", channel);
        intent.setClass(this, new_post.class);
        startActivity(intent);
    }

    /* about map fun*/
    private void getDeviceLocation() {
        final LatLng fun_latlng = new LatLng(180,180);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        System.out.println("Now the get device: " + mFusedLocationProviderClient);

        getLocationPermission();
        View view = findViewById(R.id.newPost);
        if(isOpen(view.getContext())) {
            System.out.println("+++++++++ 1 ++++++++latlng: " + cur_lat + " --" + cur_lng);
            try {
                System.out.println("+++++++++ 2 ++++++++latlng: " + cur_lat + " --" + cur_lng);
                if (mLocationPermissionGranted) {
                    System.out.println("+++++++++ 3 ++++++++latlng: " + cur_lat + " --" + cur_lng);
                    //Toast.makeText(this, "location permission granted", Toast.LENGTH_LONG).show();
                    Task location = mFusedLocationProviderClient.getLastLocation();

                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Location currentLocation = (Location) task.getResult();

                                if(currentLocation != null) {
                                    cur_lat = currentLocation.getLatitude();
                                    cur_lng = currentLocation.getLongitude();
                                    System.out.println("+++++++++4++++++++latlng: " + cur_lat + " --  " + cur_lng);
                                }

                                getData();
                                System.out.println("+++++++++5++++++++latlng: " + cur_lat + " --  " + cur_lng);
                            } else {
                                Toast.makeText(post.this, "Unable to get the current location.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            } catch (SecurityException e) {
                Log.e("Location", "Error getting device location: " + e.getMessage());
            }
        }
        else{
            Toast.makeText(post.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(post.this, "Get location permission fail.", Toast.LENGTH_SHORT).show();
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

    public boolean isInBounds(double p_lat, double p_lng, LatLng cur_loc) {
        LatLng latLng = new LatLng(p_lat, p_lng);
        double distance = SphericalUtil.computeDistanceBetween(latLng, cur_loc);
        System.out.println("distance: " + distance + "  loc: " + cur_loc + "  po: " + latLng);
        if(distance <= 1500)
            return true;

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
