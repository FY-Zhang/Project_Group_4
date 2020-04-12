package com.example.groupproject;


import android.app.Activity;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseUser user_set = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth_set = FirebaseAuth.getInstance();

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
        if(psw.equals("000000")) {
            return true; //default - not change
        } else if(psw.isEmpty()){
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Switch sw_gender = getView().findViewById(R.id.sw_gender);
        System.out.println("Now the dp: " + appCookies.userDisplay);
        sw_gender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //trigger detect
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if(isChecked) {//display
                    appCookies.userDisplay = true;
                    System.out.println("if1 the dp: " + appCookies.userDisplay);
                    db.collection("users").document(appCookies.userID).
                            update("display", true);
                } else { //not display
                    appCookies.userDisplay = false;
                    System.out.println("if2 the dp: " + appCookies.userDisplay);
                    db.collection("users").document(appCookies.userID).
                            update("display", false);
                }}
        });
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
        final String user_psw = "000000";// default show
        String user_birthday = appCookies.userBirthday;
        final String user_sex = appCookies.userGender;
        Boolean user_display = appCookies.userDisplay;

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

        if(user_sex.equals("Male")){ //default is female
            RadioButton rb_sex1 = view.findViewById(R.id.rb1);
            //RadioButton rb_sex0 = view.findViewById(R.id.rb0);
            rb_sex1.setChecked(true);
        }

        if(user_display) {
            Switch sw_gender = view.findViewById(R.id.sw_gender);
            sw_gender.setChecked(true);
        }

        //change info
        Button btn_submit = view.findViewById(R.id.btn_submit); // click submit btn and then will happen....
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
                appCookies.username = user_name;

                EditText txt_password_txt = view.findViewById(R.id.txt_password_txt);
                String user_psw = txt_password_txt.getText().toString();
                if(user_psw.equals("000000")){
                    // not change
                } else {
                    user_set.updatePassword(user_psw);
                }

                EditText txt_email_txt = view.findViewById(R.id.txt_email_txt);
                String user_email = txt_email_txt.getText().toString();
                userRef.update("email", user_email);
                user_set.updateEmail(user_email);


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
                if(sex == R.id.rb1){
                    userRef.update("gender","Male");
                    appCookies.userGender = "Male";
                } else {
                    userRef.update("gender","Female");
                    appCookies.userGender = "Female";
                }

                if(validateUsername(user_name) && validateEmail(user_email) && validatePassword(user_psw)) {
                    Navigation.findNavController(v).navigate(R.id.action_modify_to_setting);
                    //user.updatePassword("123456");
                    //user_set.sendEmailVerification(); 使用真实邮件/后期使用
                    // FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    //获取修改密码的code?
                    //firebaseAuth.sendPasswordResetEmail(user.getEmail());
                    //firebaseAuth.verifyPasswordResetCode();
                    Toast.makeText(getActivity(), "Submitted Successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
