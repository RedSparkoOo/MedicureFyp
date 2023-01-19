package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.docportal.R;



public class patientPrescription extends AppCompatActivity {
EditText patientPrescriptionName;
EditText prescriptionDoctorName;
EditText patientPrescrirptionWritten;
EditText patientPrescriptionDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_prescription);

        patientPrescriptionName = findViewById(R.id.patientPrescriptionName);
        prescriptionDoctorName = findViewById(R.id.prescriptionPatientPhone);
        patientPrescrirptionWritten = findViewById(R.id.patientPrescriptionWritten);
        patientPrescriptionDate = findViewById(R.id.patientPrescriptionDate);


    }
}