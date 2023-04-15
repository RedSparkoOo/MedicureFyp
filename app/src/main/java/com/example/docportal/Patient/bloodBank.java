package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.docportal.R;

public class bloodBank extends AppCompatActivity {
    ImageView holyfamily;
    ImageView PIMS;
    ImageView CMH;
    ImageView al_Maroof;
    ImageView kalsoom;
    ImageView faujiFoundation;

    ImageView patientblood_backToDashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);

        holyfamily = findViewById(R.id.bloodBankHolyFamily);
        PIMS = findViewById(R.id.bloodBankPIMS);
        CMH = findViewById(R.id.bloodBankCMH);
        al_Maroof = findViewById(R.id.bloodBankAl_Maroof);
        kalsoom = findViewById(R.id.bloodBankKalsoom);
        faujiFoundation = findViewById(R.id.bloodBankFaujiFoundation);



        patientblood_backToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bloodBank.this,patientDashboard.class);
                startActivity(intent);

            }
        });

        holyfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bloodBank.this,bloodBankOptions.class);
                startActivity(intent);

            }
        });

        PIMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bloodBank.this,bloodBankOptions.class);
                startActivity(intent);

            }
        });

        CMH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bloodBank.this,bloodBankOptions.class);
                startActivity(intent);

            }
        });

        al_Maroof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bloodBank.this,bloodBankOptions.class);
                startActivity(intent);

            }
        });

        kalsoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bloodBank.this,bloodBankOptions.class);
                startActivity(intent);

            }
        });

        faujiFoundation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bloodBank.this,bloodBankOptions.class);
                startActivity(intent);

            }
        });


    }
}