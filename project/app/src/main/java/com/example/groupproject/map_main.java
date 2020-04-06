package com.example.groupproject;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class map_main extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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
        LatLng lero = new LatLng(52.674423, -8.576994); //在这里读取firebase,然后写入
        mMap.addMarker(new MarkerOptions().position(lero).title("Lero"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lero));
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

}
