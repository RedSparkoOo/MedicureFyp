package com.example.docportal.Doctor;

import static com.example.docportal.R.layout.spinner_item;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.CheckEvent;
import com.example.docportal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
    EditText start_time;
    EditText close_time;
    Spinner Specializations;
    Spinner doctor_gender;
    Spinner professionCategory;
    EditText doctor_bio;
    TextView login;
    String user_id;
    String[] Doctor_profession = {"","Pharmacist","Cardiologist","Oncologist","Nephrologist","Neurologist","Pedriatican","Physiologist","Psychologist"};
    String[] Nurse_profession = {"","Mental Health Nurse (MHN)","Learning Disability Nurse (LDN)","Adult Nurse (AN)","Children Nurse (CN)","Critical Care Nurse (CCN)"};
    String[] Gender = {"","Male","Female"} ;
    String[] Profession = {"","Doctor","Nurse"} ;
    String[] Empty = {};
    String fName;
    String emailAddress;
    String Passcode;
    String phoneNo;
    String license;
    String specializations;
    String gender;
    String Bio;
    String Selected_Profession;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    ImageView back_to_doctorPage;
    String starting_time;
    String closing_time;



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
        professionCategory= findViewById(R.id.professionCategory);
        doctor_bio = findViewById(R.id.doctor_bio);
        login = findViewById(R.id.login);
        back_to_doctorPage = findViewById(R.id.back_to_doctorPage);
        start_time = findViewById(R.id.start_time);
        close_time = findViewById(R.id.close_time);



        back_to_doctorPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorNurseRegistration.this,MainActivity.class);
                startActivity(intent);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DoctorNurseRegistration.this, DoctorLogin.class);
                startActivity(intent);
            }
        });


        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimeSelection();
            }
        });

        close_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeTimeSelection();
            }
        });

        ProfessionSelection();


    }
    private void registerUser() {
        TextView[] textViews = {full_name, email_address, Password, Phone_No, License, start_time,close_time};
        CheckEvent checkEvent = new CheckEvent();

        if (checkEvent.isEmpty(textViews) || !(checkEvent.checkName(full_name) || checkEvent.checkPhone(Phone_No) || checkEvent.checkEmail(email_address) || checkEvent.checkPassword(Password)));


        else{
            fName = full_name.getText().toString();
            emailAddress = email_address.getText().toString();
            Passcode = Password.getText().toString();
            phoneNo = Phone_No.getText().toString();
            license = License.getText().toString();

            specializations = Specializations.getSelectedItem().toString();
            Selected_Profession = professionCategory.getSelectedItem().toString();
            gender = doctor_gender.getSelectedItem().toString();
            Bio = doctor_bio.getText().toString();
            firestore = FirebaseFirestore.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(emailAddress, Passcode).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser Doctor = firebaseAuth.getCurrentUser();
                        assert Doctor != null;
                        Doctor.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DoctorNurseRegistration.this, "Email sent to: " + emailAddress, Toast.LENGTH_SHORT).show();
                            }
                        });

                        user_id = firebaseAuth.getCurrentUser().getUid();

                        DocumentReference documentReference = firestore.collection("Professions").document(user_id);
                        Map<String, Object> doctor = new HashMap<>();
                        doctor.put("Full Name", fName);
                        doctor.put("Email Address", emailAddress);
                        doctor.put("Password", Passcode);
                        doctor.put("Phone #", phoneNo);
                        doctor.put("Gender",gender);
                        doctor.put("License #", license);
                        doctor.put("Profession",Selected_Profession);
                        doctor.put("Doctor_profession", specializations);
                        doctor.put("Start Time",starting_time);
                        doctor.put("Close Time",closing_time);
                        doctor.put("Bio Details", Bio);
                        documentReference.set(doctor);
                        Toast.makeText(DoctorNurseRegistration.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                        if(Selected_Profession.equals("")){
                            Toast.makeText(DoctorNurseRegistration.this, "Please Select a Profession", Toast.LENGTH_SHORT).show();

                        }

                    }
                }
            });

        }
    }

    private void ProfessionSelection() {

        ArrayAdapter GenderSelect = new ArrayAdapter(this,spinner_item,Gender);
        GenderSelect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctor_gender.setAdapter(GenderSelect);

        ArrayAdapter ProfessionSelect = new ArrayAdapter(this,spinner_item,Profession);
        ProfessionSelect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionCategory.setAdapter(ProfessionSelect);

        doctor_gender.setPrompt("Select One");
        Specializations.setPrompt("Select One");
        professionCategory.setPrompt("Select One");

        professionCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(professionCategory.getSelectedItem().equals("")){
                    ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(DoctorNurseRegistration.this, spinner_item, Empty);
                    arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Specializations.setAdapter(arrayAdapterSpecialization);
                }

                if(professionCategory.getSelectedItem().equals("Doctor")){
                    ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(DoctorNurseRegistration.this, spinner_item, Doctor_profession);
                    arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Specializations.setAdapter(arrayAdapterSpecialization);
                }

                if(professionCategory.getSelectedItem() == "Nurse"){
                    ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(DoctorNurseRegistration.this, spinner_item, Nurse_profession);
                    arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Specializations.setAdapter(arrayAdapterSpecialization);
                }



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
                start_time.setText(starting_time);
            }
        },start_hour,start_minute,false);
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
                close_time.setText(closing_time);
            }
        },start_hour,start_minute,false);
        timePickerDialog.show();
    }
}









