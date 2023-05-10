package com.example.docportal.Doctor;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.R;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class OnlineConsultation extends AppCompatActivity {

    EditText code;
    Button start;
    URL server_url;
    ImageView copy_text;
    private final String Letters = "abcdefghijklmnopqrstuvwxyz";
    private final String Number = "0123456789";
    private final char[] AlphaNumeric = (Letters + Letters.toUpperCase() + Number).toCharArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_consultation);


        code = findViewById(R.id.code);
        start = findViewById(R.id.start);
        copy_text = findViewById(R.id.copy_text);
        code.setFocusable(false);


        try {
            server_url = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions OpConferenceOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(server_url)

                    .build();
            JitsiMeet.setDefaultConferenceOptions(OpConferenceOptions);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(code.getText().toString())

                        .build();

                JitsiMeetActivity.launch(OnlineConsultation.this, options);
            }
        });


        String result = generateAlphaNumeric(16);
        code.setText(result);
        copy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData Data = ClipData.newPlainText("Copy",code.getText().toString());
                manager.setPrimaryClip(Data);

            }
        });


    }

    public String generateAlphaNumeric(int length) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i<length; i++){
            builder.append(AlphaNumeric[new Random().nextInt(AlphaNumeric.length)]);
        }
        return builder.toString();
    }


}