package com.example.docportal.Patient;

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
import com.example.docportal.Doctor.DocLogin;
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

public class patientRegistration extends AppCompatActivity {
    EditText patient_fullName;
    EditText patient_emailAddress;
    EditText patient_phoneNo;
    EditText patient_Password;
    Spinner patient_gender;
    TextView login;
    Button Register;
    ImageView back_to_patientpage;

    String patient_full_name;
    String patient_email_Address;
    String patient_phone_no;
    String patient_password;
    String patient_selected_gender;
    String Gender[] = {"","Male","Female"};
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String user_id;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        patient_fullName = findViewById(R.id.patient_fullName);
        patient_emailAddress = findViewById(R.id.patient_emailAddress);
        patient_phoneNo = findViewById(R.id.patient_phoneNo);
        patient_Password = findViewById(R.id.patient_password);
        patient_gender = findViewById(R.id.patient_gender);
        login = findViewById(R.id.login);
        Register = findViewById(R.id.Submit);
        back_to_patientpage = findViewById(R.id.back_to_patientpage);
        TextView[] textViews = {patient_fullName, patient_emailAddress, patient_Password, patient_phoneNo};
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        CheckEvent checkEvent = new CheckEvent();

        ArrayAdapter arrayAdapterSpecialization = new ArrayAdapter(this, spinner_item, Gender);
        arrayAdapterSpecialization.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patient_gender.setAdapter(arrayAdapterSpecialization);

        back_to_patientpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientRegistration.this,patientMainPage.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patientRegistration.this,DocLogin.class);
                startActivity(intent);

            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEvent.isEmpty(textViews) || !(checkEvent.checkName(patient_fullName) || checkEvent.checkPhone(patient_phoneNo) || checkEvent.checkEmail(patient_emailAddress) || checkEvent.checkPassword(patient_Password)));
                else {
                    patient_full_name = patient_fullName.getText().toString();
                    patient_email_Address = patient_emailAddress.getText().toString();
                    patient_password = patient_Password.getText().toString();
                    patient_phone_no = patient_phoneNo.getText().toString();
                    patient_selected_gender = patient_gender.getSelectedItem().toString();

                    firestore = FirebaseFirestore.getInstance();
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(patient_email_Address, patient_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser Patient = firebaseAuth.getCurrentUser();
                                Patient.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(patientRegistration.this, "Email sent to: " + patient_email_Address, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(patientRegistration.this, "User Created", Toast.LENGTH_SHORT).show();
                                user_id = firebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = firestore.collection("Patient").document(user_id);
                                Map<String, Object> patient = new HashMap<>();
                                patient.put("Patient Name", patient_full_name);
                                patient.put("Patient Email Address", patient_email_Address);
                                patient.put("Patient Password", patient_password);
                                patient.put("Patient phone_no", patient_phone_no);
                                patient.put("Patient Gender",patient_selected_gender);
                                documentReference.set(patient);
                            }
                        }
                    });
                }
            }
        });


    }
}

