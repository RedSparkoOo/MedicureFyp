package com.example.docportal.Doctor;

import static com.example.docportal.R.layout.spinner_item;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.docportal.CheckEvent;
import com.example.docportal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    Button upload_profile;
    EditText full_name;
    EditText email_address;
    EditText Password;
    EditText Phone_No;
    EditText License;
    Spinner Specializations;
    TextView login;
    TextView Lic_head;
    TextView Cat_head;
    String user_id;
    String[] Specialization = {"","Pharmacist","Cardiologist","Oncologist","Nephrologist","Neurologist","Pedriatican","physiologist","psychologist"};
    String fName;
    String emailAddress;
    String Passcode;
    String phoneNo;
    String license;
    String specializations;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        Lic_head = findViewById(R.id.Lic_head);
        Cat_head = findViewById(R.id.Cat_head);
        Confirm = findViewById(R.id.Submit);
        full_name = findViewById(R.id.firstName);
        email_address = findViewById(R.id.emailAddress);
        Password = findViewById(R.id.password);
        Phone_No = findViewById(R.id.phoneNo);
        License = findViewById(R.id.license);
        Specializations = findViewById(R.id.specialistCategory);
        login = findViewById(R.id.login);
        TextView[] textViews = {full_name, email_address, Password, Phone_No, License};
        CheckEvent checkEvent = new CheckEvent();

        ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(this, spinner_item, Specialization);
        arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Specializations.setAdapter(arrayAdapterSpecialization);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registeration.this, DocLogin.class));
            }
        });
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkEvent.isEmpty(textViews) || !(checkEvent.checkName(full_name) || checkEvent.checkPhone(Phone_No) || checkEvent.checkEmail(email_address) || checkEvent.checkPassword(Password)));
                else {
                    fName = full_name.getText().toString();
                    emailAddress = email_address.getText().toString();
                    Passcode = Password.getText().toString();
                    phoneNo = Phone_No.getText().toString();
                    license = License.getText().toString();
                    specializations = Specializations.getSelectedItem().toString();
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
                                Toast.makeText(Registeration.this, "User Created", Toast.LENGTH_SHORT).show();
                                user_id = firebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = firestore.collection("Doctor").document(user_id);
                                Map<String, Object> doctor = new HashMap<>();
                                doctor.put("Full Name", fName);
                                doctor.put("Email Address", emailAddress);
                                doctor.put("Password", Passcode);
                                doctor.put("Phone #", phoneNo);
                                doctor.put("License #", license);
                                doctor.put("Specialization", specializations);
                                documentReference.set(doctor);
                            }
                        }
                    });
                }
                   }

        });
    }
}









