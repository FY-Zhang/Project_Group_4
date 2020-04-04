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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

    public static boolean isEmpty(String str) { //check empty
        int i = str.indexOf(" ");
        if(i == -1)
            return true;
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //click back
        Button btn_back_modify = view.findViewById(R.id.btn_back_modify);
        btn_back_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_modify_to_setting);
            }
        });

        //modify - setting
            //show current info
        String user_id = "10000001"; //cookie之类的记录登录信息 (邮箱登录,然后存储id -- 是否直接使用邮箱为主键?)
        String user_name = "Auth01";
        String user_phone = "1010";
        String user_age = "10";
        String user_sex = "Female";

        EditText txt_username = view.findViewById(R.id.txt_username_modify);
        txt_username.setText(user_name);

        EditText txt_phone = view.findViewById(R.id.txt_phone_modify);
        txt_phone.setText(user_phone);

        EditText txt_age = view.findViewById(R.id.txt_age_modify);
        txt_age.setText(user_age);

        /*EditText txt_sex = view.findViewById(R.id.rb_sex_modify);
        txt_username.setText(user_name);*/
            //change info
        Button btn_submit = view.findViewById(R.id.btn_submit); // click submit
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = "10000001";
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference userRef = db.collection("users").document(user_id);

                //get text user entered
                EditText txt_username = view.findViewById(R.id.txt_username_modify);
                String user_name = txt_username.getText().toString();
                userRef.update("username", user_name);

                EditText txt_phone = view.findViewById(R.id.txt_phone_modify);
                String user_phone = txt_phone.getText().toString();
                userRef.update("phone", user_phone);

                EditText txt_age = view.findViewById(R.id.txt_age_modify);
                String user_age = txt_age.getText().toString();
                userRef.update("age", user_age);


                //userRef.update("gender",user_sex);
               /* RadioGroup rg_sex = view.findViewById(R.id.rg_sex);//RadioGroup
                    int sex = rg_sex.getCheckedRadioButtonId();
                    System.out.println("sex id: " + sex);*/
               /*     String user_sex;
                    if(sex == R.id.rb0)
                        user_sex = "Female";
                    else
                        user_sex = "Male";*/


                if(user_name.equals("") || !isEmpty(user_name))
                    Toast.makeText(getActivity(), "Valid Username!", Toast.LENGTH_SHORT).show();
                else{
                    Navigation.findNavController(v).navigate(R.id.action_modify_to_setting);
                    Toast.makeText(getActivity(), "Submitted Successfully!", Toast.LENGTH_SHORT).show();
                    System.out.println("phone: " + user_phone);
                }
            }
        });
    }
}
