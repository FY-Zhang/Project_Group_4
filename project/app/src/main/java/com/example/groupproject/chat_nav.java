package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.groupproject.appCookies.*;

public class chat_nav extends AppCompatActivity {

    Toolbar toolbar;

    EditText messageSend;
    Button send;
    DatabaseReference dbMessage;
    ListView textBox;
    List<ChatMessage> messageList;

    String friendName;
    String friendID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_nav);

        //initialize toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        friendName = intent.getStringExtra("friend_name");
        friendID = intent.getStringExtra("friend_id");

        toolbar.setTitle(friendName);

        //send message
        String dbName;
        if(userID.compareTo(friendID) > 0) {
            dbName  = "ChatMessage_"+userID+"-"+friendID;
        }else {
            dbName = "ChatMessage_"+friendID+"_"+userID;
        }

            dbMessage = FirebaseDatabase.getInstance().getReference(dbName);
        messageSend = (EditText)findViewById(R.id.inputBox);
        send = (Button)findViewById(R.id.sendMessage);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToRealtimeDatabase();
            }
        });

        //retrieve message
        textBox = (ListView)findViewById(R.id.textBox);
        messageList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                messageList.clear();

                for(DataSnapshot messageSnapshot : dataSnapshot.getChildren()){
                    ChatMessage chatMessage = messageSnapshot.getValue(ChatMessage.class);
                        messageList.add(chatMessage);
                }

                MessageList adapter = new MessageList(chat_nav.this, messageList, friendName, friendID);
                textBox.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void addToRealtimeDatabase(){
        String message = messageSend.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd_HH:mm_ss_a");
        Date date = new Date();

        if(!TextUtils.isEmpty(message)){

            String id = dbMessage.push().getKey();

            ChatMessage messageInfo = new ChatMessage(message, userID,sdf.format(date) );

            dbMessage.child(id).setValue(messageInfo);

        }else{
            Toast.makeText(this, "Message cant't be null", Toast.LENGTH_SHORT).show();
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
                intent.setClass(chat_nav.this, chat_camera.class);
                startActivity(intent);
                return true;
            case R.id.chat_location:
                intent.setClass(chat_nav.this, chat_location.class);
                startActivity(intent);
                return true;
            case R.id.chat_friend_info:
                return true;
            case R.id.map_check:
                intent.setClass(chat_nav.this, map.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}