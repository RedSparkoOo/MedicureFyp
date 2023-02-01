package com.example.docportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.RenderMode;
import com.example.docportal.Doctor.MainActivity;
import com.example.docportal.Patient.patientMainPage;
import com.example.docportal.Pharmacist.PharmacyMainPage;
import com.google.android.material.snackbar.Snackbar;

public class Entrance extends AppCompatActivity {

LottieAnimationView welcome_user;
Button doctor_selection;
Button patient_selection;
Button pharmacist_selection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

       doctor_selection = findViewById(R.id.doctor_selection);
       patient_selection = findViewById(R.id.patient_selection);
       pharmacist_selection = findViewById(R.id.pharmacist_selection);
       welcome_user = findViewById(R.id.welcome_user);
       welcome_user.setRenderMode(RenderMode.HARDWARE);


        doctor_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Entrance.this, MainActivity.class));
            }
        });

        patient_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Entrance.this, patientMainPage.class));

            }
        });

        pharmacist_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Entrance.this, PharmacyMainPage.class));

            }
        });

    }
}