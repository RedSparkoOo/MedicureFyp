package com.example.docportal.Doctor;

import static android.content.ContentValues.TAG;
import static com.example.docportal.R.layout.spinner_item;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdateDoctorNurseProfile extends AppCompatActivity {

    Button Update;
    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int CAMERA_CODE = 101;
    public static final int REQUEST_CODE = 100;
    public static final int GALLERY_CODE = 1000;
    String token;
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
    String user_id;
    String update_fName;
    String update_emailAddress;
    String update_Passcode;
    String update_phoneNo;
    String update_license;
    String update_specializations;
    String update_gend;
    String update_bio;
    String update_profession;
    FirebaseFirestore firestore;
    FirebaseAuth fAuth;
    String specialization_on_top;
    String specialization_we_got;
    String present_specialization;
    String Selected_gender;
    String selected_profession;
    String[] Doctor_profession = {"Cardiologist","Oncologist","Nephrologist","Neurologist","Pedriatican","physiologist"};
    String[] Nurse_profession = {"Mental Health Nurse (MHN)","Learning Disability Nurse (LDN)","Adult Nurse (AN)","Children Nurse (CN)","Critical Care Nurse (CCN)"};
    String[] Genders = {"Male","Female"};
    String[] Profession = {"Doctor","Nurse"} ;
    String[] Empty = {};
    StorageReference storageReference;
    FirebaseUser doctor_user;
    String old_email;
    String old_pass;
    String update_starting_time;
    String update_closing_time;
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
        update_gender = findViewById(R.id.update_doctor_gender);
        doctor_profile = findViewById(R.id.doctor_profile);
        update_doctor_bio = findViewById(R.id.update_doctor_bio);
        update_start_time = findViewById(R.id.update_start_time);
        update_close_time = findViewById(R.id.update_close_time);
        update_profession_category = findViewById(R.id.update_profession_category);


        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user_id = fAuth.getCurrentUser().getUid();
        doctor_user = fAuth.getCurrentUser();
        Map<String,Object> doctor = new HashMap<>();
        documentReference = firestore.collection("Professions").document(user_id);

        update_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStartTime();
            }
        });

        update_close_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCloseTime();
            }
        });

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



                    if (update_full_name.getText().toString().isEmpty() && update_email_address.getText().toString().isEmpty() && update_Password.getText().toString().isEmpty() && update_Phone_No.getText().toString().isEmpty() && update_License.getText().toString().isEmpty() && update_specialist_Category.getSelectedItem().toString().isEmpty() && update_gender.getSelectedItem().toString().isEmpty()) {

                        update_full_name.setError("Full Name can't be empty");
                        update_email_address.setError("Email Address can't be empty");
                        update_Password.setError("Password can't be empty");
                        update_Phone_No.setError("Phone no can't be empty");
                        update_License.setError("license No can't be empty");
                        ((TextView) update_specialist_Category.getSelectedView()).setError("Select at least one!");
                        ((TextView) update_gender.getSelectedView()).setError("Select at least one!");
                    }

                    else {

                    update_fName = update_full_name.getText().toString();
                    update_emailAddress = update_email_address.getText().toString();
                    update_Passcode = update_Password.getText().toString();
                    update_phoneNo = update_Phone_No.getText().toString();
                    update_license = update_License.getText().toString();
                    update_specializations = update_specialist_Category.getSelectedItem().toString();
                    update_profession = update_profession_category.getSelectedItem().toString();
                    update_starting_time = update_start_time.getText().toString();
                    update_closing_time = update_close_time.getText().toString();
                    update_gend = update_gender.getSelectedItem().toString();
                    update_bio = update_doctor_bio.getText().toString();


                    doctor.put("Full Name",update_fName);
                    doctor.put("Email Address",update_emailAddress);
                    doctor.put("Password",update_Passcode);
                    doctor.put("Phone #",update_phoneNo);
                    doctor.put("Gender",update_gend);
                    doctor.put("License #",update_license);
                    doctor.put("Doctor_profession",update_specializations);
                    doctor.put("Profession",update_profession);
                    doctor.put("Start Time",update_starting_time);
                    doctor.put("End Time",update_closing_time);
                    doctor.put("Bio Details",update_bio);


              try {
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
                              Toast.makeText(UpdateDoctorNurseProfile.this, "Email sent to: " + update_emailAddress+ ". Please verify it!", Toast.LENGTH_SHORT).show();
                          }
                      });
                  }


                  documentReference.set(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void unused) {
                          Toast.makeText(UpdateDoctorNurseProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                      }
                  });
              }
              catch (Exception e){
                  Toast.makeText(UpdateDoctorNurseProfile.this, e.toString(), Toast.LENGTH_SHORT).show();
              }


                }
            }
        });


    }

    private void updateProfessions() {

        ArrayAdapter GenderSelect = new ArrayAdapter(this,spinner_item,Genders);
        GenderSelect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_gender.setAdapter(GenderSelect);

        ArrayAdapter ProfessionSelect = new ArrayAdapter(this,spinner_item,Profession);
        ProfessionSelect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        update_profession_category.setAdapter(ProfessionSelect);



        update_gender.setPrompt("Select One");
        update_specialist_Category.setPrompt("Select One");
        update_profession_category.setPrompt("Select One");

        update_profession_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(update_profession_category.getSelectedItem().equals("")){
                    ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(UpdateDoctorNurseProfile.this, spinner_item, Empty);
                    arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    update_specialist_Category.setAdapter(arrayAdapterSpecialization);
                }

                if(update_profession_category.getSelectedItem().equals("Doctor")){
                    ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(UpdateDoctorNurseProfile.this, spinner_item, Doctor_profession);
                    arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    update_specialist_Category.setAdapter(arrayAdapterSpecialization);
                }

                if(update_profession_category.getSelectedItem().equals("Nurse")){
                    ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(UpdateDoctorNurseProfile.this, spinner_item, Nurse_profession);
                    arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    update_specialist_Category.setAdapter(arrayAdapterSpecialization);
                }

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
                present_specialization = documentSnapshot.getString("Doctor_profession");
                Selected_gender = documentSnapshot.getString("Gender");
                selected_profession = documentSnapshot.getString("Profession");
                update_doctor_bio.setText(documentSnapshot.getString("Bio Details"));
                update_start_time.setText(documentSnapshot.getString("Start Time"));
                update_close_time.setText(documentSnapshot.getString("End Time"));
                old_email = documentSnapshot.getString("Email Address");
                old_pass = documentSnapshot.getString("Password");

                        for (int i =0; i <Profession.length; i++){

                            if(Profession[i].equals(selected_profession)){

                                update_profession_category.setSelection(i);

                                ArrayAdapter doctorAdapter = new ArrayAdapter(UpdateDoctorNurseProfile.this,spinner_item,Profession);
                                doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                update_profession_category.setAdapter(doctorAdapter);

                            }


                        }

                        for (int i=0; i<Doctor_profession.length; i++) {


                            if (Doctor_profession[i].equals(present_specialization)) {

                                update_specialist_Category.setSelection(i);
                                specialization_on_top = Doctor_profession[0];
                                specialization_we_got = Doctor_profession[i];
                                Doctor_profession[0] = specialization_we_got;
                                Doctor_profession[i] = specialization_on_top;

                                update_specialist_Category.setSelection(i);
                                ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(UpdateDoctorNurseProfile.this, spinner_item,Doctor_profession);
                                arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                update_specialist_Category.setAdapter(arrayAdapterSpecialization);

                            }

                        }


                        for(int i =0; i <Profession.length; i++){
                            if(Profession[i].equals(selected_profession)) {
                                update_profession_category.setSelection(i);

                                ArrayAdapter arrayAdapterNurseProfession = new ArrayAdapter(UpdateDoctorNurseProfile.this, spinner_item, Profession);
                                arrayAdapterNurseProfession.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                update_profession_category.setAdapter(arrayAdapterNurseProfession);
                            }

                        }
                        for(int i=0; i<Nurse_profession.length; i++){

                            if (Nurse_profession[i].equals(present_specialization)) {

                                update_specialist_Category.setSelection(i);
                                specialization_on_top = Nurse_profession[0];
                                specialization_we_got = Nurse_profession[i];
                                Nurse_profession[0] = specialization_we_got;
                                Nurse_profession[i] = specialization_on_top;

                                ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(UpdateDoctorNurseProfile.this, spinner_item,Nurse_profession);
                                arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                update_specialist_Category.setAdapter(arrayAdapterSpecialization);

                            }
                        }







                try {
                    for (int i =0; i<=Genders.length; i++){

                        if(Genders[i].equals(Selected_gender)){
                            update_gender.setSelection(i);

                            ArrayAdapter gender_adapter = new ArrayAdapter(UpdateDoctorNurseProfile.this,spinner_item,Genders);
                            gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            update_gender.setAdapter(gender_adapter);
                        }


                    }
                }
                catch (Exception e){

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
                Toast.makeText(UpdateDoctorNurseProfile.this, "Profile not uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCloseTime() {
        Calendar calendar = Calendar.getInstance();
        int start_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int start_minute = calendar.get((Calendar.MINUTE));

        //  boolean Is24hourFormat = DateFormat.is24HourFormat(AppointmentBooking.this);
        TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateDoctorNurseProfile.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {

                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                int hourOfDay = timePicker.getHour();
                int minute = timePicker.getMinute();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                update_closing_time = timeFormat.format(calendar.getTime());
                update_close_time.setText(update_closing_time);
            }
        },start_hour,start_minute,false);
        timePickerDialog.show();

    }

    private void updateStartTime() {

        Calendar calendar = Calendar.getInstance();
        int start_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int start_minute = calendar.get((Calendar.MINUTE));

        //  boolean Is24hourFormat = DateFormat.is24HourFormat(AppointmentBooking.this);
        TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateDoctorNurseProfile.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {

                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                int hourOfDay = timePicker.getHour();
                int minute = timePicker.getMinute();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                update_starting_time = timeFormat.format(calendar.getTime());
                update_start_time.setText(update_starting_time);
            }
        },start_hour,start_minute,false);
        timePickerDialog.show();
    }

}





