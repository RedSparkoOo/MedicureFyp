package com.example.docportal.Doctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private final Context context;
    private final List<MessageModel> messageModels;
    FirestoreHandler firestoreHandler = new FirestoreHandler();

    public MessageAdapter(Context context) {
        this.context = context;
        messageModels = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messageModels.get(position);

        if (message.getSenderId().equals(firestoreHandler.getCurrentUser())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

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
            messageText = itemView.findViewById(R.id.text_gchat_message_me);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_me);
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
            messageText = itemView.findViewById(R.id.text_gchat_message_other);
            timeText = itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = itemView.findViewById(R.id.text_gchat_user_other);
            profileImage = itemView.findViewById(R.id.image_gchat_profile_other);
        }

        void bind(MessageModel message) {
            messageText.setText(message.getMessage());
            Intent intent = ((Activity) context).getIntent();

            Bundle bundle = intent.getBundleExtra("mBundle");
            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());
            dateText.setText(message.getDate());


            nameText.setText(bundle.getString("names"));

            // Insert the profile image from the URL into the ImageView.
            //  Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }
}