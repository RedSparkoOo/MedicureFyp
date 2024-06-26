package com.example.docportal.Patient;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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

    ArrayList<String> doctor_names = new ArrayList<>();
    ArrayList<String> doctor_specializations = new ArrayList<>();
    ArrayList<String> doctor_UID = new ArrayList<>();
    ArrayList<String> doctor_phone_no = new ArrayList<>();
    ArrayList<String> doctor_start_time = new ArrayList<>();
    ArrayList<String> doctor_close_time = new ArrayList<>();
    ArrayList<String> appointed_doctor_bio = new ArrayList<>();
    ArrayList<String> appointed_doctor_img = new ArrayList<>();
    Singleton singleton = new Singleton();
    AppointmentBookingAdapter book_appointment_helper_class;

    SearchDiseaseAdapter searchDiseaseAdapter;

    FirestoreHandler firestoreHandler = new FirestoreHandler();
    CollectionReference noteBookref = firestoreHandler.getFirestoreInstance().collection("SearchDisease");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_disease);
        organ = findViewById(R.id.spinner_disease);
        symptoms = findViewById(R.id.spinner_symptom);

        doctor_profile_recycler = findViewById(R.id.spinner_doctor);

        doctor_profile_recycler.setLayoutManager(new LinearLayoutManager(searchDisease.this));
        book_appointment_helper_class = new AppointmentBookingAdapter(doctor_names, doctor_specializations, doctor_UID, doctor_phone_no, doctor_start_time, doctor_close_time, appointed_doctor_bio,appointed_doctor_img, new AppointmentBookingAdapter.ItemClickListener() {
            @Override
            public void onItemClick(String details) {
                Log.d(details, "Works");
            }
        });
        doctor_profile_recycler.setAdapter(book_appointment_helper_class);
        setUpRecycler();

        singleton.setAdatper(getApplicationContext(), organ, diseases);
        organ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                disease = adapterView.getItemAtPosition(i).toString();

                if (disease != null) {
                    if (disease.equals("Lungs"))
                        checkDisease("Nephrologist");
                    else if (disease.equals("Brain"))
                        checkDisease("Neurologist");
                    else if (disease.equals("Heart"))
                        checkDisease("Cardiologist");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        symptoms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                symptom = parent.getItemAtPosition(position).toString();
                setUpRecycler();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void checkDisease(String doctor) {
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

    private void setUpRecycler() {
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
        firestoreHandler.getFirestoreInstance().collection("SearchDisease").orderBy("organ", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {

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

    private void setAdapter(List<String> list) {
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

    public void FireStoreUsersSpecific(String Category) {
        firestoreHandler.getFirestoreInstance().collection("Professions").orderBy("Full Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        String category = String.valueOf(documentChange.getDocument().get("Profession"));
                        String Specialization = String.valueOf(documentChange.getDocument().get("Doctor_profession"));

                        if (category.equals("Doctor")) {

                            if (Specialization.equals(Category)) {
                                doctor_names.add(String.valueOf(documentChange.getDocument().get("Full Name")));
                                doctor_specializations.add(String.valueOf(documentChange.getDocument().get("Doctor_profession")));
                                doctor_UID.add(documentChange.getDocument().getId());
                                doctor_phone_no.add(String.valueOf(documentChange.getDocument().get("Phone #")));
                                doctor_start_time.add(String.valueOf(documentChange.getDocument().get("Start Time")));
                                doctor_close_time.add(String.valueOf(documentChange.getDocument().get("Close Time")));
                                appointed_doctor_bio.add(String.valueOf(documentChange.getDocument().get("Bio Details")));
                                appointed_doctor_img.add(String.valueOf(documentChange.getDocument().get("Image")));
                            }
                        }

                        book_appointment_helper_class.notifyDataSetChanged();

                    }

                }
            }
        });
    }

}