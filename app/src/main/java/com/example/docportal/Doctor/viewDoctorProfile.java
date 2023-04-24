package com.example.docportal.Doctor;


import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class viewDoctorProfile extends AppCompatActivity {
    TextView doctor_name;
    TextView doctor_specialization;
    TextView doctor_email;
    TextView doctor_phone;
    TextView doctor_license;
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;
    ImageView doc_profile;
    StorageReference storageReference;
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctor_profile);

        doctor_name = findViewById(R.id.view_doctor_name);
        doctor_specialization = findViewById(R.id.view_doctor_specialization);
        doctor_email = findViewById(R.id.view_doctor_email);
        doctor_phone = findViewById(R.id.view_doctor_phone);
        doctor_license = findViewById(R.id.view_doctor_license);
        doc_profile = findViewById(R.id.doc_profile);
        storageReference = FirebaseStorage.getInstance().getReference();

        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user_id = fAuth.getCurrentUser().getUid();
        FirebaseUser doctor = fAuth.getCurrentUser();


        if(doctor.isEmailVerified()){
            loadProfile();

            DocumentReference documentReference = firestore.collection("Doctor").document(user_id);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    doctor_name.setText(documentSnapshot.getString("Full Name"));
                    doctor_specialization.setText(documentSnapshot.getString("Doctor_profession"));
                    doctor_email.setText(documentSnapshot.getString("Email Address"));
                    doctor_phone.setText(documentSnapshot.getString("Phone #"));
                    doctor_license.setText(documentSnapshot.getString("License #"));
                }
            });

        }

    }

    private void loadProfile(){
        StorageReference doc_file_ref = storageReference.child("Doctor/"+fAuth.getCurrentUser().getUid()+"/doctor_profile.jpg");
        doc_file_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(doc_profile);

            }
        });
    }

}