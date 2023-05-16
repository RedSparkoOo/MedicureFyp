package com.example.docportal.Doctor;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;

public class
AddDiseaseData extends AppCompatActivity {
    Spinner organ;
    String organs;
    String[] Organs = {"Heart", "Brain", "Lungs"};
    Button submit;


    ImageView back_to_doctorDashboard;
    Singleton singleton = new Singleton();
    private TextView disease, symptom, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_disease_data);
        disease = findViewById(R.id.disease_name);
        symptom = findViewById(R.id.symptom_doctor);
        description = findViewById(R.id.description_doctor);
        submit = findViewById(R.id.submit_disease);
        organ = findViewById(R.id.organ_spinner);
        back_to_doctorDashboard = findViewById(R.id.back_to_doctorDashboard);
        back_to_doctorDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(AddDiseaseData.this, DoctorNurseDashboard.class);
            }
        });
        singleton.setAdatper(getApplicationContext(), organ, Organs);

        organ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                organs = organ.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirestoreHandler firestoreHandler = new FirestoreHandler();
                HashMap<String, String> map = new HashMap<>();
                map.put("organ", organs);
                map.put("symptom", symptom.getText().toString());
                map.put("disease", disease.getText().toString());
                map.put("description", description.getText().toString());

                DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("SearchDisease").document(firestoreHandler.getCurrentUser());
                documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        singleton.showToast(AddDiseaseData.this, "Data inserted");
                    }
                });
            }
        });

    }
}