package com.example.docportal.Patient;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class patientViewProfile extends AppCompatActivity {
TextView view_patient_name;
TextView view_patient_email;
TextView view_patient_phone;
ImageView view_patient_profilePic;
FirebaseAuth FAuth;
StorageReference FStorage;
FirebaseFirestore FStore;

String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view_profile);


        view_patient_name = findViewById(R.id.view_patient_name);
        view_patient_email = findViewById(R.id.view_patient_email);
        view_patient_phone = findViewById(R.id.view_patient_phone);
        view_patient_profilePic = findViewById(R.id.pat_profile);

        FAuth = FirebaseAuth.getInstance();
        UID = FAuth.getCurrentUser().getUid();

        FStorage = FirebaseStorage.getInstance().getReference();
        FStore = FirebaseFirestore.getInstance();
        FirebaseUser patient = FAuth.getCurrentUser();

        if(patient.isEmailVerified()){
            loadPatientProfile();
        }
        else {
            Toast.makeText(this, "Please verify your profile first!", Toast.LENGTH_SHORT).show();
        }



    }

    private void loadPatientProfile(){

        StorageReference doc_file_ref = FStorage.child("Patient/"+FAuth.getCurrentUser().getUid()+"/patient_profile.jpg");
        doc_file_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(view_patient_profilePic);

            }
        });


        DocumentReference reference = FStore.collection("Patient").document(UID);

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value == null){
                    Toast.makeText(patientViewProfile.this, "Nothing to show!", Toast.LENGTH_SHORT).show();
                }
                else {
                    view_patient_name.setText(value.getString("Patient Name"));
                    view_patient_email.setText(value.getString("Patient Email Address"));
                    view_patient_phone.setText(value.getString("Patient Phone"));

                }

            }
        });
    }
}