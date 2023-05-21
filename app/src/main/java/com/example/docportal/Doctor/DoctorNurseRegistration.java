package com.example.docportal.Doctor;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.docportal.Patient.PatientRegistration;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class DoctorNurseRegistration extends AppCompatActivity {
    Button Confirm;
    EditText full_name;
    EditText email_address;
    EditText Password;
    EditText Phone_No;
    EditText License;
    Spinner Specializations;
    Spinner doctor_gender;
    Spinner professionCategory;
    EditText doctor_bio;
    TextView login;

    String[] Doctor_profession = {"Cardiologist","Nephrologist", "Neurologist","Physiologist","Psychologist"};
    String[] Nurse_profession = {"Mental Health Nurse (MHN)", "Learning Disability Nurse (LDN)", "Adult Nurse (AN)", "Children Nurse (CN)", "Critical Care Nurse (CCN)"};
    String[] Gender = {"Male", "Female"};
    String[] Profession = {"Doctor", "Nurse"};
    String[] Empty = {};
    Singleton singleton = new Singleton();

    ImageView back_to_doctorPage;
    String starting_time;
    String closing_time;
    FirestoreHandler firestoreHandler = new FirestoreHandler();

    String fName;
    String emailAddress;
    String Passcode;
    String phoneNo;
    String license;

    String specializations;
    String Selected_Profession;
    String gender;
    String Bio;
    CollectionReference patient_documentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_nurse_registration);
        Confirm = findViewById(R.id.Submit);
        full_name = findViewById(R.id.firstName);
        email_address = findViewById(R.id.emailAddress);
        Password = findViewById(R.id.password);
        Phone_No = findViewById(R.id.phoneNo);
        License = findViewById(R.id.license);
        Specializations = findViewById(R.id.specialistCategory);
        doctor_gender = findViewById(R.id.doctor_gender);
        professionCategory = findViewById(R.id.professionCategory);
        doctor_bio = findViewById(R.id.doctor_bio);
        login = findViewById(R.id.login);
        back_to_doctorPage = findViewById(R.id.back_to_doctorPage);



        back_to_doctorPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(DoctorNurseRegistration.this, MainActivity.class);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(DoctorNurseRegistration.this, DoctorLogin.class);
            }
        });


        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView[] textViews = {full_name, email_address, Password, Phone_No, License};
                CheckEvent checkEvent = new CheckEvent();

                if (checkEvent.isEmpty(textViews) || !checkEvent.checkName(full_name) || !checkEvent.checkEmail(email_address) || !checkEvent.checkPhone(Phone_No) || !checkEvent.checkLicense(License)) {
                    Toast.makeText(DoctorNurseRegistration.this, "Please fill out the data correctly", Toast.LENGTH_SHORT).show();
                }

                else {
                    fName = full_name.getText().toString();
                    emailAddress = email_address.getText().toString();
                    Passcode = Password.getText().toString();
                    phoneNo = Phone_No.getText().toString();
                    license = License.getText().toString();

                    specializations = Specializations.getSelectedItem().toString();
                    Selected_Profession = professionCategory.getSelectedItem().toString();
                    gender = doctor_gender.getSelectedItem().toString();
                    Bio = doctor_bio.getText().toString();
//                   registerUser();
                   checkUser();
                }
            }
        });


        ProfessionSelection();


    }

    private void checkUser() {
        patient_documentReference = firestoreHandler.getFirestoreInstance().collection("Professions");
        patient_documentReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    for (DocumentSnapshot document : value.getDocuments()) {
                        String email = document.getString("Email Address");
                        if (email != null && email.equals(emailAddress)) {
                            Toast.makeText(DoctorNurseRegistration.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    registerUser();
                }
            }
        });
    }

    private void registerUser() {


            firestoreHandler.getFirebaseAuth().createUserWithEmailAndPassword(emailAddress, Passcode).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        firestoreHandler.getFirebaseUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                singleton.showToast(DoctorNurseRegistration.this, "Email sent to: " + emailAddress);
                            }
                        });
                        DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Professions").document(firestoreHandler.getCurrentUser());
                        Map<String, Object> doctor = new HashMap<>();
                        doctor.put("Full Name", fName);
                        doctor.put("Email Address", emailAddress);
                        doctor.put("Password", Passcode);
                        doctor.put("Phone #", phoneNo);
                        doctor.put("Gender", gender);
                        doctor.put("License #", license);
                        doctor.put("Profession", Selected_Profession);
                        doctor.put("Specialization", specializations);
                        doctor.put("Bio Details", Bio);
                        documentReference.set(doctor);
                        singleton.showToast(DoctorNurseRegistration.this, "Registration Successful");
                        singleton.showToast(DoctorNurseRegistration.this, "Link sent to: "+emailAddress);

                        Intent intent = new Intent(DoctorNurseRegistration.this,DoctorLogin.class);
                        startActivity(intent);


                    }
                }
            });

        }

    private void ProfessionSelection() {
        singleton.setAdatper(this, doctor_gender, Gender);
        singleton.setAdatper(this, professionCategory, Profession);


        doctor_gender.setPrompt("Select One");
        Specializations.setPrompt("Select One");
        professionCategory.setPrompt("Select One");


        professionCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (professionCategory.getSelectedItem().equals(""))
                    singleton.setAdatper(DoctorNurseRegistration.this, Specializations, Empty);
                if (professionCategory.getSelectedItem().equals("Doctor"))
                    singleton.setAdatper(DoctorNurseRegistration.this, Specializations, Doctor_profession);
                if (professionCategory.getSelectedItem() == "Nurse")
                    singleton.setAdatper(DoctorNurseRegistration.this, Specializations, Nurse_profession);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Specializations.setEnabled(false);
            }
        });

    }

    private void startTimeSelection() {
        Calendar calendar = Calendar.getInstance();
        int start_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int start_minute = calendar.get((Calendar.MINUTE));

        //  boolean Is24hourFormat = DateFormat.is24HourFormat(AppointmentBooking.this);
        TimePickerDialog timePickerDialog = new TimePickerDialog(DoctorNurseRegistration.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {

                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                int hourOfDay = timePicker.getHour();
                int minute = timePicker.getMinute();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                starting_time = timeFormat.format(calendar.getTime());

            }
        }, start_hour, start_minute, false);
        timePickerDialog.show();


    }

    private void closeTimeSelection() {

        Calendar calendar = Calendar.getInstance();
        int start_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int start_minute = calendar.get((Calendar.MINUTE));

        //  boolean Is24hourFormat = DateFormat.is24HourFormat(AppointmentBooking.this);
        TimePickerDialog timePickerDialog = new TimePickerDialog(DoctorNurseRegistration.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {

                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                int hourOfDay = timePicker.getHour();
                int minute = timePicker.getMinute();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                closing_time = timeFormat.format(calendar.getTime());

            }
        }, start_hour, start_minute, false);
        timePickerDialog.show();
    }
}









