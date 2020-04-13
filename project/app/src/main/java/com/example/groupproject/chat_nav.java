package com.example.groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.groupproject.appCookies.*;

public class chat_nav extends AppCompatActivity {

    Toolbar toolbar;

    String friendName;
    String friendID;
    String dbName;

    //Message send
    private EditText messageSend;
    private Button send;
    private DatabaseReference dbMessage;
    private RecyclerView textBox;

    //Picture send
    private ChatMessageAdapter messageAdapter;
    private List<ChatMessage> chatList;

    private Map<String, Object> locations = new HashMap<>();

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
        if(userID.compareTo(friendID) > 0) {
            dbName  = "ChatMessage_"+userID+"-"+friendID;
        }else {
            dbName = "ChatMessage_"+friendID+"-"+userID;
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
        textBox = (RecyclerView) findViewById(R.id.textBox);
        textBox.setHasFixedSize(true);
        textBox.setLayoutManager(new LinearLayoutManager(this));

        chatList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        dbMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Integer> position_type = new ArrayList<>();
                chatList.clear();

                for(DataSnapshot chatSnapshot : dataSnapshot.getChildren()){
                    ChatMessage chatMessage = chatSnapshot.getValue(ChatMessage.class);
                    position_type.add(chatMessage.getType());
                    chatList.add(chatMessage);
                }

                messageAdapter = new ChatMessageAdapter(chat_nav.this, chatList, friendName, position_type);

                textBox.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(chat_nav.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
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

            ChatMessage messageInfo = new ChatMessage(userID, sdf.format(date), 1, message, "", "", 200, 200);

            dbMessage.child(id).setValue(messageInfo);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(friendID)
                    .update("notifications", "1-0-"+userID);

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
                intent.putExtra("friend_name", friendName);
                intent.putExtra("friend_id", friendID);
                intent.putExtra("database_name", dbName);
                startActivity(intent);
                return true;
            case R.id.chat_location:
                intent.setClass(chat_nav.this, chat_location.class);
                intent.putExtra("friend_name", friendName);
                intent.putExtra("friend_id", friendID);
                intent.putExtra("database_name", dbName);
                startActivity(intent);
                return true;
            case R.id.chat_friend_info:
                intent.setClass(chat_nav.this, profile.class);
                intent.putExtra("friend_name", friendName);
                intent.putExtra("friend_id", friendID);
                intent.putExtra("database_name", dbName);
                intent.putExtra("type", "friends");
                intent.putExtra("UID", friendID);
                startActivity(intent);

                return true;
            case R.id.chat_album:
                intent.setClass(chat_nav.this, chat_album.class);
                intent.putExtra("friend_name", friendName);
                intent.putExtra("friend_id", friendID);
                intent.putExtra("database_name", dbName);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showMap(View view){


        Button locationButton = view.findViewById(R.id.locationButton);
        Intent intent = new Intent();
        intent.setClass(chat_nav.this, map_main.class);



        intent.putExtra("latitude", locationButton.getTag(R.string.latitude).toString());
        intent.putExtra("longitude", locationButton.getTag(R.string.longitude).toString());
        intent.putExtra("location", locationButton.getTag(R.string.location).toString());

        startActivity(intent);

    }
}