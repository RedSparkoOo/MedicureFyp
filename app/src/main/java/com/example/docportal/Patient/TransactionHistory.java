package com.example.docportal.Patient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.Pharmacist.Medicine;
import com.example.docportal.Pharmacist.MedicineListAdapter;
import com.example.docportal.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionHistory extends AppCompatActivity {
    RecyclerView _equipmentList;
    TransactionAdapter buyMedicalAdapter;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference noteBookref = firestore.collection("Transaction");

    FirebaseAuth firebaseAuth;
    Object currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        firebaseAuth = FirebaseAuth.getInstance();
        Object currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        }




            setUpRecycler();





    }


    private void  setUpRecycler(){String id = currentUserId.toString();
        System.out.println(id);

            Query query = noteBookref.whereEqualTo("id",String.valueOf(currentUserId)).orderBy("time", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<TransactionModel> options = new FirestoreRecyclerOptions.Builder<TransactionModel>()
                    .setQuery(query, TransactionModel.class).build();
            buyMedicalAdapter = new TransactionAdapter(options);
            _equipmentList = findViewById(R.id.transaction_recycler);

            _equipmentList.setLayoutManager(new WrapContentLinearLayoutManager(TransactionHistory.this, LinearLayoutManager.VERTICAL, false ));


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