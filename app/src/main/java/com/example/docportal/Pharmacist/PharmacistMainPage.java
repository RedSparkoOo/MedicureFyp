package com.example.docportal.Pharmacist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.docportal.Doctor.DoctorLogin;
import com.example.docportal.Doctor.DoctorNurseRegistration;
import com.example.docportal.Doctor.MainActivity;
import com.example.docportal.Entrance;
import com.example.docportal.R;

public class PharmacistMainPage extends AppCompatActivity {
    Button login;
    Button Signup;
    Bundle doctor;
    ImageView back_to_entrance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_main_page);

        login = (Button) findViewById(R.id.Login_p);
        Signup = (Button) findViewById(R.id.SignUp_p);
        back_to_entrance = findViewById(R.id.back_to_entrance);

        back_to_entrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmacistMainPage.this, Entrance.class);
                startActivity(intent);
            }
        });



        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PharmacistMainPage.this, PharmacistRegistration.class);
                startActivity(intent);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(PharmacistMainPage.this, PharmacistLogin.class);
                startActivity(intent);



            }
        });


    }
}