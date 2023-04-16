package com.example.docportal.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.Doctor.DocLogin;
import com.example.docportal.R;

public class patientMainPage extends AppCompatActivity {
    Button _patientSignup;
    Button _patientLogin;

    Bundle patient;
    boolean patient_check = true;
    boolean doctor_check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main_page);

        _patientLogin = findViewById(R.id.patientLogin);
        _patientSignup = findViewById(R.id.patientSignUp);


        _patientSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent = new Intent(patientMainPage.this, patientRegistration.class);
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
                patient = new Bundle();
                patient.putBoolean("patient_check",patient_check);
                patient.putBoolean("doctor_check",doctor_check);
                intent.putExtras(patient);
                startActivity(intent);
            }
        });
    }
}