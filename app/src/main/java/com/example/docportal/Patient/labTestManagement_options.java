package com.example.docportal.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;


public class labTestManagement_options extends AppCompatActivity {

    RecyclerView bloodList;
    labTestManagementAdapter bloodBankAdapter;
    EditText editText;
    Query newQuery;
    ImageView All, Blood, Cardio, Liver, Kidney, Lungs;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    CollectionReference noteBookref = firestoreHandler.getFirestoreInstance().collection("LabTests");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_management_options);
        setUpRecycler();
        editText = findViewById(R.id.search_lab_test);
        All = findViewById(R.id.all_test);
        Blood = findViewById(R.id.blood_category);
        Cardio = findViewById(R.id.cardio_category);
        Liver = findViewById(R.id.liver_category);
        Kidney = findViewById(R.id.kidney_category);
        Lungs = findViewById(R.id.lungs_category);
        All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuery = noteBookref.orderBy("category", Query.Direction.DESCENDING);
                setQuery();
            }
        });
        Blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuery = noteBookref.whereEqualTo("category", "Blood");
                setQuery();
            }
        });
        Cardio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuery = noteBookref.whereEqualTo("category", "Cardio");
                setQuery();
            }
        });
        Liver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuery = noteBookref.whereEqualTo("category", "Liver");
                setQuery();
            }
        });
        Kidney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuery = noteBookref.whereEqualTo("category", "Kidney");
                setQuery();
            }
        });
        Lungs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuery = noteBookref.whereEqualTo("category", "Lungs");
                setQuery();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();

                if (query.trim().isEmpty()) {
                    newQuery = noteBookref.orderBy("category", Query.Direction.DESCENDING);
                } else {
                    // Create a new query for case-insensitive search
                    newQuery = noteBookref.whereGreaterThanOrEqualTo("category", query)
                            .whereLessThanOrEqualTo("category", query + "\uf8ff")
                            .orderBy("category");
                }
                setQuery();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setQuery() {
        FirestoreRecyclerOptions<BloodBankModel> newOptions = new FirestoreRecyclerOptions.Builder<BloodBankModel>()
                .setQuery(newQuery, BloodBankModel.class)
                .build();
        bloodBankAdapter.updateOptions(newOptions);
    }


    private void setUpRecycler() {

        Intent intent = getIntent();
        Query query = noteBookref.orderBy("category", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<BloodBankModel> options = new FirestoreRecyclerOptions.Builder<BloodBankModel>()
                .setQuery(query, BloodBankModel.class).build();
        bloodBankAdapter = new labTestManagementAdapter(intent, options);
        bloodList = findViewById(R.id.searchLabTestRecycler);

        bloodList.setLayoutManager(new WrapContentLinearLayoutManager(labTestManagement_options.this, LinearLayoutManager.VERTICAL, false));
        bloodList.setAdapter(bloodBankAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bloodBankAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        bloodBankAdapter.stopListening();
    }


}