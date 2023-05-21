package com.example.docportal.Patient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.CheckEvent;
import com.example.docportal.Doctor.DoctorLogin;
import com.example.docportal.FirestoreHandler;
import com.example.docportal.Pharmacist.PharmacistRegistration;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientRegistration extends AppCompatActivity {
    EditText patient_fullName;
    EditText patient_emailAddress;
    EditText patient_phoneNo;
    EditText patient_Password;
    Spinner patient_gender;
    TextView login;
    Button Register;
    ImageView back_to_patientpage;
    Singleton singleton = new Singleton();

    String patient_full_name;
    String patient_email_Address;
    String patient_phone_no;
    String patient_password;
    String patient_selected_gender;
    String[] Gender = {"Male", "Female"};

    FirestoreHandler firestoreHandler = new FirestoreHandler();
    CollectionReference patient_documentReference;

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


        CheckEvent checkEvent = new CheckEvent();

        singleton.setAdatper(this, patient_gender, Gender);


        back_to_patientpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(PatientRegistration.this, patientMainPage.class);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(PatientRegistration.this, DoctorLogin.class);

            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEvent.isEmpty(textViews) || !checkEvent.checkName(patient_fullName) || !checkEvent.checkPhone(patient_phoneNo) || !checkEvent.checkEmail(patient_emailAddress));

                else {
                    patient_full_name = patient_fullName.getText().toString();
                    patient_email_Address = patient_emailAddress.getText().toString();
                    patient_password = patient_Password.getText().toString();
                    patient_phone_no = patient_phoneNo.getText().toString();
                    patient_selected_gender = patient_gender.getSelectedItem().toString();
                    checkUser();

                }
            }
        });


    }

    private void checkUser() {
        patient_documentReference = firestoreHandler.getFirestoreInstance().collection("Patient");
        patient_documentReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    for (DocumentSnapshot document : value.getDocuments()) {
                        String email = document.getString("Patient Email Address");
                        if (email != null && email.equals(patient_email_Address)) {
                            Toast.makeText(PatientRegistration.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    createUser();
                }
            }
        });
    }

    private void createUser() {

        firestoreHandler.getFirebaseAuth().createUserWithEmailAndPassword(patient_email_Address, patient_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    firestoreHandler.getFirebaseUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            singleton.showToast(PatientRegistration.this, "Email sent to: " + patient_email_Address);
                        }
                    });
                    singleton.showToast(PatientRegistration.this, "User Created");

                    DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Patients").document(firestoreHandler.getCurrentUser());
                    Map<String, Object> patient = new HashMap<>();
                    patient.put("Patient Name", patient_full_name);
                    patient.put("Patient Email Address", patient_email_Address);
                    patient.put("Patient Password", patient_password);
                    patient.put("Patient phone_no", patient_phone_no);
                    patient.put("Patient Gender", patient_selected_gender);
                    documentReference.set(patient);
                }
            }
        });


    }
}

