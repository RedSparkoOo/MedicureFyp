package com.example.docportal.Doctor;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateDoctorNurseProfile extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int CAMERA_CODE = 101;
    public static final int REQUEST_CODE = 100;
    public static final int GALLERY_CODE = 1000;
    Button Update;
    EditText update_full_name;
    EditText update_email_address;
    EditText update_Password;
    EditText update_Phone_No;
    EditText update_License;
    EditText update_doctor_bio;
    EditText update_start_time;
    EditText update_close_time;
    Spinner update_gender;
    ImageView doctor_profile;
    String update_fName;
    String update_emailAddress;
    String update_Passcode;
    String update_phoneNo;
    String update_license;
    String update_specializations;
    String update_gend;
    String update_bio;
    String update_profession;

    String specialization_on_top;
    String specialization_we_got;
    String present_specialization;
    String Selected_gender;
    String selected_profession;
    String[] Doctor_profession = {"Cardiologist", "Oncologist", "Nephrologist", "Neurologist", "Pedriatican", "physiologist"};
    String[] Nurse_profession = {"Mental Health Nurse (MHN)", "Learning Disability Nurse (LDN)", "Adult Nurse (AN)", "Children Nurse (CN)", "Critical Care Nurse (CCN)"};
    String[] Genders = {"Male", "Female"};
    String[] Profession = {"Doctor", "Nurse"};
    String[] Empty = {};
    FirestoreHandler firestoreHandler = new FirestoreHandler();
    Singleton singleton = new Singleton();
    StorageReference storageReference;

    String old_email;
    String old_pass;
    Spinner update_profession_category;
    Spinner update_specialist_Category;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_doctor_nurse_profile);

        Update = findViewById(R.id.Update);
        update_full_name = findViewById(R.id.update_full_name);
        update_email_address = findViewById(R.id.update_email_address);
        update_Password = findViewById(R.id.update_password);
        update_Phone_No = findViewById(R.id.update_phone_no);
        update_License = findViewById(R.id.update_license);
        update_specialist_Category = findViewById(R.id.update_specialist_Category);
        update_profession_category = findViewById(R.id.update_profession_category);
        update_gender = findViewById(R.id.update_doctor_gender);
        doctor_profile = findViewById(R.id.doctor_profile);
        update_doctor_bio = findViewById(R.id.update_doctor_bio);




        Map<String, Object> doctor = new HashMap<>();
        documentReference = firestoreHandler.getFirestoreInstance().collection("Professions").document(firestoreHandler.getCurrentUser());

        doctor_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //-----------------Select Image From Gallery---------\\

                Intent open_gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                //noinspection deprecation
                startActivityForResult(open_gallery, GALLERY_CODE);

            }

        });
        profileChange();
        getProfile();
        updateProfessions();


        Update.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                TextView[] textViews = {update_full_name, update_email_address, update_Password, update_Phone_No, update_License};
                CheckEvent checkEvent = new CheckEvent();

                if (checkEvent.isEmpty(textViews) || !checkEvent.checkName(update_full_name) || !checkEvent.checkEmail(update_email_address) || !checkEvent.checkPhone(update_Phone_No) || !checkEvent.checkLicense(update_License)) {
                    Toast.makeText(UpdateDoctorNurseProfile.this, "Please fill out the data correctly", Toast.LENGTH_SHORT).show();
                } else {

                    update_fName = update_full_name.getText().toString();
                    update_emailAddress = update_email_address.getText().toString();
                    update_Passcode = update_Password.getText().toString();
                    update_phoneNo = update_Phone_No.getText().toString();
                    update_license = update_License.getText().toString();
                    update_specializations = update_specialist_Category.getSelectedItem().toString();
                    update_profession = update_profession_category.getSelectedItem().toString();
                    update_gend = update_gender.getSelectedItem().toString();
                    update_bio = update_doctor_bio.getText().toString();


                    doctor.put("Full Name", update_fName);
                    doctor.put("Email Address", update_emailAddress);
                    doctor.put("Password", update_Passcode);
                    doctor.put("Phone #", update_phoneNo);
                    doctor.put("Gender", update_gend);
                    doctor.put("License #", update_license);
                    doctor.put("Specialization", update_specializations);
                    doctor.put("Profession", update_profession);
                    doctor.put("Bio Details", update_bio);


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
                                    singleton.showToast(UpdateDoctorNurseProfile.this, "Email sent to: " + update_emailAddress + ". Please verify it!");
                                }
                            });
                        }


                        documentReference.set(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                singleton.showToast(UpdateDoctorNurseProfile.this, "Profile Updated");

                            }
                        });
                    } catch (Exception e) {
                        singleton.showToast(UpdateDoctorNurseProfile.this, e.toString());
                    }

                    Intent intent = new Intent(UpdateDoctorNurseProfile.this,DoctorNurseDashboard.class);
                    startActivity(intent);
                }
            }
        });


    }

    private void updateProfessions() {
        singleton.setAdatper(this, update_gender, Genders);
        singleton.setAdatper(this, update_profession_category, Profession);


        update_gender.setPrompt("Select One");
        update_specialist_Category.setPrompt("Select One");
        update_profession_category.setPrompt("Select One");

        update_profession_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (update_profession_category.getSelectedItem().equals(""))
                    singleton.setAdatper(UpdateDoctorNurseProfile.this, update_specialist_Category, Empty);
                if (update_profession_category.getSelectedItem().equals("Doctor"))
                    singleton.setAdatper(UpdateDoctorNurseProfile.this, update_specialist_Category, Doctor_profession);
                if (update_profession_category.getSelectedItem().equals("Nurse"))
                    singleton.setAdatper(UpdateDoctorNurseProfile.this, update_specialist_Category, Nurse_profession);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getProfile() {

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                update_full_name.setText(documentSnapshot.getString("Full Name"));
                update_email_address.setText(documentSnapshot.getString("Email Address"));
                update_Password.setText(documentSnapshot.getString("Password"));
                update_Phone_No.setText(documentSnapshot.getString("Phone #"));
                update_License.setText(documentSnapshot.getString("License #"));
                present_specialization = documentSnapshot.getString("Specialization");
                Selected_gender = documentSnapshot.getString("Gender");
                selected_profession = documentSnapshot.getString("Profession");
                update_doctor_bio.setText(documentSnapshot.getString("Bio Details"));
                old_email = documentSnapshot.getString("Email Address");
                old_pass = documentSnapshot.getString("Password");

                for (int i = 0; i < Profession.length; i++) {

                    if (Profession[i].equals(selected_profession)) {

                        update_profession_category.setSelection(i);
                        singleton.setAdatper(UpdateDoctorNurseProfile.this, update_profession_category, Profession);

                    }


                }

                for (int i = 0; i < Doctor_profession.length; i++) {


                    if (Doctor_profession[i].equals(present_specialization)) {

                        update_specialist_Category.setSelection(i);
                        specialization_on_top = Doctor_profession[0];
                        specialization_we_got = Doctor_profession[i];
                        Doctor_profession[0] = specialization_we_got;
                        Doctor_profession[i] = specialization_on_top;

                        update_specialist_Category.setSelection(i);
                        singleton.setAdatper(UpdateDoctorNurseProfile.this, update_profession_category, Doctor_profession);

                    }

                }


                for (int i = 0; i < Profession.length; i++) {
                    if (Profession[i].equals(selected_profession)) {
                        update_profession_category.setSelection(i);
                        singleton.setAdatper(UpdateDoctorNurseProfile.this, update_profession_category, Profession);
                    }

                }
                for (int i = 0; i < Nurse_profession.length; i++) {

                    if (Nurse_profession[i].equals(present_specialization)) {

                        update_specialist_Category.setSelection(i);
                        specialization_on_top = Nurse_profession[0];
                        specialization_we_got = Nurse_profession[i];
                        Nurse_profession[0] = specialization_we_got;
                        Nurse_profession[i] = specialization_on_top;
                        singleton.setAdatper(UpdateDoctorNurseProfile.this, update_specialist_Category, Nurse_profession);


                    }
                }


                try {
                    for (int i = 0; i <= Genders.length; i++) {

                        if (Genders[i].equals(Selected_gender)) {
                            update_gender.setSelection(i);
                            singleton.setAdatper(UpdateDoctorNurseProfile.this, update_gender, Genders);
                        }


                    }
                } catch (Exception e) {

                }


            }

        });

    }


    private void profileChange() {
        storageReference = FirebaseStorage.getInstance().getReference();


        StorageReference doc_file_ref = storageReference.child("Professions/" + firestoreHandler.getCurrentUser() + "/doctor_profile.jpg");
        doc_file_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(doctor_profile);

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
                doctor_profile.setImageURI(content_uri);
                uploadProfileToFireBase(content_uri);
            }
        }
    }

    private void uploadProfileToFireBase(Uri content_uri) {

        StorageReference doc_file_ref = storageReference.child("Doctor/" + firestoreHandler.getCurrentUser() + "/doctor_profile.jpg");
        doc_file_ref.putFile(content_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                doc_file_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(doctor_profile);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                singleton.showToast(UpdateDoctorNurseProfile.this, "Profile not uploaded");
            }
        });
    }


}





