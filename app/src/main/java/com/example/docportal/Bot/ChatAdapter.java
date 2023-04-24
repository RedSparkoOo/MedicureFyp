package com.example.docportal.Bot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    private ArrayList<ChatsModel> chatsModelsArray;
    private Context context;

    public ChatAdapter(ArrayList<ChatsModel> chatsModelsArray, Context context) {
        this.chatsModelsArray = chatsModelsArray;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg,parent,false);
                return new UserViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg,parent,false);
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatsModel chatsModel = chatsModelsArray.get(position);
        switch (chatsModel.getSender()){
            case "user":
                ((UserViewHolder)holder).UserTV.setText(chatsModel.getMessage());
                break;
            case "bot":
                ((BotViewHolder)holder).BotTV.setText(chatsModel.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (chatsModelsArray.get(position).getSender()){
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return chatsModelsArray.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView UserTV;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            UserTV = itemView.findViewById(R.id.UserChat);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView BotTV;
        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            BotTV = itemView.findViewById(R.id.BotChat);
        }
    }
}
