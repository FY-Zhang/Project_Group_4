package com.example.groupproject;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class map_main extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
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
    private static final LatLng HOBART = new LatLng(-42.8823388, 147.311042);
    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

    private Circle mAdelaideCircle;
    private GroundOverlay mSydneyGroundOverlay;
    private Marker mHobartMarker;
    private Polygon mDarwinPolygon;
    private Polyline mPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        showIreland(null);
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
        //获取当前位置信息
        Toast.makeText(map_main.this, "Current Location: ", Toast.LENGTH_SHORT).show();
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

        // A marker at Hobart.
        mHobartMarker = mMap.addMarker(new MarkerOptions().position(HOBART));
        mHobartMarker.setTag(new CustomTag("Hobart marker"));

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

}
