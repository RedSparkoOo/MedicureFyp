package com.example.docportal.Doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.R;


public class MainActivity extends AppCompatActivity {
Button login;
Button Signup;
Bundle doctor;
boolean doctor_check = true;
boolean patient_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login = (Button) findViewById(R.id.Login);
        Signup = (Button) findViewById(R.id.SignUp);



        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,Registeration.class);
                startActivity(intent);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this,DocLogin.class);
                doctor = new Bundle();
                doctor.putBoolean("doctor_check",doctor_check);
                doctor.putBoolean("patient_check",patient_check);
                intent.putExtras(doctor);
                startActivity(intent);



            }
        });


    }
}