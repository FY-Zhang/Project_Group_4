package com.example.groupproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_setting_setting extends Fragment {


    public fragment_setting_setting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting,null);
        showInfo_setting(view);

        Button btn_friendlist_setting = view.findViewById(R.id.btn_friendlist_setting); //jump to friendlist
        btn_friendlist_setting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), friendlistActivity.class);
                startActivity(intent);
            }
        });

        Button btn_channel_setting = view.findViewById(R.id.btn_channel_setting); //jump to channel
        btn_channel_setting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), channel.class);
                startActivity(intent);
            }
        });

        Button btn_logout = view.findViewById(R.id.btn_logout); //jump to channel
        btn_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent();
                intent.setClass(getActivity(), Login.class);
                startActivity(intent);*/
                map_main map_t = new map_main();
                map_t.map_GetCurrentLocation(view);
            }
        });

        return view;

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_allpost_setting = view.findViewById(R.id.btn_allpost_setting); //allpost
        btn_allpost_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_setting_to_fragment_allpost_setting);
            }
        });

        Button btn_modify = view.findViewById(R.id.btn_modify); //modify
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_setting2_to_modify);
            }
        });
    }

    private void showInfo_setting(final View view){
        String user_id = appCookies.userID;//cookie之类的记录登录信息
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        System.out.println("Access!" + user_id);

        db.collection("users")
                .whereEqualTo("UID", user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) { //show info
                                TextView tv1 = view.findViewById(R.id.dis_username);
                                tv1.setText(document.getString("username"));
                                System.out.println(document.getString("username"));

                                TextView tv2 = view.findViewById(R.id.dis_email);
                                tv2.setText(document.getString("email"));
                                System.out.println(document.getString("email"));

                                TextView tv3 = view.findViewById(R.id.dis_gender);
                                tv3.setText(document.getString("gender"));
                                System.out.println(document.getString("gender"));

                                TextView tv4 = view.findViewById(R.id.dis_birthday);
                                tv4.setText(document.getString("birthday"));
                                System.out.println(document.getString("birthday"));
                            }
                        }
                    }
                });
    }


}
