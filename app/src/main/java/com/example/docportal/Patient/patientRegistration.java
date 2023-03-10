package com.example.docportal.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    TextView login;
    Button Register;

    String patient_full_name;
    String patient_email_Address;
    String patient_phone_no;
    String patient_password;

    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    String user_id;
    boolean patient_check = true;
    boolean doctor_check = false;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        patient_fullName = findViewById(R.id.patient_fullName);
        patient_emailAddress = findViewById(R.id.patient_emailAddress);
        patient_phoneNo = findViewById(R.id.patient_phoneNo);
        patient_Password = findViewById(R.id.patient_password);
        login = findViewById(R.id.login);
        Register = findViewById(R.id.Submit);
        TextView[] textViews = {patient_fullName, patient_emailAddress, patient_Password, patient_phoneNo};
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        CheckEvent checkEvent = new CheckEvent();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                bundle = new Bundle();
//                bundle.putBoolean("patient check",patient_check);
//                bundle.putBoolean("patient check",doctor_check);
                Intent intent = new Intent(patientRegistration.this,DocLogin.class);
//                intent.putExtras(bundle);
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
                                documentReference.set(patient);
                            }
                        }
                    });
                }
            }
        });


    }
}

