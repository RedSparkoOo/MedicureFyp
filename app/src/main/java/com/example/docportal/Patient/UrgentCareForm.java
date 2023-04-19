package com.example.docportal.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
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
    FirebaseFirestore FStore;
    FirebaseAuth FAuth;
    String UID;

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

        FStore = FirebaseFirestore.getInstance();
        FAuth = FirebaseAuth.getInstance();
        UID = FAuth.getCurrentUser().getUid();

        ContactName = new ArrayList<>();
        ContactRelation = new ArrayList<>();
        ContactPhone = new ArrayList<>();
        
        
        UrgentForm_backToUrgentCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UrgentCareForm.this,urgentCare.class);
                startActivity(intent);;
            }
        });
        
        confirm_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Contact_Name = contact_name.getText().toString();
                String Contact_Relation = contact_relation.getText().toString();
                String Contact_Number = contact_number.getText().toString();

                ContactName.add(Contact_Name);
                ContactPhone.add(Contact_Number);
                ContactRelation.add(Contact_Relation);
                AddToFireBase(ContactName,ContactPhone,ContactRelation);
//                Intent intent = new Intent(UrgentCareForm.this,urgentCare.class);
//                startActivity(intent);
            }
        });
        
    }

    private void AddToFireBase(ArrayList<String> Name, ArrayList<String> Relation, ArrayList<String> Number) {


        FStore.clearPersistence();
        DocumentReference contactReference = FStore.collection("Emergency Contacts").document(UID);
        contactReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {




                if(value.contains("Contact Name") && value.contains("Contact Relation") && value.contains("Contact Number") && value.exists()){

                    for (String name_value: Name) {
                        Object Contact_Name_Latest = name_value;
                        contactReference.update("Contact Name", FieldValue.arrayUnion(Contact_Name_Latest));
                    }

                    for (String phone_value: Number) {
                        Object Contact_Phone_Latest = phone_value;
                        contactReference.update("Contact Name", FieldValue.arrayUnion(Contact_Phone_Latest));
                    }

                    for (String relation_value: Relation) {
                        Object Contact_Relation_Latest = relation_value;
                        contactReference.update("Contact Name", FieldValue.arrayUnion(Contact_Relation_Latest));
                    }

                }
                else {
                    Map<String,ArrayList<String>> contact = new HashMap<>();
                    contact.put("Contact Name", ContactName);
                    contact.put("Contact Number",ContactPhone);
                    contact.put("Contact Relation ",ContactRelation);
                    contactReference.set(contact);
                }

                Toast.makeText(UrgentCareForm.this, "Contact Stored", Toast.LENGTH_SHORT).show();




            }
        });



    }
}