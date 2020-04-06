package com.example.groupproject;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.regex.Pattern;

import io.opencensus.internal.StringUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_modify_setting extends Fragment {

    private TextInputLayout txt_username;
    private TextInputLayout txt_email;
    private TextInputLayout txt_password;

    private boolean validateUsername(String username) {
        if(username.isEmpty()) {
            txt_username.setError("Username cannot be empty!");
            return false;
        }else if(username.length()>15) {
            txt_username.setError("Username too long");
            return false;
        }else if(username.trim().isEmpty()){
            txt_username.setError("Valid Username!");
            return false;
        }else {
            txt_username.setError(null);
            return true;
        }
    }

    private boolean validateEmail(String user_email) {
        if(user_email.isEmpty()) {
            txt_email.setError("Email cannot be empty!");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
            txt_email.setError("Valid Email address!");
            return false;
        } else {
            txt_email.setError(null);
            return true;
        }
    }

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{6,}" +
                    "$");

    private boolean validatePassword(String psw) {
        if(psw.isEmpty()){
            txt_password.setError("Password cannot be empty!");
            return false;
        } else if(!PASSWORD_PATTERN.matcher(psw).matches()) {
            txt_password.setError("Password too weak");
            return false;
        }else {
            txt_password.setError(null);
            return true;
        }
    }


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
        String user_id = appCookies.userID; //cookie之类的记录登录信息 (邮箱登录,然后存储id -- 是否直接使用邮箱为主键?)
        String user_name = appCookies.username;
        String user_email = appCookies.userEmail;
        final String user_psw = "111111";
        String user_birthday = appCookies.userBirthday;
        String user_sex = appCookies.userGender;

        //initial original display
        txt_username = view.findViewById(R.id.txt_username_til);
        EditText txt_username_txt = view.findViewById(R.id.txt_username_txt);
        txt_username_txt.setText(user_name);

        txt_email = view.findViewById(R.id.txt_email_til);
        EditText txt_email_txt = view.findViewById(R.id.txt_email_txt);
        txt_email_txt.setText(user_email);

        txt_password = view.findViewById(R.id.txt_password_til);
        EditText txt_password_txt = view.findViewById(R.id.txt_password_txt);
        txt_password_txt.setText(user_psw);

        EditText txt_birthday_txt = view.findViewById(R.id.txt_birthday_txt);
        txt_birthday_txt.setText(user_birthday);


        //change info
        Button btn_submit = view.findViewById(R.id.btn_submit); // click submit
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = appCookies.userID;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference userRef = db.collection("users").document(user_id);

                //get text user entered
                EditText txt_username_txt = view.findViewById(R.id.txt_username_txt);
                String user_name = txt_username_txt.getText().toString();
                userRef.update("username", user_name);
                appCookies.username = user_name; //使用set/get函数?

                EditText txt_password_txt = view.findViewById(R.id.txt_password_txt);
                String user_psw = txt_password_txt.getText().toString();
                userRef.update("password", user_psw);

                EditText txt_email_txt = view.findViewById(R.id.txt_email_txt);
                String user_email = txt_email_txt.getText().toString();
                userRef.update("email", user_email);

                final EditText txt_birthday_txt = view.findViewById(R.id.txt_birthday_txt);
                txt_birthday_txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);

                        DatePickerDialog picker = new DatePickerDialog(getActivity(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        txt_birthday_txt.setText(dayOfMonth + "/" + month + "/" + year);
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                });
                String user_birthday = txt_birthday_txt.getText().toString();
                userRef.update("birthday", user_birthday);

                RadioGroup rg_sex = view.findViewById(R.id.rg_sex);//RadioGroup
                int sex = rg_sex.getCheckedRadioButtonId();
                String user_sex = "Female";
                if(sex == R.id.rb1)
                    user_sex = "Male";
                userRef.update("gender",user_sex);
                //System.out.println("sex id: " + sex);

                /*if(user_name.equals("") || !TextUtils.isEmpty(user_name))
                    Toast.makeText(getActivity(), "Valid Username!", Toast.LENGTH_SHORT).show();
                else if(user_phone.equals("") || !isEmpty(user_phone))
                    Toast.makeText(getActivity(), "Valid Phone!", Toast.LENGTH_SHORT).show();
                else if(user_age.equals("") || Integer.parseInt(user_age) <= 0 || Integer.parseInt(user_age) > 100)
                    Toast.makeText(getActivity(), "Valid Age!", Toast.LENGTH_SHORT).show();
                else{
                    Navigation.findNavController(v).navigate(R.id.action_modify_to_setting);
                    Toast.makeText(getActivity(), "Submitted Successfully!", Toast.LENGTH_SHORT).show();
                    System.out.println("phone: " + user_phone);*/
                if(validateUsername(user_name) && validateEmail(user_email) && validatePassword(user_psw)) {
                    Navigation.findNavController(v).navigate(R.id.action_modify_to_setting);
                    Toast.makeText(getActivity(), "Submitted Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
