package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.List;


public class patientPrescription extends AppCompatActivity {


    RecyclerView patient_prescription_recycler;

    patientPrescriptionAdapter prescriptionAdapter;

    List<String> doctor_name;
    List<String> medicine_name;
    List<String> medicine_usage;
    List<String> prescription_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_prescription);

        patient_prescription_recycler = findViewById(R.id.recieved_prescription_recycler);

        doctor_name = new ArrayList<>();
        medicine_name = new ArrayList<>();
        medicine_usage = new ArrayList<>();
        prescription_date = new ArrayList<>();

        doctor_name.add("Wasiq");
        medicine_name.add("Panadol");
        medicine_usage.add("Day and night");
        prescription_date.add("8th February");


        patient_prescription_recycler.setLayoutManager(new LinearLayoutManager(this));
        prescriptionAdapter = new patientPrescriptionAdapter(doctor_name,medicine_name,medicine_usage,prescription_date);
        patient_prescription_recycler.setAdapter(prescriptionAdapter);




    }
}