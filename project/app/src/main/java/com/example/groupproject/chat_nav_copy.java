package com.example.groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class chat_nav_copy extends AppCompatActivity {

    Toolbar toolbar;

    EditText messageSend;
    Button send;
    DatabaseReference dbMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_nav);

        //initialize toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String friendName = intent.getStringExtra(friendlistActivity.FRIEND_NAME);

        toolbar.setTitle(friendName);

        //send message
        dbMessage = FirebaseDatabase.getInstance().getReference("com.example.groupproject.ChatMessage");
        messageSend = (EditText)findViewById(R.id.inputBox);
        send = (Button)findViewById(R.id.sendMessage);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToRealtimeDatabase();
            }
        });

    }

    private void addToRealtimeDatabase(){
        String message = messageSend.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        //System.out.println("现在时间：" + sdf.format(date)); // 输出已经格式化的现在时间（24小时制）

        if(!TextUtils.isEmpty(message)){

            String id = dbMessage.push().getKey();

            ChatMessage messageInfo = new ChatMessage(message, appCookies.userID, "send_id",sdf.format(date) );
        }else{
            Toast.makeText(this, "Message cant't be null", Toast.LENGTH_LONG).show();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_chat,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()){
            case R.id.chat_camera:
                intent.setClass(chat_nav_copy.this, chat_camera.class);
                startActivity(intent);
                return true;
            case R.id.chat_location:
                intent.setClass(chat_nav_copy.this, chat_location.class);
                startActivity(intent);
                return true;
            case R.id.chat_friend_info:
                return true;
            case R.id.friend_map:
                intent.setClass(chat_nav_copy.this, map.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
