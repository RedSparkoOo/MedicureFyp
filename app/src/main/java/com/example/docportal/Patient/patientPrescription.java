package com.example.docportal.Patient;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class patientPrescription extends AppCompatActivity {


    RecyclerView patient_prescription_recycler;

    patientPrescriptionAdapter prescriptionAdapter;

    List<String> doctor_name;
    List<String> medicine_name;
    List<String> medicine_weight;
    List<String> medicine_usage;
    List<String> prescription_date;
    List<String> final_medicines_stored;

    String id;


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


        recievedPrescription();

    }

    public void recievedPrescription() {
        FirestoreHandler firestoreHandler = new FirestoreHandler();

        firestoreHandler.getFirestoreInstance().collection("Prescriptions Sent").orderBy("Prescribed Patient Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange doc_change : value.getDocumentChanges()) {

                    if (doc_change.getType() == DocumentChange.Type.ADDED) {


                        doctor_name = (List<String>) doc_change.getDocument().getData().get("Prescribed Doctor Name");
                        medicine_name = (List<String>) doc_change.getDocument().getData().get("Medicines Prescribed");
                        medicine_weight = (List<String>) doc_change.getDocument().getData().get("Medicines Prescribed Weight");
                        medicine_usage = (List<String>) doc_change.getDocument().getData().get("Medicines Prescribed Usage");
                        prescription_date = (List<String>) doc_change.getDocument().getData().get("Prescription Date");
                        id = doc_change.getDocument().getId();
                        patient_prescription_recycler.setLayoutManager(new LinearLayoutManager(patientPrescription.this));
                        prescriptionAdapter = new patientPrescriptionAdapter(doctor_name, medicine_name, medicine_weight, medicine_usage, prescription_date, id);
                        patient_prescription_recycler.setAdapter(prescriptionAdapter);


                    }
                }
            }
        });

    }

}