package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.provider.FontsContractCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.google.maps.android.SphericalUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class map_main extends FragmentActivity implements OnMapReadyCallback {

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

    private ArrayList<Circle> circlesList = new ArrayList<Circle>();
    private Circle BeijingCircle;
    private Circle ULCircle;
    private Circle SydneyCircle;
    private Circle WashingtonCircle;
    private Circle BerlinCircle;

    private Polyline CurPL;
    private GroundOverlay SydneyGroundOverlay;

    //private ArrayList<GeoPoint> gp_checkPoint = new ArrayList<>();
    private ArrayList<GeoPoint> gp_myPoint = new ArrayList<>();

    private static class CustomTag {
        private final String description;
        private int clickCount;

        public CustomTag(String description) {
            this.description = description;
            clickCount = 0;
        }
        public void incrementClickCount() {
            clickCount++;
        }
        @Override
        public String toString() {
            return "The " + description + " has been clicked " + clickCount + " times.";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            //mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
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
        showEurope(null);

        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();

        View map_view = findViewById(R.id.btn_check_map);
        map_GetCurrentLocation(map_view);
        System.out.println("************* 1 ***************");
        System.out.println("************** 2 **************");
        System.out.println("array: " + appCookies.userCheckedPoints);
        System.out.println("************** 3 **************");

        updateMarkers();
    }

    private void addOfficialMarkers() { // official check points
        mkrBeijing = mMap.addMarker(new MarkerOptions().position(Beijing).title("Beijing, Capital of China \uD83C\uDDE8\uD83C\uDDF3."));
        mkrUL = mMap.addMarker(new MarkerOptions().position(UL).title("University of Limerick."));
        mkrWashington = mMap.addMarker(new MarkerOptions().position(Washington).title("Washington, Capital of USA."));
        mkrSydney = mMap.addMarker(new MarkerOptions().position(Sydney).title("Sydney, Capital of Sydney."));
        mkrBerlin = mMap.addMarker(new MarkerOptions().position(Berlin).title("Berlin, Capital of Germany \uD83C\uDDE9\uD83C\uDDEA."));

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
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100));
    }

    public void map_GetCurrentLocation(View view) {
        //onclick fun on btn_Current // 获取经纬度
        if (mMap == null) { return; }

        getLocationPermission();//check again

        if(isOPen(view.getContext())) {// if has open GPS
            getDeviceLocation();
            if (mLocationPermissionGranted) { //if permission established

                getDeviceLocation();

                if(mLastKnownLocation == null){
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
                //System.out.println("=======N:" + i + "# Cur lat: " + cur_lat + "Cur lng: " + cur_lng);
                //System.out.println("# o lat: " + lat + "o lng: " + lng);
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
/*
    protected void searchPoints(){
        System.out.println("-------------------------------------");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document("10000001")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        System.out.println("&& Go to get points");

                        gp_checkPoint = (ArrayList<GeoPoint>) documentSnapshot.get("checkPoint");
                        System.out.println("Now the gp_c: " + gp_checkPoint);

                        //getPoints(documentSnapshot);//here to get points into gp_c list
                        System.out.println("&& End to get points");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error getting document.", e);
                        System.out.println("Error happened! ");
                    }});
        System.out.println("-------------------------------------");
    }

    protected void getPoints(DocumentSnapshot data){ // copy points from db
        gp_checkPoint = (ArrayList<GeoPoint>) data.get("checkPoint");
        //System.out.println("Get points: " + gp_checkPoint.get(0).getLatitude());
        System.out.println("Now the gp_c: " + gp_checkPoint);
    }
*/
    public void map_listView(View view) {
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("new!!!!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        System.out.println("in list ^^^: (point list) " + appCookies.userCheckedPoints);

        /*
        Intent intent = new Intent();
        intent.setClass(this, pointsList.class);
        startActivity(intent); */
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
        Toast.makeText(map_main.this, "in the on request location.", Toast.LENGTH_SHORT).show();
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
        //Toast.makeText(map_main.this, "update UI.", Toast.LENGTH_SHORT).show();

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
            System.out.println(">> lat1: " + lat1 + " lng1: " + lng1);
            for(int j = 0; j < markerList.size(); j++) {
                lat2 = markerList.get(j).getPosition().latitude;
                lng2 = markerList.get(j).getPosition().longitude;

                System.out.println(">> lat2: " + lat2 + " lng2: " + lng2);
                System.out.println("the out m: " + markerList.get(j));

                if(lat1 == lat2 && lng1 == lng2){
                    markerList.get(j).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    System.out.println("the in m: " + markerList.get(j).getTitle());
                    System.out.println("))))))CHANGE((((((((");
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
