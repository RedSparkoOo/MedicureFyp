package com.example.docportal.Doctor;

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

public class updateDoctorProfile extends AppCompatActivity {

    Button Update;
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int CAMERA_CODE = 101;
    public static final int REQUEST_CODE = 100;
    public static final int GALLERY_CODE = 1000;
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
    String update_gend;
    String update_bio;
    FirebaseFirestore firestore;
    FirebaseAuth fAuth;
    String specialization_on_top;
    String specialization_we_got;
    String present_specialization;
    String Selected_gender;
    String[] Specializations = {"Cardiologist","Oncologist","Nephrologist","Neurologist","Pedriatican","physiologist"};
    String[] Genders = {"Male","Female"};
    StorageReference storageReference;
    FirebaseUser doctor_user;
    String old_email;
    String old_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_doctor_profile);


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


        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user_id = fAuth.getCurrentUser().getUid();
        doctor_user = fAuth.getCurrentUser();
        Map<String,Object> doctor = new HashMap<>();
        DocumentReference documentReference = firestore.collection("Doctor").document(user_id);

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
                update_doctor_bio.setText(documentSnapshot.getString("Bio Details"));
                old_email = documentSnapshot.getString("Email Address");
                old_pass = documentSnapshot.getString("Password");


                try {

                        for (int i=0; i<Specializations.length; i++) {
                            if (Specializations[i].equals(present_specialization)) {

                                update_Specializations.setSelection(i);
                                specialization_on_top = Specializations[0];
                                specialization_we_got = Specializations[i];
                                Specializations[0] = specialization_we_got;
                                Specializations[i] = specialization_on_top;

                                ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(updateDoctorProfile.this, spinner_item,Specializations);
                                arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                update_Specializations.setAdapter(arrayAdapterSpecialization);

                            }
                        }

                }
                catch (Exception e){
                    Toast.makeText(updateDoctorProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                try {
                    for (int i =0; i<=Genders.length; i++){

                        if(Genders[i].equals(Selected_gender)){
                            update_gender.setSelection(i);
                            String Gender_on_top = Genders[0];
                            String GENDER_we_got = Genders[i];
                            Genders[0] = GENDER_we_got;
                            Genders[1] = Gender_on_top;

                            ArrayAdapter gender_adapter = new ArrayAdapter(updateDoctorProfile.this,spinner_item,Genders);
                            gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            update_gender.setAdapter(gender_adapter);
                        }


                    }
                }
                catch (Exception e){
                    Toast.makeText(updateDoctorProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

        });



        Update.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                try {
                    if (update_full_name.getText().toString().isEmpty() && update_email_address.getText().toString().isEmpty() && update_Password.getText().toString().isEmpty() && update_Phone_No.getText().toString().isEmpty() && update_License.getText().toString().isEmpty() && update_Specializations.getSelectedItem().toString().isEmpty() && update_gender.getSelectedItem().toString().isEmpty()) {

                        update_full_name.setError("Full Name can't be empty");
                        update_email_address.setError("Email Address can't be empty");
                        update_Password.setError("Password can't be empty");
                        update_Phone_No.setError("Phone no can't be empty");
                        update_License.setError("license No can't be empty");
                        ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");
                        ((TextView) update_gender.getSelectedView()).setError("Select at least one!");
                    }

                } catch (Exception e) {
                    Toast.makeText(updateDoctorProfile.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

//---------------------------------------FULL NAME LOGIC----------------------------------------------------------------------------
                if (update_full_name.getText().toString().isEmpty()) {

                    update_full_name.setError("First Name can't be empty");


                    if (update_email_address.getText().toString().isEmpty()) {
                        update_email_address.setError("Email Address can't be empty");
                    }

                    if (update_Password.getText().toString().isEmpty()) {
                        update_Password.setError("Password can't be empty");

                    }

                    if (update_Phone_No.getText().toString().isEmpty()) {
                        update_Phone_No.setError("Phone no can't be empty");

                    }

                    if (update_License.getText().toString().isEmpty()) {
                        update_License.setError("license No can't be empty");
                    }


                    if (update_Specializations.getSelectedItem().toString().isEmpty()) {
                        ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");
                    }

                }


//-------------------------------------------------------Email Address logic--------------------------------------------------
                else if (update_email_address.getText().toString().isEmpty()) {

                    update_email_address.setError("Email Address can't be empty");


                    if (update_full_name.getText().toString().isEmpty()) {

                        update_full_name.setError("First Name can't be empty");
                    }


                    if (update_Password.getText().toString().isEmpty()) {
                        update_Password.setError("Password can't be empty");

                    }


                    if (update_Phone_No.getText().toString().isEmpty()) {
                        update_Phone_No.setError("Phone no can't be empty");

                    }

                    if (update_License.getText().toString().isEmpty()) {
                        update_License.setError("license No can't be empty");
                    }


                    if (update_Specializations.getSelectedItem().toString().isEmpty()) {
                        ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");
                    }

                }

//-----------------------------------------------------------Password Logic-------------------------------------------------
                else if (update_Password.getText().toString().isEmpty()) {
                    update_Password.setError("Password can't be empty");


                    if (update_full_name.getText().toString().isEmpty()) {

                        update_full_name.setError("First Name can't be empty");
                    }

                    if (update_email_address.getText().toString().isEmpty()) {
                        update_email_address.setError("Email Address can't be empty");
                    }

                    if (update_Phone_No.getText().toString().isEmpty()) {
                        update_Phone_No.setError("Phone no can't be empty");

                    }

                    if (update_License.getText().toString().isEmpty()) {
                        update_License.setError("license No can't be empty");
                    }

                    if (update_Specializations.getSelectedItem().toString().isEmpty()) {
                        ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");
                    }

                }
//-------------------------------------------------Phone No logic--------------------------------------
                else if (update_Phone_No.getText().toString().isEmpty()) {

                    update_Phone_No.setError("Phone no can't be empty");
                    ;


                    if (update_full_name.getText().toString().isEmpty()) {

                        update_full_name.setError("First Name can't be empty");
                    }

                    if (update_email_address.getText().toString().isEmpty()) {
                        update_email_address.setError("Email Address can't be empty");
                    }

                    if (update_Password.getText().toString().isEmpty()) {
                        update_Password.setError("Password can't be empty");
                    }

                    if (update_License.getText().toString().isEmpty()) {
                        update_License.setError("license No can't be empty");
                    }

                    if (update_Specializations.getSelectedItem().toString().isEmpty()) {
                        ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");
                    }

                }
//-------------------------------------------------Gender--------------------------------------
                else if (update_gender.getSelectedItem().toString().isEmpty()) {

                    ((TextView) update_gender.getSelectedView()).setError("Select at least one!");

                    if (update_full_name.getText().toString().isEmpty()) {

                        update_full_name.setError("First Name can't be empty");
                    }

                    if (update_email_address.getText().toString().isEmpty()) {
                        update_email_address.setError("Email Address can't be empty");
                    }


                    if (update_Password.getText().toString().isEmpty()) {
                        update_Password.setError("Password can't be empty");
                    }

                    if (update_Phone_No.getText().toString().isEmpty()) {
                        update_Phone_No.setError("Phone no can't be empty");
                    }

                    if (update_License.getText().toString().isEmpty()) {
                        update_License.setError("license No can't be empty");
                    }
                    if (update_Specializations.getSelectedItem().toString().isEmpty()) {
                        ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");
                    }
                }

//---------------------------------------------LICENSE Logic------------------------------------------------------
                else if (update_License.getText().toString().isEmpty()) {


                    update_License.setError("license No can't be empty");


                    if (update_full_name.getText().toString().isEmpty()) {

                        update_full_name.setError("First Name can't be empty");
                    }

                    if (update_email_address.getText().toString().isEmpty()) {
                        update_email_address.setError("Email Address can't be empty");
                    }

                    if (update_Password.getText().toString().isEmpty()) {
                        update_Password.setError("Password can't be empty");
                    }

                    if (update_Phone_No.getText().toString().isEmpty()) {
                        update_Phone_No.setError("Phone no can't be empty");
                    }

                    if (update_Specializations.getSelectedItem().toString().isEmpty()) {
                        ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");
                    }

                }
//-------------------------------------------Specializations---------------------------------------------
                else if (update_Specializations.getSelectedItem().toString().isEmpty()) {

                    ((TextView) update_Specializations.getSelectedView()).setError("Select at least one!");

                    if (update_full_name.getText().toString().isEmpty()) {

                        update_full_name.setError("First Name can't be empty");
                    }

                    if (update_email_address.getText().toString().isEmpty()) {
                        update_email_address.setError("Email Address can't be empty");
                    }


                    if (update_Password.getText().toString().isEmpty()) {
                        update_Password.setError("Password can't be empty");
                    }

                    if (update_Phone_No.getText().toString().isEmpty()) {
                        update_Phone_No.setError("Phone no can't be empty");
                    }

                    if (update_License.getText().toString().isEmpty()) {
                        update_License.setError("license No can't be empty");
                    }
                    if (update_gender.getSelectedItem().toString().isEmpty()) {
                        ((TextView) update_gender.getSelectedView()).setError("Select at least one!");
                    }

                } else {


                    update_fName = update_full_name.getText().toString();
                    update_emailAddress = update_email_address.getText().toString();
                    update_Passcode = update_Password.getText().toString();
                    update_phoneNo = update_Phone_No.getText().toString();
                    update_license = update_License.getText().toString();
                    update_specializations = update_Specializations.getSelectedItem().toString();
                    update_gend = update_gender.getSelectedItem().toString();
                    update_bio = update_doctor_bio.getText().toString();


                    doctor.put("Full Name",update_fName);
                    doctor.put("Email Address",update_emailAddress);
                    doctor.put("Password",update_Passcode);
                    doctor.put("Phone #",update_phoneNo);
                    doctor.put("Gender",update_gend);
                    doctor.put("License #",update_license);
                    doctor.put("Specialization",update_specializations);
                    doctor.put("Bio Details",update_bio);


                    AuthCredential credential = EmailAuthProvider
                            .getCredential(old_email, old_pass); // Current Login Credentials \\
                    // Prompt the user to re-provide their sign-in credentials
                    doctor_user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "User re-authenticated.");
                                    //Now change your email address \\
                                    //----------------Code for Changing Email Address----------\\

                                    doctor_user.updateEmail(update_emailAddress)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User email address updated.");
                                                    }
                                                }
                                            });
                                    doctor_user.updatePassword(update_Passcode).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User Password updated.");
                                            }
                                        }
                                    });
                                }
                            });
                    if(!old_email.equals(update_emailAddress)){
                        doctor_user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(updateDoctorProfile.this, "Email sent to: " + update_emailAddress+ ". Please verify it!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    documentReference.set(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(updateDoctorProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });


    }


    private void profileChange(){
        storageReference = FirebaseStorage.getInstance().getReference();


        StorageReference doc_file_ref = storageReference.child("Doctor/"+fAuth.getCurrentUser().getUid()+"/doctor_profile.jpg");
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

        if(requestCode == GALLERY_CODE){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                Uri content_uri = data.getData();
                doctor_profile.setImageURI(content_uri);

                uploadProfileToFireBase(content_uri);
            }
        }
    }

    private void uploadProfileToFireBase(Uri content_uri) {

        StorageReference doc_file_ref = storageReference.child("Doctor/"+ Objects.requireNonNull(fAuth.getCurrentUser()).getUid()+"/doctor_profile.jpg");
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
                Toast.makeText(updateDoctorProfile.this, "Profile not uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

}





