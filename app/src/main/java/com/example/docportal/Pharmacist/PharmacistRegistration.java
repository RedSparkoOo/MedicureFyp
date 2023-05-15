package com.example.docportal.Pharmacist;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.CheckEvent;
import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;

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
    String[] Pharmacist_profession = {"", "Community pharmacist", "Specialty drug pharmacist", "Informatic pharmacist", "Hospital pharmacist", "Home care pharmacist"};
    String[] Gender = {"", "Male", "Female"};
    String fName;
    String emailAddress;
    String Passcode;
    String phoneNo;
    String license;
    String specializations;
    String gender;
    String Bio;
    Singleton singleton = new Singleton();


    FirestoreHandler firestoreHandler = new FirestoreHandler();
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


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(PharmacistRegistration.this, PharmacistLogin.class);
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

                if (checkEvent.isEmpty(textViews) || !(checkEvent.checkName(full_name) || checkEvent.checkPhone(Phone_No) || checkEvent.checkEmail(email_address) || checkEvent.checkPassword(Password)))
                    ;

                else {
                    fName = full_name.getText().toString();
                    emailAddress = email_address.getText().toString();
                    Passcode = Password.getText().toString();
                    phoneNo = Phone_No.getText().toString();
                    license = License.getText().toString();
                    specializations = Specializations.getSelectedItem().toString();
                    gender = doctor_gender.getSelectedItem().toString();
                    Bio = doctor_bio.getText().toString();


                    firestoreHandler.getFirebaseAuth().createUserWithEmailAndPassword(emailAddress, Passcode).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {


                                firestoreHandler.getFirebaseUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        singleton.showToast(PharmacistRegistration.this, "Email sent to: " + emailAddress);

                                    }
                                });

                                DocumentReference documentReference = firestoreHandler.getFirestoreInstance().collection("Professions").document(firestoreHandler.getCurrentUser());
                                String Profession = "Pharmacist";
                                Map<String, Object> doctor = new HashMap<>();
                                doctor.put("Full Name", fName);
                                doctor.put("Email Address", emailAddress);
                                doctor.put("Password", Passcode);
                                doctor.put("Phone #", phoneNo);
                                doctor.put("Gender", gender);
                                doctor.put("License #", license);
                                doctor.put("Profession", Profession);
                                doctor.put("Pharmacist Category", specializations);
                                doctor.put("Bio Details", Bio);
                                documentReference.set(doctor);
                                singleton.showToast(PharmacistRegistration.this, "Registration Successful");


                                if (specializations.equals(""))
                                    singleton.showToast(PharmacistRegistration.this, "Please Select a Profession");


                            }
                        }
                    });

                }
            }


        });


    }

    private void ProfessionSelection() {
        singleton.setAdatper(this, doctor_gender, Gender);
        singleton.setAdatper(this, Specializations, Pharmacist_profession);
        doctor_gender.setPrompt("Select One");
        Specializations.setPrompt("Select One");

    }
}