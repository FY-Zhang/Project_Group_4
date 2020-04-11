package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import static com.example.groupproject.appCookies.*;
import static com.example.groupproject.storeUserInfo.*;

public class initial_page extends AppCompatActivity {

    private  static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_page);
    }

    public void onClickSignIn(View view){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),RC_SIGN_IN);
    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                toCompleteLogin();
                return;
            }else {
                if(response == null){
                    System.out.println("Sign in cancelled");
                    return;
                }
                if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                    System.out.println("No internet connection");
                    return;
                }
            }
        }
    }

    protected void toCompleteLogin()
    {
        Intent intent = new Intent();
        intent.setClass(this, friendlistActivity.class);
        startActivity(intent);
    }

}
