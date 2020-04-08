package com.example.groupproject;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.groupproject.ChatMessage;
import static com.example.groupproject.appCookies.*;

import java.util.List;

public class MessageList extends ArrayAdapter<ChatMessage> {

    private Activity context;
    private List<ChatMessage> messageList;
    private String friendName;
    private String userName;
    private String friendID;

    public MessageList (Activity context, List<ChatMessage> messageList, String friendName, String friendID){
        super(context, R.layout.message_chat, messageList);
        this.context = context;
        this.messageList = messageList;
        this.friendName = friendName;
        this.friendID = friendID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View textBox = inflater.inflate(R.layout.message_chat, null, true);

        TextView textViewUsername = (TextView) textBox.findViewById(R.id.user_name);
        TextView textViewMessage = (TextView) textBox.findViewById(R.id.message);
        TextView textViewTime = (TextView) textBox.findViewById(R.id.time);

        ChatMessage chatMessage = messageList.get(position);
        String send_name;
        if(userID.equals(chatMessage.getSend_id())){
            send_name = username;
        }else {
            send_name = friendName;
        }

        textViewUsername.setText(send_name);
        textViewMessage.setText(chatMessage.getMessage());
        textViewTime.setText(chatMessage.getTime());

        return textBox;
    }
}
