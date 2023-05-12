package com.example.docportal.Patient;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.CheckEvent;
import com.example.docportal.Entrance;
import com.example.docportal.FirestoreHandler;
import com.example.docportal.R;
import com.example.docportal.Singleton;


public class PatientLogin extends AppCompatActivity {

    Button login;
    TextView Register;
    EditText Email;
    EditText Password;


    TextView doctor_forget_password;

    ProgressBar progress_check;
    ImageView back_to_selection;
    Singleton singleton = new Singleton();
    FirestoreHandler firestoreHandler = new FirestoreHandler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        login = findViewById(R.id.doctorLogin);
        Register = findViewById(R.id.Registration);
        doctor_forget_password = (findViewById(R.id.doctorForgetPassword));
        Email = findViewById(R.id.doctorEmail);
        Password = findViewById(R.id.doctorPassword);
        progress_check = findViewById(R.id.progress_check);

        back_to_selection = findViewById(R.id.back_to_selection);

        progress_check.setVisibility(View.INVISIBLE);

        back_to_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(PatientLogin.this, Entrance.class);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleton.openActivity(PatientLogin.this, PatientRegistration.class);
            }
        });


        forgetPassword();
        Login();

    }

    private void Login() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress_check.setVisibility(View.VISIBLE);
                TextView[] textViews = {Email, Password};
                try {
                    if (new CheckEvent().isEmpty(textViews)) ;
                    else
                        firestoreHandler.firebaseLogin(PatientLogin.this, patientDashboard.class, Email.getText().toString(), Password.getText().toString(), "Patient", progress_check);
                } catch (Exception e) {
                    singleton.showToast(PatientLogin.this, e.getMessage());
                }
            }
        });

    }

    private void forgetPassword() {

        doctor_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreHandler.forgotPassword(view);
            }
        });


    }
}


