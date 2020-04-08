package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
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
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.geometry.Bounds;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.provider.SettingsSlicesContract.KEY_LOCATION;

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
    private static final LatLng UL = new LatLng(52.673829, -8.572475);
    private static final LatLng Beijing = new LatLng(39.901389, 116.4214707);
    private static final LatLng Dublin = new LatLng(53.331876, -6.257069);
    private static final LatLng Washington = new LatLng(38.907757, -77.035599);
    private static final LatLng Sydney = new LatLng(-34, 151);

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

    private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

    private double cur_lat = 52.601238; //default
    private double cur_lng = -8.123021;
    private LatLng cur_loc = new LatLng(cur_lat, cur_lng);
    private Circle mAdelaideCircle;
    private Circle mULCircle;
    private Polyline mCurPL;
    private GroundOverlay mSydneyGroundOverlay;

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
        showIreland(null);

        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
    }

    private void addOfficialMarkers() { // official check points
        mMap.addMarker(new MarkerOptions().position(UL).title("University of Limerick."));
        mMap.addMarker(new MarkerOptions().position(Beijing).title("Beijing, Capital of China."));
        mMap.addMarker(new MarkerOptions().position(Dublin).title("Dublin, Capital of Ireland."));
        mMap.addMarker(new MarkerOptions().position(Washington).title("Washington, Capital of USA."));
        mMap.addMarker(new MarkerOptions().position(Sydney).title("Sydney, Capital of Sydney."));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Dublin));
    }

    public void showIreland(View v) {
        if (mMap == null)
            return;
        // Create bounds that include all locations of the map
        LatLngBounds.Builder bounds = LatLngBounds.builder()
                .include(Dublin)
                .include(UL);
        // Move camera to show all markers and locations
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20));
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

    public void map_CheckPoints(View view) {
        map_GetCurrentLocation(view);
        if(checkBounds(cur_loc, mULCircle)) {
            Toast.makeText(map_main.this, "Check Successfully!", Toast.LENGTH_SHORT).show();
            
        } else {
            Toast.makeText(map_main.this, "No nearby check points!", Toast.LENGTH_SHORT).show();
        }
    }

    public void map_listView(View view) {
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("new!!!!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        /*
        Intent intent = new Intent();
        intent.setClass(this, pointsList.class);
        startActivity(intent); */
    }

    private void addObjectsToMap() {
        // A circle centered on Adelaide.
        mAdelaideCircle = mMap.addCircle(new CircleOptions()
                .center(ADELAIDE)
                .radius(500000)
                .fillColor(Color.argb(150, 66, 173, 244))
                .strokeColor(Color.rgb(66, 173, 244))
                .clickable(true));
        mAdelaideCircle.setTag(new CustomTag("Adelaide circle"));

        mULCircle = mMap.addCircle(new CircleOptions()
                .center(UL)
                .radius(2000)
                .fillColor(Color.argb(150, 66, 173, 244))
                .strokeColor(Color.rgb(66, 173, 244))
                .clickable(true));
        mULCircle.setTag(new CustomTag("UL circle"));
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
        Toast.makeText(map_main.this, "update UI.", Toast.LENGTH_SHORT).show();

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

    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            //Toast.makeText(map_main.this, "Get device location", Toast.LENGTH_SHORT).show();
                            System.out.println("get device location");
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

    public static final boolean isOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return gps;
    } //check gps open

    public boolean checkBounds(LatLng cur_location, Circle checkpoint) {
        /*LatLng l1 = new LatLng(52.6793304,-8.577171); //52.6793304   //-8.577171;
        LatLng l2 = new LatLng(52.6738590,-8.5723265);*/
        double distance = SphericalUtil.computeDistanceBetween(cur_location, checkpoint.getCenter());
        System.out.println("cur: " + cur_location + ". cen:" + checkpoint.getCenter() + ". r: " + checkpoint.getRadius() + ". Distance:R " + distance);
        if(distance <= checkpoint.getRadius()) {
            return true;
        } else {
            Toast.makeText(map_main.this, "Sorry, you are not in the checking area", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
