package com.example.docportal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class videoConsultation extends AppCompatActivity {

    EditText code;
    Button start;
    Button share;
    URL server_url;
    TextView random;
    private final String Letters = "abcdefghijklmnopqrstuvwxyz";
    private final String Number = "0123456789";
    private final char[] AlphaNumeric = (Letters + Letters.toUpperCase() + Number).toCharArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_consultation);


        code = findViewById(R.id.code);
        start = findViewById(R.id.start);
        share = findViewById(R.id.share);
        random = findViewById(R.id.random_number);


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

                JitsiMeetActivity.launch(videoConsultation.this, options);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = generateAlphaNumeric(16);
            random.setText(result);
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