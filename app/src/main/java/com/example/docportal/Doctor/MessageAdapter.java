package com.example.docportal.Doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.utils.Utils;
import com.example.docportal.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private Context context;
    FirebaseAuth firebaseAuth;
    String currentUserId;
    Object currentUser;
    public MessageAdapter(Context context) {
        this.context = context;
        messageModels = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = (MessageModel) messageModels.get(position);

        if (message.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    private List<MessageModel> messageModels;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_recycler, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_recycler2, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = messageModels.get(position);
        switch (holder.getItemViewType()) {

            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(messageModel);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(messageModel);
    }






    }

    public void clear() {
        messageModels.clear();
        notifyDataSetChanged();
    }

    public void add(MessageModel messageModel) {
        messageModels.add(messageModel);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }


    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, dateText;

        SentMessageHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.text_gchat_date_me);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
        }

        void bind(MessageModel message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getTime());
            dateText.setText(message.getDate());
            // Format the stored timestamp into a readable String using method.

        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText, dateText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.text_gchat_date_other);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
            profileImage = (ImageView) itemView.findViewById(R.id.image_gchat_profile_other);
        }

        void bind(MessageModel message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());
            dateText.setText(message.getDate());


            nameText.setText(message.getName());

            // Insert the profile image from the URL into the ImageView.
            //  Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }
}