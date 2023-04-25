package com.example.docportal.Pharmacist;

import static com.example.docportal.R.layout.spinner_item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class PharmacistRegistration extends AppCompatActivity {
    Button Confirm;
    EditText full_name;
    EditText email_address;
    EditText Password;
    EditText Phone_No;
    EditText License;
    Spinner Specializations;
    Spinner doctor_gender;
    EditText doctor_bio;
    TextView login;
    String user_id;
    String[] Pharmacist_profession = {"","Community pharmacist","Specialty drug pharmacist","Informatic pharmacist","Hospital pharmacist","Home care pharmacist"};
    String[] Gender = {"","Male","Female"} ;
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
    ImageView back_to_PharmacistPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_registration);

        Confirm = findViewById(R.id.Submit);
        full_name = findViewById(R.id.firstName);
        email_address = findViewById(R.id.emailAddress);
        Password = findViewById(R.id.password);
        Phone_No = findViewById(R.id.phoneNo);
        License = findViewById(R.id.license);
        Specializations = findViewById(R.id.specialistCategory);
        doctor_gender = findViewById(R.id.doctor_gender);
        doctor_bio = findViewById(R.id.doctor_bio);
        login = findViewById(R.id.login);
        back_to_PharmacistPage = findViewById(R.id.back_to_PharmacistPage);
        TextView[] textViews = {full_name, email_address, Password, Phone_No, License};
        CheckEvent checkEvent = new CheckEvent();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PharmacistRegistration.this, PharmacistLogin.class);
                startActivity(intent);
            }
        });

        ProfessionSelection();
        pharmacistRegistration();

    }

    private void pharmacistRegistration() {
        TextView[] textViews = {full_name, email_address, Password, Phone_No, License};
        CheckEvent checkEvent = new CheckEvent();
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
                                        Toast.makeText(PharmacistRegistration.this, "Email sent to: " + emailAddress, Toast.LENGTH_SHORT).show();
                                    }
                                });

                                user_id = firebaseAuth.getCurrentUser().getUid();

                                DocumentReference documentReference = firestore.collection("Professions").document(user_id);
                                String Profession = "Pharmacist";
                                Map<String, Object> doctor = new HashMap<>();
                                doctor.put("Full Name", fName);
                                doctor.put("Email Address", emailAddress);
                                doctor.put("Password", Passcode);
                                doctor.put("Phone #", phoneNo);
                                doctor.put("Gender",gender);
                                doctor.put("License #", license);
                                doctor.put("Profession", Profession);
                                doctor.put("Pharmacist Category", specializations);
                                doctor.put("Bio Details", Bio);
                                documentReference.set(doctor);
                                Toast.makeText(PharmacistRegistration.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                if(Selected_Profession.equals("")){
                                    Toast.makeText(PharmacistRegistration.this, "Please Select a Profession", Toast.LENGTH_SHORT).show();
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

        ArrayAdapter Speciality = new ArrayAdapter(this,spinner_item,Pharmacist_profession);
        Speciality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Specializations.setAdapter(Speciality);

        doctor_gender.setPrompt("Select One");
        Specializations.setPrompt("Select One");

    }
}