package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.docportal.R;

public class patientOTPVerify extends AppCompatActivity {
EditText _patientOTP;
Button _patientOTPVerify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_otpverify);

        //assigning ids (hooks);

        _patientOTP = findViewById(R.id.patientOTP);
        _patientOTPVerify = findViewById(R.id.patientVerify);

        // On click listener to go to Login

        _patientOTPVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(patientOTPVerify.this, patientLogin.class);
                startActivity(intent);
            }
        });


    }
}