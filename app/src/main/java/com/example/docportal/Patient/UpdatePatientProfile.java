package com.example.docportal.Patient;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.CheckEvent;
import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UpdatePatientProfile extends AppCompatActivity {
    public static final int GALLERY_CODE = 1000;
    ImageView back_to_patient_Dashboard;
    ImageView patient_profile_pic;
    EditText update_patient_name;
    EditText update_patient_email_address;
    EditText update_patient_password;
    EditText update_patient_phone;
    Button update_patient_profile;
    Spinner update_patient_gender;
    StorageReference FStorage;
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    Singleton singleton = new Singleton();
    String Selected_gender;
    String[] Gender = {"Male", "Female"};
    String old_email;
    String old_pass;
    DocumentReference patient_documentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_patient_profile);


        back_to_patient_Dashboard = findViewById(R.id.back_to_patient_dashboard);
        patient_profile_pic = findViewById(R.id.patient_profile);
        update_patient_name = findViewById(R.id.update_patient_full_name);
        update_patient_email_address = findViewById(R.id.update_patient_email_address);
        update_patient_password = findViewById(R.id.update_patient_password);
        update_patient_phone = findViewById(R.id.update_patient_phone_no);
        update_patient_gender = findViewById(R.id.update_patient_gender);
        update_patient_profile = findViewById(R.id.update_patient_profile);

        FStorage = FirebaseStorage.getInstance().getReference();


        patient_documentReference = firestoreHandler.getFirestoreInstance().collection("Patient").document(firestoreHandler.getCurrentUser());
        patientProfileChange();
        getProfile();

        back_to_patient_Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(UpdatePatientProfile.this, patientDashboard.class);
            }
        });

        patient_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(open_gallery, GALLERY_CODE);
                patientProfileChange();
            }
        });

        update_patient_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updatePatientProfile();


            }
        });

    }

    private void getProfile() {

        patient_documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                update_patient_name.setText(documentSnapshot.getString("Patient Name"));
                update_patient_email_address.setText(documentSnapshot.getString("Patient Email Address"));
                update_patient_password.setText(documentSnapshot.getString("Patient Password"));
                update_patient_phone.setText(documentSnapshot.getString("Patient phone_no"));
                Selected_gender = documentSnapshot.getString("Patient Gender");
                try {
                    for (int i = 0; i <= Gender.length; i++) {

                        if (Gender[i].equals(Selected_gender)) {
                            update_patient_gender.setSelection(i);
                            singleton.setAdatper(UpdatePatientProfile.this, update_patient_gender, Gender);
                        }


                    }
                } catch (Exception e) {

                }

                old_email = documentSnapshot.getString("Patient Email Address");
                old_pass = documentSnapshot.getString("Patient Password");
            }

        });


    }


    private void updatePatientProfile() {

        TextView[] textViews = {update_patient_name, update_patient_email_address, update_patient_password, update_patient_phone};
        CheckEvent checkEvent = new CheckEvent();
        Map<String, Object> patient = new HashMap<>();

        if (checkEvent.isEmpty(textViews) || !checkEvent.checkName(update_patient_name) || !checkEvent.checkPhone(update_patient_phone) || !checkEvent.checkEmail(update_patient_email_address) || !checkEvent.checkPassword(update_patient_password));

         else {
            String patient_updated_name = update_patient_name.getText().toString();
            String patient_updated_cell = update_patient_phone.getText().toString();
            String patient_updated_email_address = update_patient_email_address.getText().toString();
            String patient_updated_password = update_patient_password.getText().toString();
            String patient_update_gender = update_patient_gender.getSelectedItem().toString();

            patient.put("Patient Name", patient_updated_name);
            patient.put("Patient Email Address", patient_updated_email_address);
            patient.put("Patient Password", patient_updated_password);
            patient.put("Patient phone_no", patient_updated_cell);
            patient.put("Patient Gender", patient_update_gender);


            try {
                AuthCredential credential = EmailAuthProvider
                        .getCredential(old_email, old_pass); // Current Login Credentials \\
                // Prompt the user to re-provide their sign-in credentials
                firestoreHandler.getFirebaseUser().reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "User re-authenticated.");
                                //Now change your email address \\
                                //----------------Code for Changing Email Address----------\\

                                firestoreHandler.getFirebaseUser().updateEmail(patient_updated_email_address)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User email address updated.");
                                                }
                                            }
                                        });
                                firestoreHandler.getFirebaseUser().updatePassword(patient_updated_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User Password updated.");
                                        }
                                    }
                                });
                            }
                        });

                if (!old_email.equals(patient_updated_email_address))
                    firestoreHandler.getFirebaseUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            singleton.showToast(UpdatePatientProfile.this, "Email sent to: " + update_patient_email_address + ". Please verify it!");
                        }
                    });

                patient_documentReference.set(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        singleton.showToast(UpdatePatientProfile.this, "Profile Updated");
                    }
                });

            } catch (Exception e) {
                singleton.showToast(UpdatePatientProfile.this, e.getMessage());
            }


        }


    }

    private void patientProfileChange() {

        FStorage = FirebaseStorage.getInstance().getReference();

        StorageReference doc_file_ref = FStorage.child("Patient/" + firestoreHandler.getCurrentUser() + "/patient_profile.jpg");
        doc_file_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(patient_profile_pic);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri content_uri = data.getData();
                patient_profile_pic.setImageURI(content_uri);

                uploadProfileToFireBase(content_uri);
            }
        }
    }

    private void uploadProfileToFireBase(Uri content_uri) {

        StorageReference doc_file_ref = FStorage.child("Patient/" + firestoreHandler.getCurrentUser() + "/patient_profile.jpg");
        doc_file_ref.putFile(content_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                doc_file_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(patient_profile_pic);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                singleton.showToast(UpdatePatientProfile.this, "Profile not uploaded");
            }
        });
    }
}