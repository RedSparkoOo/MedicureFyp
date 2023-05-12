package com.example.docportal.Patient;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class urgentCare extends AppCompatActivity {

    ImageView patientUrgent_backToDashboard;
    RecyclerView urgent_recycler;
    ImageView empty_phone_book;
    TextView no_contacts_added;
    ImageView add_contacts;
    UrgentCareAdapter careAdapter;


    List<String> ContactName;
    List<String> ContactNumber;
    List<String> Relation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent_care);

        patientUrgent_backToDashboard = findViewById(R.id.patientUrgent_backToDashboard);
        urgent_recycler = findViewById(R.id.urgent_recycler);
        empty_phone_book = findViewById(R.id.empty_phone_book);
        no_contacts_added = findViewById(R.id.no_contacts_added);
        add_contacts = findViewById(R.id.add_contacts);
        urgent_recycler.setVisibility(View.INVISIBLE);


        add_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton singleton = new Singleton();
                singleton.openActivity(urgentCare.this, UrgentCareForm.class);
            }
        });

        loadContacts();

    }

    private void loadContacts() {
        FirestoreHandler firestoreHandler = new FirestoreHandler();

        firestoreHandler.getFirestoreInstance().collection("Emergency Contacts").orderBy("Contact Name", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc != null) {
                        empty_phone_book.setVisibility(View.INVISIBLE);
                        no_contacts_added.setVisibility(View.INVISIBLE);
                        urgent_recycler.setVisibility(View.VISIBLE);

                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            String ID = dc.getDocument().getId();

                            if (ID.equals(firestoreHandler.getCurrentUser())) {

                                ContactName = new ArrayList<>();
                                ContactNumber = new ArrayList<>();
                                Relation = new ArrayList<>();

                                ContactName = (List<String>) dc.getDocument().getData().get("Contact Name");
                                ContactNumber = (List<String>) dc.getDocument().getData().get("Contact Number");
                                Relation = (List<String>) dc.getDocument().getData().get("Contact Relation ");

                                urgent_recycler.setLayoutManager(new LinearLayoutManager(urgentCare.this));
                                careAdapter = new UrgentCareAdapter(ContactName, Relation, ContactNumber);
                                urgent_recycler.setAdapter(careAdapter);


                            }

                        }

                    }


                }


            }
        });


    }
}