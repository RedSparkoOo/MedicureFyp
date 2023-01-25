package com.example.docportal.Doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.docportal.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class checkAppointment extends AppCompatActivity {


    RecyclerView patient_appointment_recycler_view;

    List<String> patient_name;
    List<String> patient_phone;
    List<String> patient_date;
    List<String> patient_time;
    List<String> patient_description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_appointment);
        //------------------Assigning Hooks------------------------------------


        Bundle bundle = getIntent().getExtras();


          patient_name = new ArrayList<>();
          patient_name.add("Wasiq Fayyaz");
          patient_name.add("Wasiq Fayyaz");
          patient_name.add("Wasiq Fayyaz");

          patient_phone = new ArrayList<>();
          patient_phone.add("0321-6543210");
          patient_phone.add("0321-6543210");
          patient_phone.add("0321-6543210");

          patient_date = new ArrayList<>();
          patient_date.add("07/07/2022");
          patient_date.add("07/07/2022");
          patient_date.add("07/07/2022");

          patient_time = new ArrayList<>();
          patient_time.add("6:30 pm");
          patient_time.add("7:30 pm");
          patient_time.add("8:30 pm");

          patient_description = new ArrayList<>();
          patient_description.add("Hi, Good Morning, My Back Hurts!");
          patient_description.add("Hi, Good AfterNoon, My Stomach Hurts!");
          patient_description.add("Hi, Good Evening, My head hurts!");

          patient_appointment_recycler_view = findViewById(R.id.patient_appointment_recycler);
          patient_appointment_recycler_view.setLayoutManager(new LinearLayoutManager(checkAppointment.this));
          checkAppointmentAdapter checkAppointmentAdapter = new checkAppointmentAdapter(patient_name,patient_phone,patient_date,patient_time,patient_description);
          patient_appointment_recycler_view.setAdapter(checkAppointmentAdapter);

    }
}