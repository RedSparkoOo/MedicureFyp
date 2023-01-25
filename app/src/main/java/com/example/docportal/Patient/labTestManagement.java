package com.example.docportal.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.docportal.R;

public class labTestManagement extends AppCompatActivity {
    ImageView chugtai;
    ImageView temar;
    ImageView citi;
    ImageView excel;
    ImageView idc;
    ImageView nih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_management);

        // assigning hooks (ID's)
        chugtai = findViewById(R.id.chugtaiLab);
        temar = findViewById(R.id.temarLab);
        citi = findViewById(R.id.citiLab);
        excel = findViewById(R.id.excelLab);
        idc = findViewById(R.id.idcLab);
        nih = findViewById(R.id.nihLab);





        // Implementing On clicks

        // On click listener for Chugtai Lab
        chugtai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(labTestManagement.this,labTestManagement_options.class);
                startActivity(intent);
            }
        });

        // On click listener for temar Lab
        temar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(labTestManagement.this,labTestManagement_options.class);
                startActivity(intent);
            }
        });

        // On click listener for citi Lab
        citi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(labTestManagement.this,labTestManagement_options.class);
                startActivity(intent);
            }
        });

        // On click listener for excel Lab
        excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(labTestManagement.this,labTestManagement_options.class);
                startActivity(intent);
            }
        });

        // On click listener for idc Lab
        idc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(labTestManagement.this,labTestManagement_options.class);
                startActivity(intent);
            }
        });

        // On click listener for nih Lab
        nih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(labTestManagement.this,labTestManagement_options.class);
                startActivity(intent);
            }
        });


    }
}