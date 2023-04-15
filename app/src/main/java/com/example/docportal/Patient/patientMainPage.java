package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.docportal.Doctor.DocLogin;
import com.example.docportal.Doctor.Registeration;
import com.example.docportal.R;

public class patientMainPage extends AppCompatActivity {
Button _patientSignup;
Button _patientLogin;

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
                Intent intent = new Intent(patientMainPage.this, DocLogin.class);
                startActivity(intent);

            }
        });
    }
}