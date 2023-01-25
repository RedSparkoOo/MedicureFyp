package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.docportal.R;

public class patientViewProfile extends AppCompatActivity {
TextView _viewPatientName;
TextView _viewPatientEmail;
TextView _viewPatientPassword;
TextView _viewPatientPhone;
TextView _viewPatientCnic;
TextView _viewPatientGender;
TextView _viewPatientDob;

ImageView _viewPatientProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_profile);


        // Assignign Ids Hooks

        _viewPatientName = findViewById(R.id.viewPatientName);
        _viewPatientEmail = findViewById(R.id.viewPatientEmail);
        _viewPatientPassword = findViewById(R.id.viewPatientPassword);
        _viewPatientPhone = findViewById(R.id.viewPatientPhone);
        _viewPatientCnic = findViewById(R.id.viewPatientCnic);
        _viewPatientGender = findViewById(R.id.viewPatientGender);
        _viewPatientDob = findViewById(R.id.viewPatientDob);

        _viewPatientProfilePic = findViewById(R.id.viewPatientPicture);


    }
}