package com.example.docportal.Patient;

import static android.content.ContentValues.TAG;

import static com.example.docportal.R.layout.spinner_item;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.CheckEvent;
import com.example.docportal.Doctor.updateDoctorProfile;
import com.example.docportal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class patientProfileUpdate extends AppCompatActivity {
    ImageView back_to_patient_Dashboard;
    ImageView patient_profile_pic;
    EditText update_patient_name;
    EditText update_patient_email_address;
    EditText update_patient_password;
    EditText update_patient_phone;
    Button update_patient_profile;
    Spinner update_patient_gender;
    FirebaseAuth FAuth;
    FirebaseFirestore FStore;
    StorageReference FStorage;
    FirebaseUser patient_user;
    String UID;
    String Selected_gender;
    String Gender[] = {"Male","Female"};
    String old_email;
    String old_pass;

    public static final int GALLERY_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile_update);


        back_to_patient_Dashboard = findViewById(R.id.back_to_patient_dashboard);
        patient_profile_pic = findViewById(R.id.patient_profile);
        update_patient_name = findViewById(R.id.update_patient_full_name);
        update_patient_email_address = findViewById(R.id.update_patient_email_address);
        update_patient_password = findViewById(R.id.update_patient_password);
        update_patient_phone = findViewById(R.id.update_patient_phone_no);
        update_patient_gender = findViewById(R.id.update_patient_gender);
        update_patient_profile = findViewById(R.id.update_patient_profile);
        FAuth = FirebaseAuth.getInstance();
        FStore = FirebaseFirestore.getInstance();
        FStorage = FirebaseStorage.getInstance().getReference();
        patient_user = FAuth.getCurrentUser();
        TextView[] textViews = {update_patient_name, update_patient_email_address, update_patient_password, update_patient_phone};
        CheckEvent checkEvent = new CheckEvent();

        UID = FAuth.getCurrentUser().getUid();
        Map<String,Object> patient = new HashMap<>();
        DocumentReference patient_documentReference = FStore.collection("Patient").document(UID);
        patientProfileChange();

        patient_documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                update_patient_name.setText(documentSnapshot.getString("Patient Name"));
                update_patient_email_address.setText(documentSnapshot.getString("Patient Email Address"));
                update_patient_password.setText(documentSnapshot.getString("Patient Password"));
                update_patient_phone.setText(documentSnapshot.getString("Patient phone_no"));
                try {
                    for (int i =0; i<=Gender.length; i++){

                        if(Gender[i].equals(Selected_gender)){
                            update_patient_gender.setSelection(i);
                            String Gender_on_top = Gender[0];
                            String GENDER_we_got = Gender[i];
                            Gender[0] = GENDER_we_got;
                            Gender[1] = Gender_on_top;

                            ArrayAdapter gender_adapter = new ArrayAdapter(patientProfileUpdate.this,spinner_item,Gender);
                            gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            update_patient_gender.setAdapter(gender_adapter);
                        }


                    }
                }
                catch (Exception e){
                    Toast.makeText(patientProfileUpdate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                old_email = documentSnapshot.getString("Patient Email Address");
                old_pass = documentSnapshot.getString("Patient Password");
            }

        });

        back_to_patient_Dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientProfileUpdate.this,patientDashboard.class);
                startActivity(intent);
            }
        });

        patient_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent open_gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //noinspection deprecation
                startActivityForResult(open_gallery, GALLERY_CODE);
                patientProfileChange();
            }
        });

        update_patient_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (checkEvent.isEmpty(textViews) || (checkEvent.checkName(update_patient_name) || checkEvent.checkPhone(update_patient_phone) || checkEvent.checkEmail(update_patient_email_address) || checkEvent.checkPassword(update_patient_password)));

                String patient_updated_name = update_patient_name.getText().toString();
                String patient_updated_cell = update_patient_phone.getText().toString();
                String patient_updated_email_address = update_patient_email_address.getText().toString();
                String patient_updated_password = update_patient_password.getText().toString();


                patient.put("Patient Name",patient_updated_name);
                patient.put("Patient Email Address",patient_updated_email_address);
                patient.put("Patient Password",patient_updated_password);
                patient.put("Patient phone_no",patient_updated_cell);



                AuthCredential credential = EmailAuthProvider
                        .getCredential(old_email, old_pass); // Current Login Credentials \\
                // Prompt the user to re-provide their sign-in credentials
                patient_user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "User re-authenticated.");
                                //Now change your email address \\
                                //----------------Code for Changing Email Address----------\\

                                patient_user.updateEmail(patient_updated_email_address)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User email address updated.");
                                                }
                                            }
                                        });
                               patient_user.updatePassword(patient_updated_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if (task.isSuccessful()) {
                                           Log.d(TAG, "User Password updated.");
                                       }
                                   }
                               });
                            }
                        });

                    if(!old_email.equals(update_patient_email_address)){
                        patient_user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(patientProfileUpdate.this, "Email sent to: " + update_patient_email_address+ ". Please verify it!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    patient_documentReference.set(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(patientProfileUpdate.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        }
                    });





            }
        });

    }

    private void patientProfileChange(){

        FStorage = FirebaseStorage.getInstance().getReference();

        StorageReference doc_file_ref = FStorage.child("Patient/"+FAuth.getCurrentUser().getUid()+"/patient_profile.jpg");
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

        if(requestCode == GALLERY_CODE){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                Uri content_uri = data.getData();
                patient_profile_pic.setImageURI(content_uri);

                uploadProfileToFireBase(content_uri);
            }
        }
    }

    private void uploadProfileToFireBase(Uri content_uri) {

        StorageReference doc_file_ref = FStorage.child("Patient/"+ Objects.requireNonNull(FAuth.getCurrentUser()).getUid()+"/patient_profile.jpg");
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
                Toast.makeText(patientProfileUpdate.this, "Profile not uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }
}