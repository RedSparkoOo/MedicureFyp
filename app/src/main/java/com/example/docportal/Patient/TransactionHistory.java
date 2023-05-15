package com.example.docportal.Patient;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

public class TransactionHistory extends AppCompatActivity {
    RecyclerView _equipmentList;
    TransactionAdapter buyMedicalAdapter;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    ImageView back_to_patient_dashboard;
    LinearLayout empty_history;

    CollectionReference noteBookref = firestoreHandler.getFirestoreInstance().collection("Transaction");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        empty_history = findViewById(R.id.empty_history);

        setUpRecycler();
        back_to_patient_dashboard = findViewById(R.id.back_to_patient_dashboard);

        back_to_patient_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionHistory.this,patientDashboard.class);
                startActivity(intent);
            }
        });

        empty_history.setVisibility(View.VISIBLE);
    }


    private void setUpRecycler() {

        Query query = noteBookref.whereEqualTo("id", String.valueOf(firestoreHandler.getCurrentUser())).orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<TransactionModel> options = new FirestoreRecyclerOptions.Builder<TransactionModel>()
                .setQuery(query, TransactionModel.class).build();
        buyMedicalAdapter = new TransactionAdapter(options);
        _equipmentList = findViewById(R.id.transaction_recycler);

        _equipmentList.setLayoutManager(new WrapContentLinearLayoutManager(TransactionHistory.this, LinearLayoutManager.VERTICAL, false));


        buyMedicalAdapter.updateOptions(options);
        _equipmentList.setAdapter(buyMedicalAdapter);
        empty_history.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void onStart() {
        super.onStart();
        buyMedicalAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        buyMedicalAdapter.stopListening();
    }


}