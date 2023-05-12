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


public class labTestManagement extends AppCompatActivity {

    RecyclerView bloodList;
    labsAdapter bloodBankAdapter;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    CollectionReference noteBookref = firestoreHandler.getFirestoreInstance().collection("LabTests");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_management);
        setUpRecycler();

    }

    private void setUpRecycler() {

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