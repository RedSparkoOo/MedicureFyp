package com.example.docportal;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class mainstartScreen extends AppCompatActivity {
    Button toEntrance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainstart_screen);
        toEntrance = findViewById(R.id.toEntrance);
        toEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainstartScreen.this,Entrance.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
        }
    }
