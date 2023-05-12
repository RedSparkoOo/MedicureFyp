package com.example.docportal.Pharmacist;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.docportal.Entrance;
import com.example.docportal.R;
import com.example.docportal.Singleton;

public class PharmacistMainPage extends AppCompatActivity {
    Button login;
    Button Signup;
    Singleton singleton = new Singleton();
    ImageView back_to_entrance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_main_page);

        login = findViewById(R.id.Login_p);
        Signup = findViewById(R.id.SignUp_p);
        back_to_entrance = findViewById(R.id.back_to_entrance);

        back_to_entrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleton.openActivity(PharmacistMainPage.this, Entrance.class);

            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleton.openActivity(PharmacistMainPage.this, PharmacistRegistration.class);

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                singleton.openActivity(PharmacistMainPage.this, PharmacistLogin.class);


            }
        });


    }
}