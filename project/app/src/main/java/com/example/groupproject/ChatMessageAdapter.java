package com.example.groupproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.groupproject.appCookies.userID;
import static com.example.groupproject.appCookies.username;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int IMAGE_MESSAGE = 0;
    private int TEXT_MESSAGE = 1;
    private int LOCATION_MESSAGE = 2;

    private Context context;
    private List<ChatMessage> chatMessages;
    private String friendName;
    private ArrayList<Integer> position_type = new ArrayList<>();

    public ChatMessageAdapter(Context context, List<ChatMessage> chatMessages, String friendName, ArrayList<Integer> position_type){
        this.context = context;
        this.chatMessages = chatMessages;
        this.friendName = friendName;
        this.position_type = position_type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == TEXT_MESSAGE){
            view = getView(R.layout.chat_message);
            MessageViewHolder messageViewHolder = new MessageViewHolder(view);
            return messageViewHolder;
        }else if(viewType == IMAGE_MESSAGE){
            view = getView(R.layout.chat_image);
            ImageViewHolder imageViewHolder = new ImageViewHolder(view);
            return imageViewHolder;
        }else {
            view = getView(R.layout.chat_location);
            LocationViewHolder locationViewHolder = new LocationViewHolder(view);
            return locationViewHolder;
        }
    }

    private View getView(int view) {
        View view1 = View.inflate(context, view, null);
        return view1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String send_name;
        ChatMessage chatMessageCurrent = chatMessages.get(position);
        if(chatMessageCurrent.getSend_id().equals(userID)){
            send_name = new String(username);
        }else {
            send_name = new String(friendName);
        }

        if(holder instanceof ImageViewHolder){
            ImageViewHolder imageViewHolder = (ImageViewHolder)holder;

            imageViewHolder.sendName.setText(send_name);
            imageViewHolder.time.setText(chatMessageCurrent.getTime());
            Picasso.with(context)
                    .load(chatMessageCurrent.getImgUrl())
                    .into(imageViewHolder.imageView);
        }else if(holder instanceof MessageViewHolder){
            MessageViewHolder messageViewHolder = (MessageViewHolder)holder;

            messageViewHolder.sendName.setText(send_name);
            messageViewHolder.time.setText(chatMessageCurrent.getTime());
            messageViewHolder.message.setText(chatMessageCurrent.getMessage());

        }else if(holder instanceof LocationViewHolder){
            LocationViewHolder locationViewHolder = (LocationViewHolder)holder;

            locationViewHolder.sendName.setText(send_name);
            locationViewHolder.time.setText(chatMessageCurrent.getTime());

            locationViewHolder.button.setText(chatMessageCurrent.getLocation());
            locationViewHolder.button.setTag(R.string.latitude, chatMessageCurrent.getLatitude());
            locationViewHolder.button.setTag(R.string.longitude, chatMessageCurrent.getLongitude());
            locationViewHolder.button.setTag(R.string.location, chatMessageCurrent.getLocation());
        }
    }

    @Override
    public int getItemCount() {

        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position_type.get(position);
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView sendName;
        public TextView time;
        public ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            sendName = itemView.findViewById(R.id.sendName);
            time = itemView.findViewById(R.id.sendTime);
            imageView = itemView.findViewById(R.id.sendPicture);
            imageView.setAdjustViewBounds(true);
        }
    }
    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView sendName;
        public TextView time;
        public TextView message;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            sendName = itemView.findViewById(R.id.sendName);
            time = itemView.findViewById(R.id.sendTime);
            message = itemView.findViewById(R.id.sendMessage);
        }
    }
    public static class LocationViewHolder extends RecyclerView.ViewHolder{
        public TextView sendName;
        public TextView time;
        public Button button;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            sendName = itemView.findViewById(R.id.sendName);
            time = itemView.findViewById(R.id.sendTime);
            button = itemView.findViewById(R.id.locationButton);
        }
    }
}
