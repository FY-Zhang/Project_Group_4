package com.example.groupproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.groupproject.appCookies.userID;
import static com.example.groupproject.appCookies.username;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ImageViewHolder> {

    private Context context;
    private List<ChatMessage> chatMessages;
    private String friendName;

    public ChatMessageAdapter(Context context, List<ChatMessage> chatMessages, String friendName){
        this.context = context;
        this.chatMessages = chatMessages;
        this.friendName = friendName;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_message, parent, false);
        return new ImageViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ChatMessage chatMessageCurrent = chatMessages.get(position);
        String send_name;
        if(chatMessageCurrent.getSend_id().equals(userID)){
            send_name = new String(username);
        }else {
            send_name = new String(friendName);
        }
        holder.sendName.setText(send_name);
        holder.time.setText(chatMessageCurrent.getTime());
        holder.message.setText(chatMessageCurrent.getMessage());
        if (chatMessageCurrent.getType() == 0) {
            Picasso.with(context)
                    .load(chatMessageCurrent.getImgUrl())
                    .into(holder.imageView);
        }else {
            Picasso.with(context)
                    .load("https://firebasestorage.googleapis.com/v0/b/groupproject-ffdc4.appspot.com/o/chat_images%2FdefaultImage.png?alt=media&token=d78422b1-313f-4c50-a791-a29fdde9b9b5")
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {

        return chatMessages.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView sendName;
        public TextView time;
        public ImageView imageView;
        public TextView message;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            sendName = itemView.findViewById(R.id.sendName);
            time = itemView.findViewById(R.id.sendTime);
            imageView = itemView.findViewById(R.id.sendPicture);
            message = itemView.findViewById(R.id.sendMessage);
        }
    }
}
