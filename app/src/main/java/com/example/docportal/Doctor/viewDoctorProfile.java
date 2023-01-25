package com.example.docportal.Doctor;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.TextView;


import com.example.docportal.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class viewDoctorProfile extends AppCompatActivity {
    TextView doctor_name;
    TextView doctor_specialization;
    TextView doctor_email;
    TextView doctor_phone;
    TextView doctor_license;
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor_profile);

        doctor_name = findViewById(R.id.view_doctor_name);
        doctor_specialization = findViewById(R.id.view_doctor_specialization);
        doctor_email = findViewById(R.id.view_doctor_email);
        doctor_phone = findViewById(R.id.view_doctor_phone);
        doctor_license = findViewById(R.id.view_doctor_license);

        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser doctor = fAuth.getCurrentUser();
        user_id = fAuth.getCurrentUser().getUid();

        if(doctor.isEmailVerified()){

           DocumentReference documentReference = firestore.collection("Doctor").document(user_id);
           documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
               @Override
               public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                   doctor_name.setText(documentSnapshot.getString("Full Name"));
                   doctor_specialization.setText(documentSnapshot.getString("Specialization"));
                   doctor_email.setText(documentSnapshot.getString("Email Address"));
                   doctor_phone.setText(documentSnapshot.getString("Phone #"));
                   doctor_license.setText(documentSnapshot.getString("License #"));
               }
           });

        }





    }
}