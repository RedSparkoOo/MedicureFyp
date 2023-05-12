package com.example.docportal.Patient;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.Entrance;
import com.example.docportal.R;
import com.example.docportal.Singleton;

public class patientMainPage extends AppCompatActivity {
    Button _patientSignup;
    Button _patientLogin;
    ImageView back_to_entrance;
    Singleton singleton = new Singleton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main_page);

        _patientLogin = findViewById(R.id.patientLogin);
        _patientSignup = findViewById(R.id.patientSignUp);
        back_to_entrance = findViewById(R.id.back_to_entrance);

        back_to_entrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(patientMainPage.this, Entrance.class);
            }
        });


        _patientSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                singleton.openActivity(patientMainPage.this, PatientRegistration.class);


            }
        });

        _patientLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(patientMainPage.this, PatientLogin.class);

            }
        });
    }
}