package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.docportal.R;

public class patientLogin extends AppCompatActivity {
 EditText _patientPhone;
 EditText _patientPassword;

 Button _patientLogin;

 TextView patientForgetPassword;
 TextView patientRegistration;

 ImageView patientLoginBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);

        // hooks id assigned
        _patientPhone =findViewById(R.id.patientPhone);
        _patientPassword =findViewById(R.id.doctorPassword);

        _patientLogin = findViewById(R.id.patientLogin);
        patientRegistration = findViewById(R.id.patientRegistration);

        patientRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientLogin.this, patientRegistration.class);
                startActivity(intent);
            }
        });

        patientForgetPassword = findViewById(R.id.patientforgetPassword);
        patientForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientLogin.this, patientOTPVerify.class);
                startActivity(intent);
            }
        });
        //On click listener to go to Dashboard;
        _patientLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(patientLogin.this,patientDashboard.class);
                startActivity(intent);
            }
        });



    }
}