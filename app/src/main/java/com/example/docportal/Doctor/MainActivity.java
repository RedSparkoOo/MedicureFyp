package com.example.docportal.Doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.Entrance;
import com.example.docportal.R;


public class MainActivity extends AppCompatActivity {
    Button login;
    Button Signup;
    Bundle doctor;
    ImageView back_to_entrance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        login = (Button) findViewById(R.id.Login);
        Signup = (Button) findViewById(R.id.SignUp);
        back_to_entrance = findViewById(R.id.back_to_entrance);

        back_to_entrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Entrance.class);
                startActivity(intent);
            }
        });



        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,DoctorNurseRegistration.class);
                startActivity(intent);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, DoctorLogin.class);
                startActivity(intent);



            }
        });


    }
}