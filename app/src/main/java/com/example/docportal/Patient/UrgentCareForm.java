package com.example.docportal.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.example.docportal.UrgentCareFormCheckEvent;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UrgentCareForm extends AppCompatActivity {

    EditText contact_name;
    EditText contact_relation;
    EditText contact_number;
    ImageView UrgentForm_backToUrgentCare;
    Button confirm_contact;


    ArrayList<String> ContactName;
    ArrayList<String> ContactRelation;
    ArrayList<String> ContactPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent_care_form);

        contact_name = findViewById(R.id.contact_name);
        contact_relation = findViewById(R.id.contact_relation);
        contact_number = findViewById(R.id.contact_number);
        UrgentForm_backToUrgentCare = findViewById(R.id.UrgentForm_backToUrgentCare);
        confirm_contact = findViewById(R.id.confirm_contact);

        TextView[] textViews = {contact_name, contact_relation, contact_number};
        UrgentCareFormCheckEvent formCheckEvent = new UrgentCareFormCheckEvent();

        ContactName = new ArrayList<>();
        ContactRelation = new ArrayList<>();
        ContactPhone = new ArrayList<>();


        UrgentForm_backToUrgentCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UrgentCareForm.this, urgentCare.class);
                startActivity(intent);
            }
        });

        confirm_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (formCheckEvent.isEmpty(textViews) || !((formCheckEvent.checkName(contact_name) || (formCheckEvent.checkPhone(contact_number)))))
                    ;
                else {
                    String Contact_Name = contact_name.getText().toString();
                    String Contact_Relation = contact_relation.getText().toString();
                    String Contact_Number = contact_number.getText().toString();

                    ContactName.add(Contact_Name);
                    ContactPhone.add(Contact_Number);
                    ContactRelation.add(Contact_Relation);
                    AddToFireBase(ContactName, ContactPhone, ContactRelation);
                }


//                Intent intent = new Intent(UrgentCareForm.this,urgentCare.class);
//                startActivity(intent);
            }
        });

    }

    private void AddToFireBase(ArrayList<String> Name, ArrayList<String> Relation, ArrayList<String> Number) {
        FirestoreHandler firestoreHandler = new FirestoreHandler();


        firestoreHandler.getFirestoreInstance().clearPersistence();
        DocumentReference contactReference = firestoreHandler.getFirestoreInstance().collection("Emergency Contacts").document(firestoreHandler.getCurrentUser());
        contactReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                if (value.contains("Contact Name") && value.contains("Contact Relation") && value.contains("Contact Number") && value.exists()) {

                    for (String name_value : Name) {
                        Object Contact_Name_Latest = name_value;
                        contactReference.update("Contact Name", FieldValue.arrayUnion(Contact_Name_Latest));
                    }

                    for (String phone_value : Number) {
                        Object Contact_Phone_Latest = phone_value;
                        contactReference.update("Contact Number", FieldValue.arrayUnion(Contact_Phone_Latest));
                    }

                    for (String relation_value : Relation) {
                        Object Contact_Relation_Latest = relation_value;
                        contactReference.update("Contact Relation", FieldValue.arrayUnion(Contact_Relation_Latest));
                    }

                } else {
                    Map<String, ArrayList<String>> contact = new HashMap<>();
                    contact.put("Contact Name", ContactName);
                    contact.put("Contact Number", ContactPhone);
                    contact.put("Contact Relation ", ContactRelation);
                    contactReference.set(contact);
                }
                Singleton singleton = new Singleton();
                singleton.showToast(UrgentCareForm.this, "Contact Stored");

            }
        });


    }
}