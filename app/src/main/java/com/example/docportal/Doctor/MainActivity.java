package com.example.docportal.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.docportal.R;


public class MainActivity extends AppCompatActivity {
Button login;
Button Signup;
LottieAnimationView DoctorAnimationView;
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
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Working", Toast.LENGTH_SHORT).show();

            }
        });


    }
}