package com.example.docportal.Patient;


import android.os.Bundle;

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

    CollectionReference noteBookref = firestoreHandler.getFirestoreInstance().collection("Transaction");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        setUpRecycler();
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