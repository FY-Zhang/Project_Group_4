package com.example.groupproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import static com.example.groupproject.appCookies.userAvatar;


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

        return view;

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_modify = view.findViewById(R.id.btn_modify); //modify
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_setting2_to_modify);
            }
        });

        ImageView avatar = view.findViewById(R.id.imageView);

        avatar.setAdjustViewBounds(true);
        Picasso.with(avatar.getContext())
                .load(userAvatar)
                .into(avatar);
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

   /* public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_setting,menu);
        return true;
    } */

}
