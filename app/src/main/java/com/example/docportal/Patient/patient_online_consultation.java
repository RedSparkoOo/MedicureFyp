package com.example.docportal.Patient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.R;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class patient_online_consultation extends AppCompatActivity {
EditText rec_code;
Button rec_start;
URL rec_server_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_online_consultation);

        rec_code = findViewById(R.id.rec_code);
        rec_start = findViewById(R.id.rec_start);

        try {
            rec_server_url = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions OpConferenceOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(rec_server_url)

                    .build();
            JitsiMeet.setDefaultConferenceOptions(OpConferenceOptions);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        rec_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rec_code.length() < 16){
                    Toast.makeText(patient_online_consultation.this, "Incorrect code", Toast.LENGTH_SHORT).show();
                }
                else {

                    JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(rec_code.getText().toString())

                            .build();

                    JitsiMeetActivity.launch(patient_online_consultation.this, options);
                }


            }
        });

    }
}