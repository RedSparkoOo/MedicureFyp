package com.example.docportal.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class labTestManagement extends AppCompatActivity {

    RecyclerView bloodList;
    labsAdapter bloodBankAdapter;
    // EditText editText;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference noteBookref = firestore.collection("LabTests");
    Button _pharmacyAddToCart;
    FirebaseAuth firebaseAuth;

    Object currentUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_management);

        //     editText = findViewById(R.id.medicineSearch);
        firebaseAuth = FirebaseAuth.getInstance();
        Object currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        }




        setUpRecycler();


    }










    private void  setUpRecycler() {

            Query query = noteBookref.orderBy("labName", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<BloodBankModel> options = new FirestoreRecyclerOptions.Builder<BloodBankModel>()
                    .setQuery(query, BloodBankModel.class).build();
            bloodBankAdapter = new labsAdapter(options);
            bloodList = findViewById(R.id.labs_available_recycler);

            bloodList.setLayoutManager(new WrapContentLinearLayoutManager(labTestManagement.this, LinearLayoutManager.VERTICAL, false));
            bloodList.setAdapter(bloodBankAdapter);

//            editText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    String aquery = charSequence.toString().toLowerCase();
//                    Query filteredQuery = noteBookref.orderBy("category", Query.Direction.DESCENDING).startAt(aquery).endAt(query + "\uf8ff"); // Replace "name" with the field you want to filter on
//                    FirestoreRecyclerOptions<BloodBankModel> options = new FirestoreRecyclerOptions.Builder<BloodBankModel>()
//                            .setQuery(filteredQuery, BloodBankModel.class).build();
//                    bloodBankAdapter.updateOptions(options);
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//
//
//                }
//            });


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