package com.example.docportal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.RenderMode;
import com.example.docportal.Doctor.MainActivity;
import com.example.docportal.Patient.patientMainPage;
import com.example.docportal.Pharmacist.PharmacyMainPage;

public class Entrance extends AppCompatActivity {

LottieAnimationView welcome_user;
Button doctor_selection;
Button patient_selection;
Button pharmacist_selection;
ImageView back_to_start;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

       doctor_selection = findViewById(R.id.doctor_selection);
       patient_selection = findViewById(R.id.patient_selection);
       pharmacist_selection = findViewById(R.id.pharmacist_selection);
        back_to_start = findViewById(R.id.back_to_start);
       welcome_user = findViewById(R.id.welcome_user);
       welcome_user.setRenderMode(RenderMode.HARDWARE);

       back_to_start.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Entrance.this, mainstartScreen.class));
           }
       });


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