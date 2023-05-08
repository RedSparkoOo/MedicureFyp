package com.example.docportal.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.Patient.customerSupport;
import com.example.docportal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddDiseaseData extends AppCompatActivity {
    private TextView disease, symptom, description;
    Spinner organ;
    String organs;
    String[] Organs = {"Heart", "Brain", "Lungs"};
    Button submit;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Object currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_disease_data);
        disease = findViewById(R.id.disease_name);
        symptom = findViewById(R.id.symptom_doctor);
        description = findViewById(R.id.description_doctor);
        submit = findViewById(R.id.submit_disease);
        organ = findViewById(R.id.organ_spinner);

        Object currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        }
        firebaseFirestore = FirebaseFirestore.getInstance();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Organs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        organ.setAdapter(adapter);
        organ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                organs=  organ.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put("organ", organs);
                map.put("symptom", symptom.getText().toString());
                map.put("disease", disease.getText().toString());
                map.put("description", description.getText().toString());

                DocumentReference documentReference = firebaseFirestore.collection("SearchDisease").document(currentUserId.toString());
                documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddDiseaseData.this, "Data inserted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}