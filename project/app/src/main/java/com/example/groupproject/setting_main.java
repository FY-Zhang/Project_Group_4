package com.example.groupproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Objects;

public class setting_main extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_setting,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.setting_post:
                intent = new Intent(setting_main.this, post_my.class);
                startActivity(intent);
                //finish();
                return true;
            case R.id.setting_fav:
                intent = new Intent(setting_main.this, post_favorite.class);
                startActivity(intent);
                //finish();
                return true;
            case R.id.setting_logout:
                intent = new Intent(setting_main.this, initial_page.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toChannel(View view) {
        Intent intent = new Intent(this, channel.class);
        startActivity(intent);
        finish();
    }

    public void toFriendlist(View view) {
        Intent intent = new Intent(this, friendlistActivity.class);
        startActivity(intent);
        finish();
    }

    public void toSetting(View view) {
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
           if(appCookies.frgState == 0) { //jump from set -/ not edit
                moveTaskToBack(false);
                Intent intent_toFrd = new Intent(this, friendlistActivity.class);
                startActivity(intent_toFrd);
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
