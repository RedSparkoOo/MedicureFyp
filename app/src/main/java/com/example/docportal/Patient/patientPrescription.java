package com.example.docportal.Patient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.docportal.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PatientPrescription extends AppCompatActivity {


    RecyclerView patient_prescription_recycler;

    patientPrescriptionAdapter prescriptionAdapter;

    List<String> doctor_name;
    List<String> medicine_name;
    List<String> medicine_weight;
    List<String> medicine_usage;
    List<String> prescription_date;
    List<String> final_medicines_stored;
    List<String> doctor_category;
    List<String> Prescription_id;
    String doctor_id;
    String id;

    FirebaseFirestore FStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_prescription);

        patient_prescription_recycler = findViewById(R.id.recieved_prescription_recycler);

        doctor_name = new ArrayList<>();
        medicine_name = new ArrayList<>();
        medicine_weight = new ArrayList<>();
        medicine_usage = new ArrayList<>();
        prescription_date = new ArrayList<>();
        final_medicines_stored = new ArrayList<>();
        doctor_category = new ArrayList<>();
        Prescription_id = new ArrayList<>();


        recievedPrescription();



    }

    private void doctorData(String ID) {

        FStore = FirebaseFirestore.getInstance();
        FStore.collection("Professions").orderBy("Full Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange doc_change : value.getDocumentChanges()){

                    if(doc_change.getType() == DocumentChange.Type.ADDED){
                        String doc_id = doc_change.getDocument().getId();

                        if(doc_id.equals(ID)){

                            doctor_category.add(String.valueOf(doc_change.getDocument().get("Doctor_profession")));
                            patient_prescription_recycler.setLayoutManager(new LinearLayoutManager(PatientPrescription.this));
                            prescriptionAdapter = new patientPrescriptionAdapter(doctor_name,medicine_name,medicine_weight,medicine_usage,prescription_date,doctor_category,Prescription_id);
                            patient_prescription_recycler.setAdapter(prescriptionAdapter);

                        }


                    }
                }
            }
        });

    }

    public void recievedPrescription(){


        FStore = FirebaseFirestore.getInstance();
        FStore.collection("Prescription Sent").orderBy("Patient Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange doc_change : value.getDocumentChanges()){


                    if(doc_change.getType() == DocumentChange.Type.ADDED){
                        String recieved_email = String.valueOf(doc_change.getDocument().get("Patient Email"));
                        doctor_name.add(String.valueOf(doc_change.getDocument().getData().get("Doctor Name")));
                        medicine_name.add(String.valueOf(doc_change.getDocument().getData().get("Medicine Prescribed")));
                        medicine_weight.add(String.valueOf(doc_change.getDocument().getData().get("Medicine Weight")));
                        medicine_usage.add(String.valueOf(doc_change.getDocument().getData().get("Medicine Usage")));
                        prescription_date.add(String.valueOf(doc_change.getDocument().getData().get("Prescription Date")));
                        doctor_id = String.valueOf(doc_change.getDocument().getData().get("Doctor Id"));
                        Prescription_id.add(doc_change.getDocument().getId());

                        FStore.collection("Patient").orderBy("Patient Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for(DocumentChange doc_change : value.getDocumentChanges()){

                                    if(doc_change.getType() == DocumentChange.Type.ADDED){
                                        String email = String.valueOf(doc_change.getDocument().get("Patient Email Address"));

                                        if(email.equals(recieved_email)){
                                            doctorData(doctor_id);


                                        }


                                    }
                                }
                            }
                        });







                    }
                }
            }
        });

    }

}