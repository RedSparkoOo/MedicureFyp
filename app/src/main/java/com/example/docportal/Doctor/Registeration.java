package com.example.docportal.Doctor;

import static com.example.docportal.R.layout.spinner_item;

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

import java.util.HashMap;
import java.util.Map;

public class Registeration extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
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
        TextView[] textViews = {full_name, email_address, Password, Phone_No, License};
        CheckEvent checkEvent = new CheckEvent();



        ProfessionSelection();

        back_to_doctorPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registeration.this,MainActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Registeration.this,DocLogin.class);
                startActivity(intent);
            }
        });
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                                        Toast.makeText(Registeration.this, "Email sent to: " + emailAddress, Toast.LENGTH_SHORT).show();
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
                                doctor.put("Bio Details", Bio);
                                documentReference.set(doctor);
                                Toast.makeText(Registeration.this, "Registration Successful", Toast.LENGTH_SHORT).show();


//                                if(Selected_Profession.equals("Nurse")){
//                                    DocumentReference documentReference = firestore.collection("Nurse").document(user_id);
//                                    Map<String, Object> nurse = new HashMap<>();
//                                    nurse.put("Full Name", fName);
//                                    nurse.put("Email Address", emailAddress);
//                                    nurse.put("Password", Passcode);
//                                    nurse.put("Phone #", phoneNo);
//                                    nurse.put("Gender",gender);
//                                    nurse.put("License #", license);
//                                    nurse.put("Profession",Selected_Profession);
//                                    nurse.put("Nurse_profession", specializations);
//                                    nurse.put("Bio Details", Bio);
//                                    documentReference.set(nurse);
//                                    Toast.makeText(Registeration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                                }
//
//                                if(Selected_Profession.equals("Pharmacist")){
//                                    DocumentReference documentReference = firestore.collection("Nurse").document(user_id);
//                                    Map<String, Object> pharmacist = new HashMap<>();
//                                    pharmacist.put("Full Name", fName);
//                                    pharmacist.put("Email Address", emailAddress);
//                                    pharmacist.put("Password", Passcode);
//                                    pharmacist.put("Phone #", phoneNo);
//                                    pharmacist.put("Gender",gender);
//                                    pharmacist.put("License #", license);
//                                    pharmacist.put("Profession",Selected_Profession);
//                                    pharmacist.put("Pharmacist_profession", specializations);
//                                    pharmacist.put("Bio Details", Bio);
//                                    documentReference.set(pharmacist);
//                                    Toast.makeText(Registeration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                                }

                                if(Selected_Profession.equals("")){
                                    Toast.makeText(Registeration.this, "Please Select a Profession", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                    });

                }
            }


        });
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
                    ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(Registeration.this, spinner_item, Empty);
                    arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Specializations.setAdapter(arrayAdapterSpecialization);
                }

                if(professionCategory.getSelectedItem().equals("Doctor")){
                    ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(Registeration.this, spinner_item, Doctor_profession);
                    arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Specializations.setAdapter(arrayAdapterSpecialization);
                }

                if(professionCategory.getSelectedItem() == "Nurse"){
                    ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(Registeration.this, spinner_item, Nurse_profession);
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
}









