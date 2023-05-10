package com.example.docportal.Patient;

import static com.example.docportal.R.layout.spinner_item;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.List;

public class viewMedicalRecord extends AppCompatActivity {
    RecyclerView medicalRecordRecycler;
    List<String> fileName;
    medicalRecordAdapter medicalRecordAdapter;

    Spinner viewMedicalRecordCategory;

    String[] recordCategory = {"","Medicine","Medical Equipment","Appointments"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medical_record);

        medicalRecordRecycler = findViewById(R.id.medicalRecordRecycler);
        viewMedicalRecordCategory = findViewById(R.id.viewMedicalCategory);

        ArrayAdapter arrayAdapterRecord = new ArrayAdapter(this, spinner_item,recordCategory);
        arrayAdapterRecord.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewMedicalRecordCategory.setAdapter(arrayAdapterRecord);


        fileName = new ArrayList<>();
        fileName.add("Appointment booked at 5:30 pm");
        fileName.add("Appointment booked at 6:30 pm");
        fileName.add("Appointment booked at 7:30 pm");
        fileName.add("Medicine bought with 30 Rs");
        fileName.add("Medicine bought with 30 Rs");
        fileName.add("Medicine bought with 30 Rs");
        fileName.add("Medicine bought with 30 Rs");
        fileName.add("Dengue Test booked at 3:50 pm");
        fileName.add("Dengue Test booked at 4:50 pm");
        fileName.add("Dengue Test booked at 5:50 pm");





        medicalRecordRecycler.setLayoutManager(new LinearLayoutManager(this));
        medicalRecordAdapter = new medicalRecordAdapter(fileName);
        medicalRecordRecycler.setAdapter(medicalRecordAdapter);


    }
}