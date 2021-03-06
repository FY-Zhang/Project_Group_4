package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.List;

public class initial_page extends AppCompatActivity {

    private  static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_page);

        Log.d("Token-Firebase", ""+ FirebaseInstanceId.getInstance().getToken());

        Intent intent = getIntent();
        String newUser = intent.getStringExtra("new_user");
        System.out.println("user value: " + newUser);
        if(newUser != null) {
            if(newUser.equals("true")){
                Toast.makeText(initial_page.this, "You can log in now :)", Toast.LENGTH_SHORT).show();
            }
        }
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
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //Toast.makeText(this, "Exiting", Toast.LENGTH_SHORT).show();
            System.out.println("to exit");
            appCookies.curUserLoc = "null-null";
            //finish();
            moveTaskToBack(false);
            /*finish();
            System.exit(0); */
            System.out.println("------------");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
