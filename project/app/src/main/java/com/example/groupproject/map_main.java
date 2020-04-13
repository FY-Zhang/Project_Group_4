package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.provider.FontsContractCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.maps.android.SphericalUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class map_main extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private static final String TAG = map_main.class.getSimpleName();
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private static final String KEY_LOCATION = "location";

    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private double cur_lat = 52.601238; //default
    private double cur_lng = -8.123021;
    private LatLng cur_loc = new LatLng(cur_lat, cur_lng);

    private double cus_lat;
    private double cus_lng;
    private LatLng cus_loc;
    private Marker cus_mkr;

    private static final LatLng Beijing = new LatLng(39.901389, 116.4214707);
    private static final LatLng UL = new LatLng(52.673829, -8.572475);
    private static final LatLng Washington = new LatLng(38.907757, -77.035599);
    private static final LatLng Sydney = new LatLng(-33.868819, 151.2092955);
    private static final LatLng Berlin = new LatLng(52.520006, 13.404954);

    private ArrayList<Marker> markerList = new ArrayList<Marker>();
    private Marker mkrBeijing;
    private Marker mkrUL;
    private Marker mkrWashington;
    private Marker mkrSydney;
    private Marker mkrBerlin;
    private Marker mkrDrag;

    private ArrayList<Circle> circlesList = new ArrayList<Circle>();
    private Circle BeijingCircle;
    private Circle ULCircle;
    private Circle SydneyCircle;
    private Circle WashingtonCircle;
    private Circle BerlinCircle;

    private ArrayList<String> markedLat = new ArrayList<>();
    private ArrayList<String> markedLng = new ArrayList<>();
    private ArrayList<String> markedTil = new ArrayList<>();

    //private Polyline CurPL;
    //private GroundOverlay SydneyGroundOverlay;

    private ArrayList<GeoPoint> gp_myPoint = new ArrayList<>(); // custom design points

    private static class CustomTag {
        private final String description;
        private int clickCount;
        public CustomTag(String description) {
            this.description = description;
            clickCount = 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        setContentView(R.layout.activity_map_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_check_frg);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addOfficialMarkers();
        addObjectsToMap();
        showEurope(null); //or chat loc

        getLocationPermission();
        updateLocationUI();
        mMap.setOnMarkerClickListener(this);
        //getDeviceLocation();

        updateMarkers();
    }

    private void addOfficialMarkers() { // official check points
        mkrBeijing = mMap.addMarker(new MarkerOptions().position(Beijing).title("Beijing, Capital of China. \uD83C\uDDE8\uD83C\uDDF3."));
        mkrUL = mMap.addMarker(new MarkerOptions().position(UL).title("University of Limerick, Ireland. \uD83C\uDDEE\uD83C\uDDEA"));
        mkrWashington = mMap.addMarker(new MarkerOptions().position(Washington).title("Washington, Capital of USA. \uD83C\uDDFA\uD83C\uDDF8"));
        mkrSydney = mMap.addMarker(new MarkerOptions().position(Sydney).title("Sydney, Capital of Australia. \uD83C\uDDE6\uD83C\uDDFA"));
        mkrBerlin = mMap.addMarker(new MarkerOptions().position(Berlin).title("Berlin, Capital of Germany \uD83C\uDDE9\uD83C\uDDEA."));

        mkrDrag = mMap.addMarker(new MarkerOptions().position(new LatLng(51.5, 0.1)).title("Drag me to get the position!"));
        mkrDrag.setDraggable(true);

        markerList.add(mkrBeijing);
        markerList.add(mkrUL);
        markerList.add(mkrWashington);
        markerList.add(mkrSydney);
        markerList.add(mkrBerlin);
    }

    public void showEurope(View v) {
        if (mMap == null)
            return;
        // Create bounds that include all locations of the map
        LatLngBounds.Builder bounds = LatLngBounds.builder()
                .include(UL)
                .include(Berlin);
        // Move camera to show all markers and locations
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 200));
        System.out.println("The show fun");

        Intent intent = getIntent(); // custom design points from chat
        System.out.println("intent: " + intent);
        if(intent != null) {
            System.out.println("now the in: " + intent);
            //System.out.println("the lat: " + intent.getStringExtra("latitude"));
            String cus_lat_str = intent.getStringExtra("latitude");
            String cus_lng_str = intent.getStringExtra("longitude");

            if(cus_lat_str != null && cus_lng_str != null) {
                cus_lat = Double.parseDouble(cus_lat_str);
                cus_lng = Double.parseDouble(cus_lng_str);
                String cus_add = intent.getStringExtra("location");
                cus_loc = new LatLng(cus_lat, cus_lng);
                System.out.println("------" + cus_loc + " address: " + cus_add);

                cus_mkr = mMap.addMarker(new MarkerOptions().position(cus_loc).title(cus_add));
                cus_mkr.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(cus_loc));
            }
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if(marker.getPosition().equals(cus_loc)) {
            marker.showInfoWindow();

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Collect");
            dialog.setMessage("Do you want to collect this place?");
            dialog.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference colPoiRef = db.collection("users").document(appCookies.userID).collection("points");

                            Map<String, Object> data = new HashMap<>();
                            data.put("latitude", cus_lat);
                            data.put("longitude", cus_lng);
                            data.put("location", cus_mkr.getTitle());

                            colPoiRef.document(cus_mkr.getTitle()).set(data, SetOptions.merge());

                            Toast.makeText(map_main.this, "Collect Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(map_main.this, map_points.class);//flush this page
                            startActivity(intent);
                        }
                    });
            dialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    });
            dialog.show();
        }

        return false;
    }

    public void map_GetCurrentLocation(View view) {
        if (mMap == null) { return; }

        getLocationPermission();//check again

        if(isOPen(view.getContext())) {// if has open GPS
            getDeviceLocation();
            if (mLocationPermissionGranted) { //if permission established

                getDeviceLocation();

                if(mLastKnownLocation == null){ //待改善
                    int try_t = 0;
                    cur_lat = 52.00;
                    cur_lng = -8.00;
                    Toast.makeText(map_main.this, "Getting address ..." , Toast.LENGTH_SHORT).show();

                    while(mLastKnownLocation == null && try_t < 10) { //try to get location
                        getDeviceLocation();
                        System.out.println("Location: " + mLastKnownLocation);
                        try_t++;
                    }

                    if(mLastKnownLocation == null) { // if still fail
                        Toast.makeText(map_main.this, "Please try again!", Toast.LENGTH_SHORT).show();
                    } else { // if success
                        cur_lat = mLastKnownLocation.getLatitude();
                        cur_lng = mLastKnownLocation.getLongitude();
                        Toast.makeText(map_main.this, "Lat: " + cur_lat + ". Lng: " + cur_lng, Toast.LENGTH_SHORT).show();
                        System.out.println("1 Lat: " + cur_lat + ". Lng: " + cur_lng);
                    }
                } else {
                    cur_lat = mLastKnownLocation.getLatitude();
                    cur_lng = mLastKnownLocation.getLongitude();
                    Toast.makeText(map_main.this, "Lat: " + cur_lat + ". Lng: " + cur_lng, Toast.LENGTH_SHORT).show();
                    System.out.println("2 Lat: " + cur_lat + ". Lng: " + cur_lng);
                }

                cur_loc = new LatLng(cur_lat, cur_lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(cur_loc));
            } else {
                Log.i(TAG, "Please grant location permission.");
                Toast.makeText(map_main.this, "Please grant location permission", Toast.LENGTH_SHORT).show();

                getLocationPermission();
            }
        }
        else{
            Toast.makeText(map_main.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
        }
    }

    public void map_CheckPoints(View view) { // once button, all check
        map_GetCurrentLocation(view);
        updateMarkers();

        int num = 0;
        for(int i = 0; i < circlesList.size(); i++){
            if (checkBounds(cur_loc, circlesList.get(i))) { //get circle center
                double lat = circlesList.get(i).getCenter().latitude;
                double lng = circlesList.get(i).getCenter().longitude;

                if(isChecked(lat, lng)){ //if checked
                    Toast.makeText(map_main.this, "You have checked!", Toast.LENGTH_SHORT).show();
                } else { //if new, insert
                    //String user_id = appCookies.userID;
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document(appCookies.userID)
                            .update("checkPoint", FieldValue.arrayUnion(new GeoPoint(lat, lng))); //add into db
                    appCookies.userCheckedPoints.add(new GeoPoint(lat, lng));
                    Toast.makeText(map_main.this, "You checked successfully!", Toast.LENGTH_SHORT).show();

                    System.out.println("success check * -- =");
                    updateMarkers();
                }
                num++;
            }
        }
        if(num == 0) { Toast.makeText(map_main.this, "No nearby check points!", Toast.LENGTH_SHORT).show(); }
    }

    public void map_listView(View view) {
        System.out.println("marked marker:------------------1");

        Intent intent = new Intent(map_main.this, map_points.class);
        intent.putExtra("ofi_lat", markedLat);
        intent.putExtra("ofi_lng", markedLng);
        intent.putExtra("ofi_til", markedTil);

        startActivity(intent);
    }

    private void addObjectsToMap() {

        BeijingCircle = mMap.addCircle(new CircleOptions()
                .center(Beijing)
                .radius(50000)
                .fillColor(Color.argb(150, 66, 173, 244))
                .strokeColor(Color.rgb(66, 173, 244))
                .clickable(true));
        BeijingCircle.setTag(new CustomTag("Beijing, China"));

        SydneyCircle = mMap.addCircle(new CircleOptions()
                .center(Sydney)
                .radius(50000)
                .fillColor(Color.argb(150, 66, 173, 244))
                .strokeColor(Color.rgb(66, 173, 244))
                .clickable(true));
        SydneyCircle.setTag(new CustomTag("Sydney, Australia"));

        ULCircle = mMap.addCircle(new CircleOptions()
                .center(UL)
                .radius(2000)
                .fillColor(Color.argb(150, 66, 173, 244))
                .strokeColor(Color.rgb(66, 173, 244))
                .clickable(true));
        ULCircle.setTag(new CustomTag("UL, Ireland"));

        WashingtonCircle = mMap.addCircle(new CircleOptions()
                .center(Washington)
                .radius(50000)
                .fillColor(Color.argb(150, 66, 173, 244))
                .strokeColor(Color.rgb(66, 173, 244))
                .clickable(true));
        WashingtonCircle.setTag(new CustomTag("Washington, USA"));

        BerlinCircle = mMap.addCircle(new CircleOptions()
                .center(Berlin)
                .radius(50000)
                .fillColor(Color.argb(150, 66, 173, 244))
                .strokeColor(Color.rgb(66, 173, 244))
                .clickable(true));
        WashingtonCircle.setTag(new CustomTag("Berlin, Germany"));

        circlesList.add(BeijingCircle); //store these circles in list
        circlesList.add(ULCircle);
        circlesList.add(WashingtonCircle);
        circlesList.add(SydneyCircle);
        circlesList.add(BerlinCircle);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            Toast.makeText(map_main.this, "Get location permission success.", Toast.LENGTH_SHORT).show();
            System.out.println("get success");
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Toast.makeText(map_main.this, "Get location permission fail.", Toast.LENGTH_SHORT).show();
            System.out.println("get fail");
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
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void updateMarkers(){ //change colour for checked circle -- warning: used after getPoint()
        Toast.makeText(map_main.this, "Update markers", Toast.LENGTH_SHORT).show();
        double lat1, lat2, lng1, lng2;
        System.out.println("The size: " + appCookies.userCheckedPoints.size());
        for(int i = 0; i < appCookies.userCheckedPoints.size(); i++) {
            lat1 = appCookies.userCheckedPoints.get(i).getLatitude();
            lng1 = appCookies.userCheckedPoints.get(i).getLongitude();

            for(int j = 0; j < markerList.size(); j++) {
                lat2 = markerList.get(j).getPosition().latitude;
                lng2 = markerList.get(j).getPosition().longitude;


                if(lat1 == lat2 && lng1 == lng2){
                    markerList.get(j).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)); // checked marker

                    String lat_t = String.valueOf(markerList.get(j).getPosition().latitude);
                    String lng_t = String.valueOf(markerList.get(j).getPosition().longitude);
                    if(!markedTil.contains(markerList.get(j).getTitle())) { // discard duplicate
                        markedLat.add(lat_t);
                        markedLng.add(lng_t);
                        markedTil.add(markerList.get(j).getTitle());
                    }
                }
            }
        }
    }

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            //System.out.println("get device location");
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Toast.makeText(map_main.this, "Current Location is null ", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", Objects.requireNonNull(e.getMessage()));
        }
    }

    public static boolean isOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    } //check gps open

    public boolean checkBounds(LatLng cur_location, Circle checkpoint) {
        double distance = SphericalUtil.computeDistanceBetween(cur_location, checkpoint.getCenter());
        System.out.println("cur: " + cur_location + ". cen:" + checkpoint.getCenter() + ". r: " + checkpoint.getRadius() + ". Distance: " + distance);
        return distance <= checkpoint.getRadius();
    }

    public boolean isChecked(double lat, double lng) {
        for(int i = 0; i < appCookies.userCheckedPoints.size(); i++) { //check whether the latlng exists in the copy array
            if(lat == appCookies.userCheckedPoints.get(i).getLatitude()
                    && lng == appCookies.userCheckedPoints.get(i).getLongitude()){
                //System.out.println("now pass: " + lat + " ~ " + lng);
                //System.out.println("now in arr: " + gp_checkPoint.get(i).getLatitude() + " ~ " + gp_checkPoint.get(i).getLongitude());
                return true;
            }
        }
        System.out.println("Not existed.");
        return false;
    }
}
