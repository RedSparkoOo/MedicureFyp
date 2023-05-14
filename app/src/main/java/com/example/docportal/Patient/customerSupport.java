package com.example.docportal.Patient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.CheckEvent;
import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class customerSupport extends AppCompatActivity {
    EditText name, phone, email, message;
    Button submit;
    FirebaseStorage firebaseStorage;

    StorageReference storageReference;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    String identifier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_support);
        name = findViewById(R.id.name_support);
        email = findViewById(R.id.email_address_support);
        phone = findViewById(R.id.phone_support);
        message = findViewById(R.id.message_support);
        TextView[] textViews = {name, email, phone, message};

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        System.out.println(getIntent().getStringExtra("identify"));
        identifier = getIntent().getStringExtra("identify");
        submit = findViewById(R.id.submit_support);
        CheckEvent checkEvent = new CheckEvent();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton singleton = new Singleton();
                System.out.println(checkEvent.isEmpty(textViews));
                if (!(checkEvent.checkEmail(email) || checkEvent.checkName(name)) || checkEvent.isEmpty(textViews)) {
                    checkEvent.checkEmail(email);
                    checkEvent.checkItemName(name);
                    singleton.showToast(customerSupport.this, "please full all form");
                } else {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("Id", firestoreHandler.getCurrentUser());
                    map.put("Name", name.getText().toString());
                    map.put("Email", email.getText().toString());
                    map.put("Phone", phone.getText().toString());
                    map.put("Message", message.getText().toString());

                    DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Customer Support").document(firestoreHandler.getCurrentUser());
                    documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            singleton.showToast(customerSupport.this, "Message Sent");
                            singleton.showToast(customerSupport.this," Response will be arrived on your email ");
                        }
                    });
                }
            }
        });
        if (identifier.equals("patient")) {

            DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Patient").document(firestoreHandler.getCurrentUser());
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    name.setText(value.getString("Patient Name"));
                    email.setText(value.getString("Patient Email Address"));
                    phone.setText(value.getString("Patient phone_no"));

                }


            });
        } else if (identifier.equals("doctor")) {
            DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Professions").document(firestoreHandler.getCurrentUser());
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