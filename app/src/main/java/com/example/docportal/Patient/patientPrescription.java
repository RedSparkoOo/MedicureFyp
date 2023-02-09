package com.example.docportal.Patient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.docportal.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class patientPrescription extends AppCompatActivity {


    RecyclerView patient_prescription_recycler;

    patientPrescriptionAdapter prescriptionAdapter;

    List<String> doctor_name;
    List<String> medicine_name;
    List<String> medicine_usage;
    List<String> prescription_date;
    List<String> final_medicines_stored;

    String[] arr;
    FirebaseFirestore FStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_prescription);

        patient_prescription_recycler = findViewById(R.id.recieved_prescription_recycler);

        doctor_name = new ArrayList<>();
        medicine_name = new ArrayList<>();
        medicine_usage = new ArrayList<>();
        prescription_date = new ArrayList<>();
        final_medicines_stored = new ArrayList<>();

        doctor_name.clear();
        medicine_name.clear();
        medicine_usage.clear();
        prescription_date.clear();
        recievedPrescription();

    }

    public void recievedPrescription(){


        FStore = FirebaseFirestore.getInstance();
        FStore.collection("Prescriptions Sent").orderBy("Prescribed Patient Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange doc_change : value.getDocumentChanges()){

                    if(doc_change.getType() == DocumentChange.Type.ADDED){
                        String id = doc_change.getDocument().getId();
                        String meds_recieved;
                        doctor_name.add(String.valueOf(doc_change.getDocument().get("Prescribing Doctor Name")));

                        meds_recieved = String.valueOf(doc_change.getDocument().get("Medicine Name"));
                        String[] cut = meds_recieved.split(",");
                        medicine_name.add(String.valueOf(cut));

                        medicine_usage.add(String.valueOf(doc_change.getDocument().get("Medicine Usage")));
                        prescription_date.add(String.valueOf(doc_change.getDocument().get("Medicine Weight")));


                        patient_prescription_recycler.setLayoutManager(new LinearLayoutManager(patientPrescription.this));
                        prescriptionAdapter = new patientPrescriptionAdapter(doctor_name,medicine_name,medicine_usage,prescription_date);
                        patient_prescription_recycler.setAdapter(prescriptionAdapter);






                    }
                }
            }
        });




    }
}