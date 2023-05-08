package com.example.docportal.Patient;

import static java.lang.Boolean.valueOf;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class searchDisease extends AppCompatActivity {
    String[] diseases = {"Heart", "Brain", "Lungs"};
    ArrayList<String> Brain = new ArrayList<>();
    ArrayList<String> Lungs = new ArrayList<>();
    ArrayList<String> Heart = new ArrayList<>();

    RecyclerView symptomR;
    Spinner organ;
    String disease;

    String symptom;
    Spinner symptoms;

    RecyclerView doctor_profile_recycler;

    ArrayList<String> doctor_names = new ArrayList<>();;
    ArrayList<String> doctor_specializations = new ArrayList<>();;
    ArrayList<String> doctor_UID = new ArrayList<>();;
    ArrayList<String> doctor_phone_no = new ArrayList<>();;


    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    bookAppointmentHelperClass book_appointment_helper_class;

    SearchDiseaseAdapter searchDiseaseAdapter;

    CollectionReference noteBookref = firestore.collection("SearchDisease");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_disease);
        organ = findViewById(R.id.spinner_disease);
        symptoms = findViewById(R.id.spinner_symptom);
        firestore = FirebaseFirestore.getInstance();
        doctor_profile_recycler = findViewById(R.id.spinner_doctor);

        doctor_profile_recycler.setLayoutManager(new LinearLayoutManager(searchDisease.this));
        book_appointment_helper_class = new bookAppointmentHelperClass(doctor_names, doctor_specializations, doctor_UID, doctor_phone_no,new bookAppointmentHelperClass.ItemClickListener() {
            @Override
            public void onItemClick(String details) {
                Log.d(details,"Works");
            }
        });
        doctor_profile_recycler.setAdapter(book_appointment_helper_class);
        setUpRecycler ();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, diseases);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        organ.setAdapter(adapter);
        organ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                disease = adapterView.getItemAtPosition(i).toString();

                if(disease != null) {
                    if (disease.equals("Lungs"))
                        checkDisease("Hospital pharmacist");
                    else if (disease.equals("Brain"))
                        checkDisease("Neurologist");
                    else if (disease.equals("Heart"))
                        checkDisease("Nurse");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        symptoms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                symptom = parent.getItemAtPosition(position).toString();
                setUpRecycler();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }
    private void checkDisease(String doctor){
        Brain.clear();
        Lungs.clear();
        Heart.clear();
        symptoms.setAdapter(null);
        symptomR.setAdapter(null);
        fetchData();
        doctor_names.clear();
        doctor_specializations.clear();
        doctor_UID.clear();
        doctor_phone_no.clear();
        FireStoreUsersSpecific(doctor);
    }
    private void setUpRecycler () {
        System.out.println(symptom);

        Query query = noteBookref.whereEqualTo("symptom", symptom);
        FirestoreRecyclerOptions<DiseaseModel> options = new FirestoreRecyclerOptions.Builder<DiseaseModel>()
                .setQuery(query, DiseaseModel.class).build();
        searchDiseaseAdapter = new SearchDiseaseAdapter(options);
        symptomR = findViewById(R.id.spinner_recycler);

        symptomR.setLayoutManager(new WrapContentLinearLayoutManager(searchDisease.this, LinearLayoutManager.VERTICAL, false));
        symptomR.setAdapter(searchDiseaseAdapter);
        searchDiseaseAdapter.startListening();


    }
    public void fetchData() {
            firestore.collection("SearchDisease").orderBy("organ", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {

                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null)
                        Toast.makeText(searchDisease.this, error.toString(), Toast.LENGTH_SHORT).show();
                    for (DocumentChange dc : value.getDocumentChanges()) {

                        if (disease.equals("Brain") && dc.getDocument().get("organ").equals("Brain") && !Brain.contains(dc.getDocument().get("symptom"))) {
                                Brain.add(String.valueOf(dc.getDocument().get("symptom")));
                            setAdapter(Brain);
                        } else if (disease.equals("Heart") && dc.getDocument().get("organ").equals("Heart") && !Heart.contains(dc.getDocument().get("symptom"))) {
                                Heart.add(String.valueOf(dc.getDocument().get("symptom")));
                            setAdapter(Heart);
                        } else if (disease.equals("Lungs") && dc.getDocument().get("organ").equals("Lungs") && !Lungs.contains(dc.getDocument().get("symptom"))) {
                                Lungs.add(String.valueOf(dc.getDocument().get("symptom")));
                            setAdapter(Lungs);
                        }
                    }
                        book_appointment_helper_class.notifyDataSetChanged();
                    }
                });
            }
            private void setAdapter(List<String> list){

                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                symptoms.setAdapter(adapter2);
            }

    @Override
    protected void onStart() {
        super.onStart();
        searchDiseaseAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        searchDiseaseAdapter.stopListening();
    }

    public void FireStoreUsersSpecific(String Category){
        firestore.collection("Professions").orderBy("Full Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange documentChange : value.getDocumentChanges()){
                    if(documentChange.getType() == DocumentChange.Type.ADDED){

                        String category = String.valueOf(documentChange.getDocument().get("Profession"));
                        String Specialization = String.valueOf(documentChange.getDocument().get("Doctor_profession"));

                        if(category.equals("Doctor")){

                            if(Specialization.equals(Category)){
                                doctor_names.add(String.valueOf(documentChange.getDocument().get("Full Name")));
                                doctor_specializations.add(String.valueOf(documentChange.getDocument().get("Doctor_profession")));
                                doctor_UID.add(documentChange.getDocument().getId());
                                doctor_phone_no.add(String.valueOf(documentChange.getDocument().get("Phone #")));
                            }
                        }

                        book_appointment_helper_class.notifyDataSetChanged();

                    }

                }
            }
        });
    }

}