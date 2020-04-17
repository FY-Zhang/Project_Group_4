package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.AtomicDoubleArray;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class map_points extends AppCompatActivity {

    private ArrayList<GeoPoint> userCheckedPoints = new ArrayList<>();
    private List<Map<String, Object>> ofiList = new ArrayList<>();
    private List<Map<String, Object>> myList = new ArrayList<>();

    private ArrayList<String> markedLat = new ArrayList<>();
    private ArrayList<String> markedLng = new ArrayList<>();
    private ArrayList<String> markedTil = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_points);

        /*ofi poi*/
        final Intent intent = getIntent();
        if(intent != null){
            markedLat = intent.getStringArrayListExtra("ofi_lat");
            markedLng = intent.getStringArrayListExtra("ofi_lng");
            markedTil = intent.getStringArrayListExtra("ofi_til");

            System.out.println(markedLat);

            final String[] from_ofi = new String[]{"latlng","location"};
            final int[] to_ofi = new int[]{R.id.dis_ofi_latlng,R.id.dis_ofi_loc};

            for(int i = 0; i < markedTil.size(); i++) {
                Map<String, Object> ofi_poi = new HashMap<>();

                ofi_poi.put("latlng", "Lat Lng: " + markedLat.get(i) + "," + markedLng.get(i));
                ofi_poi.put("location", markedTil.get(i));

                ofiList.add(ofi_poi);
                System.out.println("Now we.. " + ofiList);
            }

            SimpleAdapter simpleAdapter_ofi = new SimpleAdapter(map_points.this, ofiList, R.layout.point_item_ofi, from_ofi, to_ofi);
            final ListView listView_ofi = findViewById(R.id.lv_ofcPoi);
            listView_ofi.setAdapter(simpleAdapter_ofi);
        }

        /*my opi*/
        final String[] from_my = new String[]{"latlng","location"};
        final int[] to_my = new int[]{R.id.dis_my_latlng,R.id.dis_my_loc};
        final List<String> pointId = new ArrayList<>();// array for id of points
        final List<Double> points_lat_my = new ArrayList<>();//record lat lng
        final List<Double> points_lng_my = new ArrayList<>();//record lat lng

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colPoiRef = db.collection("users").document(appCookies.userID).collection("points");
        colPoiRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> my_poi= new HashMap<>();

                                my_poi.put("latlng", "Lat Lng: " + document.get("latitude") + "," + document.get("longitude"));
                                my_poi.put("location", document.get("location"));

                                myList.add(my_poi);
                                pointId.add(document.getId());
                                points_lat_my.add((Double) document.get("latitude"));
                                points_lng_my.add((Double) document.get("longitude"));
                                System.out.println("pointID.. " + pointId + "lat: " + points_lat_my + " lng: " + points_lng_my);
                            }
                            final SimpleAdapter simpleAdapter_my = new SimpleAdapter(map_points.this, myList, R.layout.point_item_my, from_my, to_my);
                            final ListView listView_my = findViewById(R.id.lv_myPoi);
                            listView_my.setAdapter(simpleAdapter_my);

                            listView_my.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(map_points.this);
                                    dialog.setTitle("Points");
                                    dialog.setMessage("My points operations");
                                    dialog.setPositiveButton("Delete",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    FirebaseFirestore ff = FirebaseFirestore.getInstance();
                                                    ff.collection("users").document(appCookies.userID).collection("points")
                                                            .document(pointId.get(position))
                                                            .delete();
                                                    System.out.println("position: " + pointId.get(position));
                                                    Toast.makeText(map_points.this, "Cancel Successfully!", Toast.LENGTH_SHORT).show();
                                                    myList.remove(position);
                                                    simpleAdapter_my.notifyDataSetChanged();
                                                    listView_my.invalidate();
                                                }
                                            });
                                    dialog.setNeutralButton("Go",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    System.out.println("Info:--- " + points_lat_my.get(position) + " , " + points_lng_my.get(position));

                                                    Intent intent_toMap = new Intent(map_points.this, map_main.class);
                                                    intent_toMap.putExtra("latitude", points_lat_my.get(position).toString());
                                                    intent_toMap.putExtra("longitude", points_lng_my.get(position).toString());
                                                    intent_toMap.putExtra("location", "Destination");
                                                    intent_toMap.putExtra("from", "map_points");

                                                    startActivity(intent_toMap);
                                                    finish();
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
                    }
                });
    }
}
