package com.example.docportal.Doctor;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.Entrance;
import com.example.docportal.R;
import com.example.docportal.Singleton;


public class MainActivity extends AppCompatActivity {
    Button login;
    Button Signup;
    ImageView back_to_entrance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Singleton singleton = new Singleton();


        login = findViewById(R.id.Login);
        Signup = findViewById(R.id.SignUp);
        back_to_entrance = findViewById(R.id.back_to_entrance);

        back_to_entrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(MainActivity.this, Entrance.class);
            }
        });


        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleton.openActivity(MainActivity.this, DoctorNurseRegistration.class);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleton.openActivity(MainActivity.this, DoctorLogin.class);
            }
        });


    }
}