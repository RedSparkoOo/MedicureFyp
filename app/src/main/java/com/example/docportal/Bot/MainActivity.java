package com.example.docportal.Bot;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
RecyclerView chat_recycler;
EditText write_msg;
FloatingActionButton send_msg;
private final String BOT_key = "bot";
private final String USER_key = "user";
private ArrayList<ChatsModel> chatsModelArrayList;
private  ChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        chat_recycler = findViewById(R.id.Chat);
        write_msg = findViewById(R.id.Msg);
        send_msg = findViewById(R.id.SendMsg);

        chatsModelArrayList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatsModelArrayList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chat_recycler.setLayoutManager(manager);
        chat_recycler.setAdapter(chatAdapter);

        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(write_msg.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter a msg", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(write_msg.getText().toString());
                write_msg.setText("");
            }
        });

    }

    private void getResponse(String msg) {
        chatsModelArrayList.add(new ChatsModel(msg, USER_key));
        chatAdapter.notifyDataSetChanged();
        String url = "http://api.brainshop.ai/get?bid=174794&key=TAhXHumRxCdtOUf9&uid=uid&msg="+msg;
        String BASE_url = "http://api.brainshop.ai/";
        Retrofit retroFit = new Retrofit.Builder()
                .baseUrl(BASE_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetroFit fit = retroFit.create(RetroFit.class);
        Call<MsgModel> call = fit.getMessage(url);
        call.enqueue(new Callback<MsgModel>() {
            @Override
            public void onResponse(Call<MsgModel> call,Response<MsgModel> response) {
                MsgModel model = response.body();


                    chatsModelArrayList.add(new ChatsModel(model.getCnt(),BOT_key));
                    chatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<MsgModel> call, Throwable t) {
              chatsModelArrayList.add(new ChatsModel("Please Revert your question",BOT_key));
                chatAdapter.notifyDataSetChanged();

            }
        });


    }
}