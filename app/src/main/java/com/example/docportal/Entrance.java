package com.example.docportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.docportal.Doctor.MainActivity;
import com.example.docportal.Patient.patientMainPage;

public class Entrance extends AppCompatActivity {
ImageView _doctor;
ImageView _patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        _doctor = findViewById(R.id.doctor);
        _patient = findViewById(R.id.patient);


        _doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Entrance.this, MainActivity.class));
            }
        });

        _patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Entrance.this, patientMainPage.class));
            }
        });

    }
}