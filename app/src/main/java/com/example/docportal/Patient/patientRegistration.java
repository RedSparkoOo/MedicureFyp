package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.R;

public class patientRegistration extends AppCompatActivity {
    EditText _patientFirstName;
    EditText _patientLastName;
    EditText _patientEmailAddress;
    EditText _patientPassword;
    EditText _patientCnic;
    EditText _patientDateOfBith;

    RadioGroup _patientGender;

    Button _registerInfo;

    ImageView patient_BackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        // hooks passing (assigning Id);
        _patientFirstName = findViewById(R.id.patientFirstName);
        _patientLastName = findViewById(R.id.patientLastName);
        _patientEmailAddress = findViewById(R.id.patientEmailAddress);
        _patientPassword = findViewById(R.id.doctorPassword);
        _patientCnic = findViewById(R.id.patientCnic);
        _patientDateOfBith = findViewById(R.id.patientDateOfBirth);

        _patientGender = findViewById(R.id.patientGender);

        _registerInfo = findViewById(R.id.registerInfo);


        _registerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(patientRegistration.this, patientOTPVerify.class);
                startActivity(intent);
                Toast.makeText(patientRegistration.this, "Working!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}