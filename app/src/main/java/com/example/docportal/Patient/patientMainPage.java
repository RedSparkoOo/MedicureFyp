package com.example.docportal.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.Entrance;
import com.example.docportal.R;

public class patientMainPage extends AppCompatActivity {
    Button _patientSignup;
    Button _patientLogin;
    ImageView back_to_entrance;
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
                Intent intent = new Intent(patientMainPage.this, Entrance.class);
                startActivity(intent);
            }
        });


        _patientSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(patientMainPage.this, PatientRegistration.class);
                    startActivity(intent);
                }
                catch (Exception e){
                    Toast.makeText(patientMainPage.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        _patientLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(patientMainPage.this, PatientLogin.class);
                startActivity(intent);
            }
        });
    }
}