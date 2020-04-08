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
    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private static final LatLng DARWIN = new LatLng(-12.425892, 130.86327);
    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

    private Circle mAdelaideCircle;
    private GroundOverlay mSydneyGroundOverlay;
    private Polygon mDarwinPolygon;
    private Polyline mPolyline;

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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

    public void map_GetCurrentLocation(View view) { //onclick fun on btn_Current // 获取经纬度
        //Toast.makeText(map_main.this, "in get ", Toast.LENGTH_SHORT).show();
        if (mMap == null) {
            return;
        }

        getLocationPermission();//check again

        if(isOPen(view.getContext())) {// if has open GPS
            getDeviceLocation();
            if (mLocationPermissionGranted) { //if permission established

                double lat;
                double lng;

                getDeviceLocation();

                if(mLastKnownLocation == null){
                    int try_t = 0;
                    lat = 52;
                    lng = -8;
                    Toast.makeText(map_main.this, "Getting address ..." , Toast.LENGTH_SHORT).show();

                    while(mLastKnownLocation == null && try_t < 10) { //try to get location
                        getDeviceLocation();
                        lat = 52;
                        lng = -8;
                        try_t++;
                    }

                    if(mLastKnownLocation == null) { // if still fail
                        Toast.makeText(map_main.this, "Please try again!", Toast.LENGTH_SHORT).show();
                    } else { // if success
                        lat = mLastKnownLocation.getLatitude();
                        lng = mLastKnownLocation.getLongitude();
                        Toast.makeText(map_main.this, "Lat: " + lat + ". Lng: " + lng, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    lat = mLastKnownLocation.getLatitude();
                    lng = mLastKnownLocation.getLongitude();
                    Log.i(TAG, "success want to get lt.");
                    Toast.makeText(map_main.this, "Lat: " + lat + ". Lng: " + lng, Toast.LENGTH_SHORT).show();
                }

                LatLng cur_p = new LatLng(lat, lng);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(cur_p));
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
        //符合条件则将官方标签变色
        Toast.makeText(map_main.this, "Check Successfully!", Toast.LENGTH_SHORT).show();
        //Toast.makeText(map_main.this, "No nearby check points!", Toast.LENGTH_SHORT).show();
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

        // A ground overlay at Sydney.
        mSydneyGroundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.icon_camera))
                .position(SYDNEY, 700000)
                .clickable(true));
        mSydneyGroundOverlay.setTag(new CustomTag("Sydney ground overlay"));

        // A polygon centered at Darwin.
        mDarwinPolygon = mMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(DARWIN.latitude + 3, DARWIN.longitude - 3),
                        new LatLng(DARWIN.latitude + 3, DARWIN.longitude + 3),
                        new LatLng(DARWIN.latitude - 3, DARWIN.longitude + 3),
                        new LatLng(DARWIN.latitude - 3, DARWIN.longitude - 3))
                .fillColor(Color.argb(150, 34, 173, 24))
                .strokeColor(Color.rgb(34, 173, 24))
                .clickable(true));
        mDarwinPolygon.setTag(new CustomTag("Darwin polygon"));

        // A polyline from Perth to Brisbane.
        mPolyline = mMap.addPolyline(new PolylineOptions()
                .add(PERTH, BRISBANE)
                .color(Color.rgb(103, 24, 173))
                .width(30)
                .clickable(true));
        mPolyline.setTag(new CustomTag("Perth to Brisbane polyline"));
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


}
