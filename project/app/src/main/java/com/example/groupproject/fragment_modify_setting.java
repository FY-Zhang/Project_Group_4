package com.example.groupproject;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_modify_setting extends Fragment {


    public fragment_modify_setting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_back_modify = view.findViewById(R.id.btn_back_modify); //click back
        btn_back_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_modify_to_setting);
            }
        });

        Button btn_submit = view.findViewById(R.id.btn_submit); // click submit
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_id = "10000001"; //cookie之类的记录登录信息
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference userRef = db.collection("users").document(user_id);

                userRef.update("username", "Auth_x");
                userRef.update("email","email_x");
                userRef.update("phone","1123456");
                userRef.update("gender","test");
                userRef.update("age", "1");


                Navigation.findNavController(v).navigate(R.id.action_modify_to_setting);
            }
        });
    }
}
