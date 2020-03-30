package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class setting_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        showInfo_setting();
    }

    public void showInfo_setting(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) { //show info
                                TextView tv1 = findViewById(R.id.dis_username);
                                tv1.setText(document.getString("username"));

                                TextView tv2 = findViewById(R.id.dis_phone);
                                tv2.setText(document.getString("phone"));

                                TextView tv3 = findViewById(R.id.dis_email);
                                tv3.setText(document.getString("email"));

                                TextView tv4 = findViewById(R.id.dis_gender);
                                tv4.setText(document.getString("gender"));

                                TextView tv5 = findViewById(R.id.dis_age);
                                tv5.setText(document.getString("age"));
                            }
                        }
                    }
                });
    }

    public void onClickStoreData_setting(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference username = db.collection("username");

        
    }
}
