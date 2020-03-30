package com.example.groupproject;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


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
        btn_channel_setting .setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), channel.class);
                startActivity(intent);
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




}
