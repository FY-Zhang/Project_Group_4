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

import java.util.List;

public class MessageList extends ArrayAdapter<ChatMessage> {

    private Activity context;
    private List<ChatMessage> messageList;

    public MessageList (Activity context, List<ChatMessage> messageList){
        super(context, R.layout.message_chat, messageList);
        this.context = context;
        this.messageList = messageList;
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

        textViewUsername.setText(chatMessage.getSend_id());
        textViewMessage.setText(chatMessage.getMessage());
        textViewTime.setText(chatMessage.getTime());

        return textBox;
    }
}
