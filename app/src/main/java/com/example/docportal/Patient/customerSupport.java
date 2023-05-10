package com.example.docportal.Patient;

import static kotlin.reflect.jvm.internal.impl.builtins.StandardNames.FqNames.map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.CheckEvent;
import com.example.docportal.Pharmacist.EditMedicine;
import com.example.docportal.Pharmacist.MedicineList;
import com.example.docportal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class customerSupport extends AppCompatActivity {
    EditText name, phone, email, message;
    Button submit;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    CheckEvent checkEvent;
    Object currentUserId;
    String identifier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);
        name = findViewById(R.id.name_support);
        email = findViewById(R.id.email_address_support);
        phone = findViewById(R.id.phone_support);
        message = findViewById(R.id.message_support);
        TextView[] textViews = {name,email,phone,message};
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        System.out.println(getIntent().getStringExtra("identify"));
        identifier = getIntent().getStringExtra("identify");
        Object currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        }
        submit = findViewById(R.id.submit_support);
        CheckEvent checkEvent = new CheckEvent();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(checkEvent.isEmpty(textViews));
                if (((checkEvent.checkEmail(email) || checkEvent.checkName(name)) && (checkEvent.isEmpty(textViews)  ))== false){
                    checkEvent.checkEmail(email);
                    checkEvent.checkItemName(name);
                }
                else {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("Id", currentUserId.toString());
                    map.put("Name", name.getText().toString());
                    map.put("Email", email.getText().toString());
                    map.put("Phone", phone.getText().toString());
                    map.put("Message", message.getText().toString());

                    DocumentReference documentReference = firebaseFirestore.collection("Customer Support").document(currentUserId.toString());
                    documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(customerSupport.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        if(identifier.equals("patient")) {

            DocumentReference documentReference = firebaseFirestore.collection("Patient").document(currentUserId.toString());
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    name.setText(value.getString("Patient Name"));
                    email.setText(value.getString("Patient Email Address"));
                    phone.setText(value.getString("Patient phone_no"));

                }


            });
        }
        else if (identifier.equals("doctor")){
            DocumentReference documentReference = firebaseFirestore.collection("Professions").document(currentUserId.toString());
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    name.setText(value.getString("Full Name"));
                    email.setText(value.getString("Email Address"));
                    phone.setText(value.getString("Phone #"));

                }


            });
        }

    }
}