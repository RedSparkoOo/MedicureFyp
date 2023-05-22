package com.example.docportal.Pharmacist;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UpdatePharmacist extends AppCompatActivity {


    public static final int GALLERY_CODE = 1000;
    Button Update;
    String token, Image;
    Uri content_uri;
    EditText update_full_name;
    EditText update_email_address;
    EditText update_Password;
    EditText update_Phone_No;
    EditText update_License;
    EditText update_doctor_bio;
    Spinner update_Specializations;
    Spinner update_gender;
    ImageView doctor_profile;
    String user_id;
    String update_fName;
    String update_emailAddress;
    String update_Passcode;
    String update_phoneNo;
    String update_license;
    String update_specializations;
    String update_gends;

    String update_bio;


    String present_specialization;
    String Selected_gender;
    String[] Specializations = {"Community pharmacist", "Specialty drug pharmacist", "Informatic pharmacist", "Hospital pharmacist", "Home care pharmacist"};
    String[] Genders = {"Male", "Female"};

    StorageReference storageReference;

    String old_email;
    String old_pass;
    Singleton singleton = new Singleton();
    FirestoreHandler firestoreHandler = new FirestoreHandler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pharmacist);


        Update = findViewById(R.id.Update);
        update_full_name = findViewById(R.id.update_full_name);
        update_email_address = findViewById(R.id.update_email_address);
        update_Password = findViewById(R.id.update_password);
        update_Phone_No = findViewById(R.id.update_phone_no);
        update_License = findViewById(R.id.update_license);
        update_Specializations = findViewById(R.id.update_specialist_Category);
        update_gender = findViewById(R.id.update_doctor_gender);
        doctor_profile = findViewById(R.id.doctor_profile);
        update_doctor_bio = findViewById(R.id.update_doctor_bio);
        TextView[] textViews = {update_full_name,update_email_address,  update_Password ,update_Phone_No,update_License , update_doctor_bio };


        user_id = firestoreHandler.getCurrentUser();

        Map<String, Object> doctor = new HashMap<>();
        DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Professions").document(user_id);

        doctor_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //-----------------Select Image From Gallery---------\\

                Intent open_gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                //noinspection deprecation
                startActivityForResult(open_gallery, GALLERY_CODE);

            }

        });


        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot != null) {
                    if (documentSnapshot.getString("Image") != null || !documentSnapshot.getString("Image").isEmpty()) {
                        Image = documentSnapshot.getString("Image");
                        Picasso.get().load(documentSnapshot.getString("Image")).into(doctor_profile);
                    }
                }
                update_full_name.setText(documentSnapshot.getString("Full Name"));
                update_email_address.setText(documentSnapshot.getString("Email Address"));
                update_Password.setText(documentSnapshot.getString("Password"));
                update_Phone_No.setText(documentSnapshot.getString("Phone #"));
                update_License.setText(documentSnapshot.getString("License #"));

                present_specialization = documentSnapshot.getString("Pharmacist Category");
                Selected_gender = documentSnapshot.getString("Gender");
                update_doctor_bio.setText(documentSnapshot.getString("Bio Details"));
                old_email = documentSnapshot.getString("Email Address");
                old_pass = documentSnapshot.getString("Password");
                singleton.setAdatper(UpdatePharmacist.this, update_Specializations, Specializations);

                singleton.setAdatper(UpdatePharmacist.this, update_gender, Genders);

                if (documentSnapshot.getString("Gender") != null && documentSnapshot.getString("Gender").equals("Male"))
                    update_gender.setSelection(0);
                else
                    update_gender.setSelection(1);

                update_gends = documentSnapshot.getString("Gender");


                String pharmacistCategory = documentSnapshot.getString("Pharmacist Category");
                if (pharmacistCategory != null) {
                    for (int i = 0; i < Specializations.length; i++) {
                        if (pharmacistCategory.equals(Specializations[i])) {
                            update_Specializations.setSelection(i);
                            break;
                        }
                    }
                }
                update_Specializations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        update_specializations = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {


                    }
                });
                update_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        update_gends = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

        });


        Update.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                CheckEvent checkEvent = new CheckEvent();
                if (checkEvent.isEmpty(textViews)) ;
                else {

                    update_fName = update_full_name.getText().toString();
                    update_emailAddress = update_email_address.getText().toString();
                    update_Passcode = update_Password.getText().toString();
                    update_phoneNo = update_Phone_No.getText().toString();
                    update_license = update_License.getText().toString();
                    update_specializations = update_Specializations.getSelectedItem().toString();

                    update_bio = update_doctor_bio.getText().toString();

                    Image = String.valueOf(content_uri);

                    doctor.put("Image", Image);
                    doctor.put("Full Name", update_fName);
                    doctor.put("Email Address", update_emailAddress);
                    doctor.put("Password", update_Passcode);
                    doctor.put("Phone #", update_phoneNo);
                    doctor.put("Gender", update_gends);
                    doctor.put("License #", update_license);
                    doctor.put("Pharmacist Category", update_specializations);
                    doctor.put("Bio Details", update_bio);
                    doctor.put("Token", token);


                    AuthCredential credential = EmailAuthProvider
                            .getCredential(old_email, old_pass);
                    firestoreHandler.getFirebaseUser().reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "User re-authenticated.");


                                    firestoreHandler.getFirebaseUser().updateEmail(update_emailAddress)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User email address updated.");
                                                    }
                                                }
                                            });
                                    firestoreHandler.getFirebaseUser().updatePassword(update_Passcode).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User Password updated.");
                                            }
                                        }
                                    });
                                }
                            });
                    if (!old_email.equals(update_emailAddress)) {
                        firestoreHandler.getFirebaseUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                singleton.showToast(UpdatePharmacist.this, "Email sent to: " + update_emailAddress);

                            }
                        });
                    }


                    documentReference.set(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            singleton.showToast(UpdatePharmacist.this, "Profile Updated");

                        }
                    });


                }
            }
        });

        doctor_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 1000);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                content_uri = data.getData();
                doctor_profile.setImageURI(content_uri);
            }
        }
    }


}