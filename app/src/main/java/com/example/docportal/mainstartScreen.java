package com.example.docportal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class mainstartScreen extends AppCompatActivity {
    Button toEntrance;
    FirebaseFirestore firestore;
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

                firestore = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(false)
                        .build();
                firestore.setFirestoreSettings(settings);
            }
        });



    }

    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
        }
    }
