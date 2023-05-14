package com.example.docportal.Patient;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class bloodBank extends AppCompatActivity {

    RecyclerView bloodList;
    BanksAdapter bloodBankAdapter;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference noteBookref = firestore.collection("bloodbank");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);
        setUpRecycler();
    }


    private void setUpRecycler() {

        Query query = noteBookref.orderBy("name", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<BloodBankModel> options = new FirestoreRecyclerOptions.Builder<BloodBankModel>()
                .setQuery(query, BloodBankModel.class).build();
        bloodBankAdapter = new BanksAdapter(options);
        bloodList = findViewById(R.id.banks_available_recycler);

        bloodList.setLayoutManager(new WrapContentLinearLayoutManager(bloodBank.this, LinearLayoutManager.VERTICAL, false));
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