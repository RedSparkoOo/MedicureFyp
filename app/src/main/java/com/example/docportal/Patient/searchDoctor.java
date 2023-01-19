package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.docportal.R;

import java.util.ArrayList;
import java.util.List;

public class searchDoctor extends AppCompatActivity {
    RecyclerView searchDoctorRecyclerview;
    List<String> doctorNames;
    List<String> doctorCategory;
    List<String> doctorPhone;
    List<Bitmap> doctorProfile;
    com.example.docportal.Patient.searchDoctorAdapter searchDoctorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);

        searchDoctorRecyclerview = findViewById(R.id.searchDoctorRecycler);
        doctorNames = new ArrayList<>();
        doctorNames.add("Dr. John");
        doctorNames.add("Dr. Wick");
        doctorNames.add("Dr. James");
        doctorNames.add("Dr. Imran");
        doctorNames.add("Dr. Kennedy");
        doctorNames.add("Dr. Wahab");
        doctorNames.add("Dr. Connors");
        doctorNames.add("Dr. Ali");
        doctorNames.add("Dr. Babar");
        doctorNames.add("Dr. Khaild");

        doctorCategory = new ArrayList<>();
        doctorCategory.add("Cardiologist");
        doctorCategory.add("Pedriatican");
        doctorCategory.add("Oncologist");
        doctorCategory.add("Dermatologist");
        doctorCategory.add("Neurologist");
        doctorCategory.add("Nephrologist");
        doctorCategory.add("Physiologist");
        doctorCategory.add("Physician");
        doctorCategory.add("Anesthesiologists");
        doctorCategory.add("Hematologists");

        doctorPhone = new ArrayList<>();
        doctorPhone.add("0123-45678911");
        doctorPhone.add("0223-46678912");
        doctorPhone.add("0323-47678913");
        doctorPhone.add("0423-48678914");
        doctorPhone.add("0523-49678915");
        doctorPhone.add("0623-41078916");
        doctorPhone.add("0723-41178917");
        doctorPhone.add("0823-41278918");
        doctorPhone.add("0923-41378919");
        doctorPhone.add("0103-41478910");



        searchDoctorRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        searchDoctorAdapter = new searchDoctorAdapter(doctorNames,doctorCategory, doctorPhone);
        searchDoctorRecyclerview.setAdapter(searchDoctorAdapter);




    }
}