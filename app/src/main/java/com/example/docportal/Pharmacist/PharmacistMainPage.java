package com.example.docportal.Pharmacist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.docportal.R;
import com.example.docportal.mainstartScreen;

public class PharmacistMainPage extends AppCompatActivity {

    ImageView back_to_entrance;
    Button SignUp;
    Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacist_main_page);

        back_to_entrance = findViewById(R.id.back_to_entrance);
        SignUp = findViewById(R.id.SignUp);
        Login = findViewById(R.id.Login);
        back_to_entrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmacistMainPage.this, mainstartScreen.class);
                startActivity(intent);
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmacistMainPage.this,PharmacistRegistration.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmacistMainPage.this,PharmacistLogin.class);
                startActivity(intent);
            }
        });


    }
}